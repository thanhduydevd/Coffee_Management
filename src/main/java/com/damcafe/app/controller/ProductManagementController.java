package com.damcafe.app.controller;

import com.damcafe.app.dao.Product_DAO;
import com.damcafe.app.entity.Product;
import com.damcafe.app.entity.ProductCategory;
import com.damcafe.app.dao.ProductCategory_DAO;
import com.damcafe.app.entity.Size;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductManagementController {

    @FXML private ComboBox<ProductCategory> product_category;
    @FXML private ComboBox<ProductCategory> product_search_category;
    @FXML private ComboBox<String> product_search_sort;
    @FXML private ComboBox<Size> product_size;
    @FXML private TextField product_id;
    @FXML private TextField product_name;
    @FXML private TextField product_price;
    @FXML private TextArea product_describe;
    @FXML private Button product_image;
    @FXML private Button button_add;
    @FXML private Button button_update;
    @FXML private Button button_delete;
    @FXML private Button button_clear;
    @FXML private TableView<Product> productTable;

    @FXML private TableColumn<Product, String> colMaSanPham;
    @FXML private TableColumn<Product, String> colTenSanPham;
    @FXML private TableColumn<Product, ProductCategory> colLoaiSanPham;
    @FXML private TableColumn<Product, String> colKichThuoc;
    @FXML private TableColumn<Product, Integer> colGiaGoc;
    @FXML private TableColumn<Product, String> colMoTa;
    @FXML private TableColumn<Product, String> colHinhAnh;
    @FXML private TableColumn<Product, Integer> colTrangThai;
    @FXML private Label message;
    @FXML private TextField product_search_name;

    private String selectedAbsolutePath;
    private String selectedRelativeName;
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ArrayList<Product> productsDB = Product_DAO.getAllProducts();
    public void initialize() {
        List<ProductCategory> categories = ProductCategory_DAO.getAllProductCategories();
        product_category.getItems().setAll(categories);
        product_search_category.getItems().setAll(categories);
        product_category.getSelectionModel().selectFirst();
        StringConverter<ProductCategory> conv = new StringConverter<>() {
            @Override public String toString(ProductCategory c) { return c == null ? "" : c.getTenLoaiSanPham(); }
            @Override public ProductCategory fromString(String s) { return null; }
        };
        product_category.setConverter(conv);
        product_search_category.setConverter(conv);
        product_search_category.getItems().add(new ProductCategory("Tất cả", "Tất cả"));
        product_search_category.getSelectionModel().selectLast();
        product_search_sort.getItems().addAll("Mặc định", "Tên A → Z", "Tên Z → A", "Giá thấp → cao", "Giá cao → thấp");
        product_search_sort.getSelectionModel().selectFirst();

        product_size.getItems().addAll(Size.values());
        product_size.getSelectionModel().selectFirst();
        product_size.setConverter(new StringConverter<>() {
            @Override public String toString(Size size) { return size == null ? "" : size.toString(); }
            @Override public Size fromString(String s) { return Size.valueOf(s); }
        });

        colMaSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        colLoaiSanPham.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
        colKichThuoc.setCellValueFactory(new PropertyValueFactory<>("kichThuoc"));
        colGiaGoc.setCellValueFactory(new PropertyValueFactory<>("giaGoc"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colHinhAnh.setCellValueFactory(new PropertyValueFactory<>("hinhAnh"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        colHinhAnh.setCellFactory(column -> new TableCell<>() {
            private final ImageView iv = new ImageView();
            {
                iv.setFitHeight(50);
                iv.setFitWidth(50);
                iv.setPreserveRatio(true);
                setAlignment(Pos.CENTER);
            }
            @Override
            protected void updateItem(String fileName, boolean empty) {
                super.updateItem(fileName, empty);
                if (empty || fileName == null || fileName.isBlank()) {
                    setGraphic(null);
                } else {
                    File file = new File("src/main/data_images/products/" + fileName);
                    if (file.exists()) {
                        Image img = new Image(file.toURI().toString());
                        iv.setImage(img);
                        setGraphic(iv);
                    } else {
                        System.err.println("Không tìm thấy ảnh: " + file.getAbsolutePath());
                        setGraphic(null);
                    }
                }
            }
        });

        for (Product p : productsDB) {
            productList.add(p);
        }
        productTable.setItems(productList);

        // Xử lý sự kiện
        product_image.setOnAction(e -> chooseImage());
        button_add.setOnAction(e -> addProduct());
        button_delete.setOnAction(e -> deleteProduct());
        button_update.setOnAction(e -> updateProduct());
        button_clear.setOnAction(e -> clearForm());
        product_search_name.setOnKeyReleased(e -> filterAll());
        product_search_category.setOnAction(e -> filterAll());
        product_search_sort.setOnAction(e -> filterAll());

        productTable.setOnMouseClicked(e -> {
            Product p = productTable.getSelectionModel().getSelectedItem();
            if (p != null) {
                product_id.setText(p.getMaSanPham());
                product_name.setText(p.getTenSanPham());
                product_price.setText(String.valueOf(p.getGiaGoc()));
                product_describe.setText(p.getMoTa());
                product_category.setValue(p.getLoaiSanPham());
                selectedRelativeName = p.getHinhAnh();
                product_image.setText(selectedRelativeName);
                product_id.setEditable(false);
            }
        });
    }

    private String getRelativeImageName(String absolutePath) {
        return new File(absolutePath).getName();
    }

    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chọn ảnh sản phẩm");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = chooser.showOpenDialog(product_image.getScene().getWindow());
        if (file != null) {
            selectedAbsolutePath = file.getAbsolutePath();
            selectedRelativeName = file.getName(); // Chỉ lưu tên file

            // Hiển thị tên ảnh lên UI
            product_image.setText(selectedRelativeName);
            System.out.println("Chọn ảnh: " + selectedRelativeName);

            // Đường dẫn thư mục đích ngoài resources
            Path targetPath = Paths.get("src/main/data_images/products/" + selectedRelativeName);

            try {
                Files.createDirectories(targetPath.getParent()); // Đảm bảo thư mục tồn tại
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Đã lưu ảnh vào: " + targetPath);
            } catch (IOException e) {
                System.err.println("Lỗi khi lưu ảnh: " + e.getMessage());
            }
        }
    }


    private boolean validateData() {
        String id     = product_id.getText().trim();
        String name   = product_name.getText().trim();
        String priceS = product_price.getText().trim();
        String desc   = product_describe.getText().trim();
        ProductCategory cat = product_category.getValue();
        Size size = product_size.getValue();

        if (id.isEmpty() || !id.matches("SP\\d{3}")) {
            message.setText("Mã sản phẩm phải chính xác. VD: SP001!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        if(name.isEmpty()) {
            message.setText("Tên sản phẩm không được để trống!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (cat == null) {
            message.setText("Chưa chọn loại sản phẩm!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (size == null) {
            message.setText("Chưa chọn kích thước sản phẩm!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (selectedRelativeName == null) {
            message.setText("Chưa chọn hình ảnh sản phẩm!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (desc.isEmpty()) {
            message.setText("Mô tả sản phẩm không được để trống!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (priceS.isEmpty()) {
            message.setText("Giá sản phẩm không được để trống!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!priceS.matches("\\d+")) {
            message.setText("Giá phải là số nguyên dương!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (Integer.parseInt(priceS) <= 0) {
            message.setText("Giá phải lớn hơn 0!");
            message.setStyle("-fx-text-fill: red;");
            return false;
        }

        return true;
    }

    private Product readData() {
        String id     = product_id.getText().trim();
        String name   = product_name.getText().trim();
        String priceS = product_price.getText().trim();
        String desc   = product_describe.getText().trim();
        ProductCategory cat = product_category.getValue();
        Size size = product_size.getValue();
        int price = Integer.parseInt(priceS);

        Product p = new Product(id, name, cat, size, price, desc, selectedRelativeName, 1);
        return p;
    }

    private void updateTable () {
        productList.clear();
        productTable.refresh();
        productsDB.clear();
        productsDB = Product_DAO.getAllProducts();
        for (Product p : productsDB) {
            productList.add(p);
        }
    }

    private void addProduct() {
        if (validateData()) {
            Product p = readData();
            if (Product_DAO.addProductToDB(p)) {
                productList.add(p);
                message.setText("Thêm sản phẩm thành công!");
                message.setStyle("-fx-text-fill: green;");
                clearForm();
                productTable.refresh();
                System.out.println("Thêm sản phẩm thành công: " + p);
            } else {
                message.setText("Mã sản phẩm đã tồn tại!");
                message.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void deleteProduct() {
        Product sel = productTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            message.setText("Chưa chọn sản phẩm!");
            message.setStyle("-fx-text-fill: red;");
            return;
        }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Xóa sản phẩm " + sel.getTenSanPham() + "?",
                ButtonType.OK, ButtonType.CANCEL
        );
        if (a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (Product_DAO.deleteProductToDB(sel.getMaSanPham())) {
                updateTable();
                message.setText("Xóa sản phẩm thành công!");
                message.setStyle("-fx-text-fill: green;");
                clearForm();
            } else {
                message.setText("Xóa sản phẩm thất bại!");
                message.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void updateProduct() {
        Product product = productTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            message.setText("Chưa chọn sản phẩm!");
            message.setStyle("-fx-text-fill: red;");
            return;
        } else  {
            if (validateData()) {
                Product p = readData();
                if (Product_DAO.updateProductToDB(p)) {
                    updateTable();
                    message.setText("Cập nhật sản phẩm thành công!");
                    message.setStyle("-fx-text-fill: green;");
                    clearForm();
                } else {
                    message.setText("Cập nhật sản phẩm thất bại!");
                    message.setStyle("-fx-text-fill: red;");
                }
            }
        }

    }

    private void filterAll() {
        String searchText = product_search_name.getText().trim().toLowerCase();
        ProductCategory selectedCategory = product_search_category.getValue();
        String selectedSort = product_search_sort.getValue();

        List<Product> filteredList = new ArrayList<>(productsDB);

        // Lọc theo tên
        if (!searchText.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(p -> p.getTenSanPham().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        }

        // Lọc theo loại sản phẩm
        if (selectedCategory != null) {
            if (!selectedCategory.getTenLoaiSanPham().equals("Tất cả")) {
                filteredList = filteredList.stream()
                        .filter(p -> p.getLoaiSanPham().getTenLoaiSanPham().equals(selectedCategory.getTenLoaiSanPham()))
                        .collect(Collectors.toList());
            } else {
                filteredList = new ArrayList<>(productsDB);
            }
        }

        // Sắp xếp nếu có lựa chọn
        if (selectedSort != null && !selectedSort.equals("Mặc định")) {
            if (selectedSort.equals("Tên A → Z")) {
                filteredList.sort(Comparator.comparing(Product::getTenSanPham));
            } else if (selectedSort.equals("Tên Z → A")) {
                filteredList.sort(Comparator.comparing(Product::getTenSanPham).reversed());
            } else if (selectedSort.equals("Giá thấp → cao")) {
                filteredList.sort(Comparator.comparingDouble(Product::getGiaGoc));
            } else if (selectedSort.equals("Giá cao → thấp")) {
                filteredList.sort(Comparator.comparingDouble(Product::getGiaGoc).reversed());
            }
        }

        // Hiển thị kết quả cuối cùng
        productTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void clearForm() {
        product_id.clear(); product_name.clear(); product_price.clear(); product_describe.clear();
        product_category.getSelectionModel().clearSelection();
        selectedAbsolutePath = null; selectedRelativeName = null;
        product_image.setText("Chọn ảnh sản phẩm");
        product_id.setEditable(true);
    }
}
