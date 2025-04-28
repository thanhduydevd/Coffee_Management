/*
 * @ (#) ProductCategory.java   1.0 28/4/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.damcafe.app.entity;

/*
 * @description
 * @author: Tran Tuan Hung
 * @date: 28/4/25
 * @version: 1.0
 */
public class ProductCategory {
    private String maLoaiSanPham;
    private String tenLoaiSanPham;

    public ProductCategory(String maLoaiSanPham, String tenLoaiSanPham) {
        this.maLoaiSanPham = maLoaiSanPham;
        this.tenLoaiSanPham = tenLoaiSanPham;
    }

    public String getMaLoaiSanPham() {
        return maLoaiSanPham;
    }

    public void setMaLoaiSanPham(String maLoaiSanPham) {
        this.maLoaiSanPham = maLoaiSanPham;
    }

    public String getTenLoaiSanPham() {
        return tenLoaiSanPham;
    }

    public void setTenLoaiSanPham(String tenLoaiSanPham) {
        this.tenLoaiSanPham = tenLoaiSanPham;
    }

    @Override
    public String toString() {
        return tenLoaiSanPham; // để hiển thị tên loại sản phẩm khi đưa vào ComboBox
    }
}


