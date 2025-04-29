package com.damcafe.app.entity;

import java.util.Objects;

public class KhuyenMai {
    private final String maKhuyenMai;
    private double tiGia;

    public KhuyenMai(String maKhuyenMai, double tiGia) {
        this.maKhuyenMai = maKhuyenMai;
        this.tiGia = tiGia;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public double getTiGia() {
        return tiGia;
    }

    public void setTiGia(double tiGia) {
        this.tiGia = tiGia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhuyenMai khuyenMai = (KhuyenMai) o;
        return Objects.equals(maKhuyenMai, khuyenMai.maKhuyenMai);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKhuyenMai);
    }
}
