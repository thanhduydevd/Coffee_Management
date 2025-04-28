package com.damcafe.app.entity;

import java.util.Objects;
import com.damcafe.app.entity.ProductCategory;

public class SanPham {
    private final String maSanPham;
    private String tenSanPham;
    private double gia;
    private boolean bestSeller;
    private int soLuong;
    private ProductCategory loai;  // dùng ProductCategory

    public SanPham(String maSanPham, String tenSanPham, double gia,
                   boolean bestSeller, int soLuong, ProductCategory loai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.bestSeller = bestSeller;
        this.soLuong = soLuong;
        this.loai = loai;
    }

    public SanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public ProductCategory getLoai() {
        return loai;
    }

    public void setLoai(ProductCategory loai) {
        this.loai = loai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SanPham sanPham = (SanPham) o;
        return Objects.equals(maSanPham, sanPham.maSanPham);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maSanPham);
    }

    @Override
    public String toString() {
        // Chỉ trả về tên sản phẩm; nếu cần bạn có thể hiển thị thêm loại:
        // return tenSanPham + " (" + loai.getTenLoaiSanPham() + ")";
        return tenSanPham;
    }
}
