package com.damcafe.app.controller;

import com.damcafe.app.entity.ProductCategory;
import dao.ProductCategory_DAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.List;

public class ProductManagementController {

    @FXML
    private ComboBox<ProductCategory> product_category;
    @FXML
    private ComboBox<ProductCategory>  product_search_category;
    @FXML
    private ComboBox<String> product_search_sort;
    @FXML

    private ProductCategory_DAO productCategoryDAO;

    public void initialize() {
        // 1. Lấy danh sách loại sản phẩm từ DAO
        productCategoryDAO = new ProductCategory_DAO();
        List<ProductCategory> productCategories = productCategoryDAO.getAllProductCategories();
        product_category.getItems().setAll(productCategories);
        product_search_category.getItems().setAll(productCategories);

        // 3. Cài đặt cách hiển thị tên loại sản phẩm
        product_category.setConverter(new StringConverter<ProductCategory>() {
            @Override
            public String toString(ProductCategory category) {
                return category == null ? "" : category.getTenLoaiSanPham();
            }
            @Override
            public ProductCategory fromString(String string) {
                return null; // không dùng
            }
        });
        product_search_category.setConverter(new StringConverter<ProductCategory>() {
            @Override
            public String toString(ProductCategory category) {
                return category == null ? "" : category.getTenLoaiSanPham();
            }
            @Override
            public ProductCategory fromString(String string) {
                return null; // không dùng
            }
        });

        // ----------- Thêm lựa chọn cho ComboBox sắp xếp ----------
        product_search_sort.getItems().addAll(
                "Mặc định",
                "Tên A → Z",
                "Tên Z → A",
                "Giá thấp → cao",
                "Giá cao → thấp"
        );

        // Cài đặt mặc định chọn "Mặc định"
        product_search_sort.getSelectionModel().selectFirst();

        // 4. Thêm listener để demo khi chọn mục
        product_category.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                System.out.println("Bạn vừa chọn: "
                        + newVal.getMaLoaiSanPham()
                        + " - " + newVal.getTenLoaiSanPham());
            }
        });
    }
}
