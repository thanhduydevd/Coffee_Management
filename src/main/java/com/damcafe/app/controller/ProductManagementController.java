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
import java.util.ArrayList;
import java.util.List;

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
        // Khởi tạo DAO và load danh sách loại sản phẩm
        List<ProductCategory> categories = ProductCategory_DAO.getAllProductCategories();
        product_category.getItems().setAll(categories);
        product_search_category.getItems().setAll(categories);

        // Hiển thị tên loại sản phẩm trong ComboBox
        StringConverter<ProductCategory> conv = new StringConverter<>() {
            @Override public String toString(ProductCategory c) { return c == null ? "" : c.getTenLoaiSanPham(); }
            @Override public ProductCategory fromString(String s) { return null; }
        };
        product_category.setConverter(conv);
        product_search_category.setConverter(conv);

        // Cài đặt lựa chọn sắp xếp
        product_search_sort.getItems().addAll("Mặc định", "Tên A → Z", "Tên Z → A", "Giá thấp → cao", "Giá cao → thấp");
        product_search_sort.getSelectionModel().selectFirst();

        // Cài đặt lựa chọn kích thước
        product_size.getItems().addAll(Size.values());
        product_size.getSelectionModel().selectFirst();
        product_size.setConverter(new StringConverter<>() {
            @Override public String toString(Size size) { return size == null ? "" : size.toString(); }
            @Override public Size fromString(String s) { return Size.valueOf(s); }
        });

        // Ánh xạ cột với thuộc tính Product
        colMaSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        colLoaiSanPham.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
        colKichThuoc.setCellValueFactory(new PropertyValueFactory<>("kichThuoc"));
        colGiaGoc.setCellValueFactory(new PropertyValueFactory<>("giaGoc"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colHinhAnh.setCellValueFactory(new PropertyValueFactory<>("hinhAnh"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // Hiển thị hình ảnh từ resource
        colHinhAnh.setCellFactory(column -> new TableCell<>() {
            private final ImageView iv = new ImageView();
            {
                iv.setFitHeight(50);
                iv.setFitWidth(50);
                iv.setPreserveRatio(true);
                setAlignment(Pos.CENTER);
            }
            @Override protected void updateItem(String fileName, boolean empty) {
                super.updateItem(fileName, empty);
                if (empty || fileName == null || fileName.isBlank()) {
                    setGraphic(null);
                } else {
                    try {
                        Image img = new Image(getClass().getResource(
                                "/com/damcafe/app/images/products/" + fileName
                        ).toExternalForm());
                        iv.setImage(img);
                        setGraphic(iv);
                    } catch (Exception e) {
                        setGraphic(null);
                        System.err.println("Lỗi load ảnh: " + fileName);
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
        product_search_name.setOnKeyReleased(e -> searchProductByName());
        product_search_category.setOnAction(e -> searchProductByCategory());
        product_search_sort.setOnAction(e -> searchProductBySort());

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

    /** Trả về tên file để lưu vào CSDL và load lại từ resource */
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
            selectedRelativeName = getRelativeImageName(selectedAbsolutePath);
            product_image.setText(selectedRelativeName);
            System.out.println("Chọn ảnh: " + selectedRelativeName);
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

        if (productList.stream().anyMatch(p -> p.getMaSanPham().equals(id))) {
            message.setText("Mã sản phẩm đã tồn tại!");
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
            } else {
                message.setText("Thêm sản phẩm thất bại!");
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
            Product newProduct = readData();
            if (Product_DAO.updateProductToDB(newProduct)) {
                updateTable();
                message.setText("Cập nhật sản phẩm thành công!");
                message.setStyle("-fx-text-fill: green;");
                clearForm();
                productTable.getSelectionModel().clearSelection();
                System.out.println(newProduct);
            } else {
                message.setText("Cập nhật sản phẩm thất bại!");
                message.setStyle("-fx-text-fill: red;");
            }
        }

    }

    private void searchProductByName() {
        String searchText = product_search_name.getText().trim();
        if (searchText.isEmpty()) {
            productTable.setItems(productList);
            return;
        }
        List<Product> filteredList = new ArrayList<>();
        for (Product p : productList) {
            if (p.getTenSanPham().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(p);
            }
        }
        productTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void searchProductByCategory() {
        ProductCategory selectedCategory = product_search_category.getValue();
        if (selectedCategory == null) {
            productTable.setItems(productList);
            return;
        }
        List<Product> filteredList = new ArrayList<>();
        for (Product p : productList) {
            if (p.getLoaiSanPham().equals(selectedCategory)) {
                filteredList.add(p);
            }
        }
        productTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void searchProductBySort() {
        String selectedSort = product_search_sort.getValue();
        if (selectedSort.equals("Mặc định")) {
            productTable.setItems(productList);
            return;
        }
        List<Product> sortedList = new ArrayList<>(productList);
        if (selectedSort.equals("Tên A → Z")) {
            sortedList.sort((p1, p2) -> p1.getTenSanPham().compareTo(p2.getTenSanPham()));
        } else if (selectedSort.equals("Tên Z → A")) {
            sortedList.sort((p1, p2) -> p2.getTenSanPham().compareTo(p1.getTenSanPham()));
        } else if (selectedSort.equals("Giá thấp → cao")) {
            sortedList.sort((p1, p2) -> Double.compare(p1.getGiaGoc(), p2.getGiaGoc()));
        } else if (selectedSort.equals("Giá cao → thấp")) {
            sortedList.sort((p1, p2) -> Double.compare(p2.getGiaGoc(), p1.getGiaGoc()));
        }
        productTable.setItems(FXCollections.observableArrayList(sortedList));
    }

    private void clearForm() {
        product_id.clear(); product_name.clear(); product_price.clear(); product_describe.clear();
        product_category.getSelectionModel().clearSelection();
        selectedAbsolutePath = null; selectedRelativeName = null;
        product_image.setText("Chọn ảnh sản phẩm");
        product_id.setEditable(true);
    }
}
