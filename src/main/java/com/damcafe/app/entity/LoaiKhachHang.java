package com.damcafe.app.entity;

public enum LoaiKhachHang {
    THANH_VIEN("Thành viên"),
    VANG_LAI("vãng lai");

    private String ten;

    LoaiKhachHang(String ten) {
        this.ten = ten;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
