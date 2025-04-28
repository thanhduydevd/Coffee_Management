/*
 * @ (#) TaiKhoan.java   1.0 4/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.damcafe.app.entity;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 4/27/2025
 * version: 1.0
 */
public class TaiKhoan {
    private final String maTaiKhoan;
    private NhanVien maNhanVien;
    private String tenTaiKhoan;
    private String matKhau;


    // Constructor
    public TaiKhoan(String maTaiKhoan, NhanVien maNhanVien, String tenTaiKhoan, String matKhau) {
        this.maTaiKhoan = maTaiKhoan;
        this.maNhanVien = maNhanVien;
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
    }

    public TaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    // Getter and Setter methods
    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public NhanVien getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(NhanVien maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }


}
