package com.damcafe.app.controller;

import com.damcafe.app.entity.Product;
import com.damcafe.app.entity.ProductCategory;
import com.damcafe.app.dao.ProductCategory_DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductManagementController {

    @FXML
    private ComboBox<ProductCategory> product_category;
    @FXML
    private ComboBox<ProductCategory> product_search_category;
    @FXML
    private ComboBox<String> product_search_sort;
    @FXML
    private TextField product_id;
    @FXML
    private TextField product_name;
    @FXML
    private TextField product_price;
    @FXML
    private TextArea product_describe;
    @FXML
    private Button product_image;
    @FXML
    private Button button_add;
    @FXML
    private Button button_update;
    @FXML
    private Button button_delete;
    @FXML
    private TableView<Product> productTable;

    @FXML private TableColumn<Product, String> colMaSanPham;
    @FXML private TableColumn<Product, String> colTenSanPham;
    @FXML private TableColumn<Product, ProductCategory> colLoaiSanPham;
    @FXML private TableColumn<Product, String> colKichThuoc;
    @FXML private TableColumn<Product, Integer> colGiaGoc;
    @FXML private TableColumn<Product, String> colMoTa;
    @FXML private TableColumn<Product, String> colHinhAnh;
    @FXML private TableColumn<Product, Integer> colTrangThai;
    @FXML
    private Label message;
    @FXML
    private ImageView productImageView;

    private ProductCategory_DAO productCategoryDAO;
    private String selectedImagePath = null;
    ObservableList<Product> productList = FXCollections.observableArrayList();

    public void initialize() {
        // Khởi tạo DAO
        productCategoryDAO = new ProductCategory_DAO();

        // Load danh sách loại sản phẩm
        List<ProductCategory> productCategories = productCategoryDAO.getAllProductCategories();
        product_category.getItems().setAll(productCategories);
        product_search_category.getItems().setAll(productCategories);

        // Cài đặt hiển thị ComboBox loại sản phẩm
        product_category.setConverter(new StringConverter<ProductCategory>() {
            @Override
            public String toString(ProductCategory category) {
                return category == null ? "" : category.getTenLoaiSanPham();
            }
            @Override
            public ProductCategory fromString(String string) {
                return null;
            }
        });
        product_search_category.setConverter(new StringConverter<ProductCategory>() {
            @Override
            public String toString(ProductCategory category) {
                return category == null ? "" : category.getTenLoaiSanPham();
            }
            @Override
            public ProductCategory fromString(String string) {
                return null;
            }
        });

        // Thêm các lựa chọn sắp xếp
        product_search_sort.getItems().addAll(
                "Mặc định",
                "Tên A → Z",
                "Tên Z → A",
                "Giá thấp → cao",
                "Giá cao → thấp"
        );
        product_search_sort.getSelectionModel().selectFirst();

        // Thiết lập ánh xạ giữa cột và thuộc tính trong Product
        colMaSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        colLoaiSanPham.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
        colKichThuoc.setCellValueFactory(new PropertyValueFactory<>("kichThuoc"));
        colGiaGoc.setCellValueFactory(new PropertyValueFactory<>("giaGoc"));
        colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colHinhAnh.setCellValueFactory(new PropertyValueFactory<>("hinhAnh"));
        colHinhAnh.setCellFactory(column -> {
            return new TableCell<Product, String>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(50);  // Kích thước ảnh hiển thị
                    imageView.setFitWidth(50);
                    imageView.setPreserveRatio(true);
                    setAlignment(Pos.CENTER);
                }

                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);

                    if (empty || imagePath == null || imagePath.isBlank()) {
                        setGraphic(null);
                    } else {
                        try {
                            javafx.scene.image.Image image = new javafx.scene.image.Image("file:" + imagePath, 100, 100, true, true);
                            imageView.setImage(image);
                            setGraphic(imageView);
                        } catch (Exception e) {
                            setGraphic(null);
                        }
                    }
                }
            };
        });

        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));


        productTable.setItems(productList);

        // Xử lý chọn ảnh sản phẩm
        product_image.setOnAction(event -> chooseImage());

        // Xử lý nút thêm sản phẩm
        button_add.setOnAction(event -> addProduct());
    }

    public static String getRelativeImagePath(String absolutePath) {
        File file = new File(absolutePath);
        String fileName = file.getName();

        String relativePath = "../../images/products/coffee/" + fileName;

        return relativePath;
    }

    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh sản phẩm");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.avif")
        );

        java.io.File selectedFile = fileChooser.showOpenDialog(product_image.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            String relativePath = getRelativeImagePath(selectedImagePath);
            product_image.setText(relativePath);

            // In ra đường dẫn tương đối (hoặc làm gì đó với nó)
            System.out.println("Relative Path: " + relativePath);
        }
    }

    private void addProduct() {
        String id = product_id.getText().trim();
        String name = product_name.getText().trim();
        String priceStr = product_price.getText().trim();
        String description = product_describe.getText().trim();
        ProductCategory category = product_category.getValue();

        if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty() || category == null || selectedImagePath == null) {
            message.setText("Vui lòng điền đầy đủ thông tin sản phẩm!");
            message.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!priceStr.matches("\\d+")) {
            message.setText("Giá sản phẩm phải là số nguyên dương!");
            message.setStyle("-fx-text-fill: red;");
            return;
        }

        int price = Integer.parseInt(priceStr);
        if (price <= 0) {
            message.setText("Giá sản phẩm phải lớn hơn 0!");
            message.setStyle("-fx-text-fill: red;");
            return;
        }

        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Price: " + priceStr);
        System.out.println("Description: " + description);
        System.out.println("Category: " + category);
        System.out.println("Image Path: " + selectedImagePath);

        productList.add(new Product(id, name, category, "All", price, description, selectedImagePath, 1));

        message.setText("Thêm sản phẩm thành công!");
        message.setStyle("-fx-text-fill: green;");

        // Clear the form
        clearForm();
    }

    private void clearForm() {
        product_id.clear();
        product_name.clear();
        product_price.clear();
        product_describe.clear();
        product_category.getSelectionModel().clearSelection();
        selectedImagePath = null;
        product_image.setText("Chọn ảnh sản phẩm");
    }

}
