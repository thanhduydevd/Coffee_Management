package com.damcafe.app.entity;

import java.util.Objects;

public class SanPham {
    private final String maSanPham;
    private String tenSanPham;
    private double gia;
    private boolean isBestSeller;
    private int soLuong;
    private LoaiSanPham loai;

    public SanPham(String maSanPham, String tenSanPham, double gia, boolean isBestSeller, int soLuong, LoaiSanPham loai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.isBestSeller = isBestSeller;
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
        return isBestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        isBestSeller = bestSeller;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public LoaiSanPham getLoai() {
        return loai;
    }

    public void setLoai(LoaiSanPham loai) {
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
        return String.format("");
    }
}
