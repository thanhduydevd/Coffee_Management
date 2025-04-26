package com.damcafe.app.entity;

import java.util.Objects;

public class KhachHang {
    private final String maKhachHang;
    private String tenKhachHang;
    private String diaChi;
    private String sdt;
    private int diemTichLuy;
    private LoaiKhachHang loai;

    public KhachHang(String maKhachHang, String tenKhachHang, String diaChi, String sdt, LoaiKhachHang loai) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.diemTichLuy = 0;
        this.loai = loai;
    }

    public KhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }


    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public LoaiKhachHang getLoai() {
        return loai;
    }

    public void setLoai(LoaiKhachHang loai) {
        this.loai = loai;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(maKhachHang, khachHang.maKhachHang);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKhachHang);
    }
}
