package com.damcafe.app.entity;

public enum LoaiSanPham {
    Coffe("Cafe"),
    TraSua("Trà sữa"),
    MonAn("Món ăn");

    private String loai;

    private LoaiSanPham (String loai){
        this.loai = loai;
    }
    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}
