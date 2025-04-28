package com.damcafe.app.entity;

import java.time.LocalDate;
import java.util.Objects;

public class NhanVien {
    private final String maNhanVien;
    private String tenNhanVien;
    private String diaChi;
    private String sdt;
    private LocalDate ngayVaoLam;
    private double luong;
    private String email;

    public NhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }


    public NhanVien(String maNhanVien, String tenNhanVien, String diaChi, String sdt, LocalDate ngayVaoLam, double luong, String email) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.ngayVaoLam = ngayVaoLam;
        this.luong = luong;
        this.email = email;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
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

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(maNhanVien.toLowerCase(), nhanVien.maNhanVien.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maNhanVien.toLowerCase());
    }
}
