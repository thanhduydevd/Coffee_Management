/*
 * @ (#) Product.java   1.0 29/4/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.damcafe.app.entity;

/*
 * @description
 * @author: Tran Tuan Hung
 * @date: 29/4/25
 * @version: 1.0
 */

import java.util.Objects;

public class Product {
    private String maSanPham;
    private String tenSanPham;
    private ProductCategory loaiSanPham;
    private Size kichThuoc;
    private double giaGoc;
    private String moTa;       // Có thể null
    private String hinhAnh;    // Có thể null
    private int trangThai;

    public Product(String maSanPham, String tenSanPham, ProductCategory loaiSanPham, Size kichThuoc, double giaGoc, String moTa, String hinhAnh, int trangThai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.loaiSanPham = loaiSanPham;
        this.kichThuoc = kichThuoc;
        this.giaGoc = giaGoc;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.trangThai = trangThai;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public ProductCategory getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(ProductCategory loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public Size getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(Size kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public double getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(int giaGoc) {
        this.giaGoc = giaGoc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(maSanPham, product.maSanPham);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maSanPham);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s %s %s",maSanPham,tenSanPham,loaiSanPham,kichThuoc
                ,giaGoc,moTa,hinhAnh,trangThai);
    }
}

