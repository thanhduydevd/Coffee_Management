/*
 * @ (#) TaiKhoan_DAO.java   1.0 4/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.damcafe.app.dao;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.NhanVien;
import com.damcafe.app.entity.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 4/27/2025
 * version: 1.0
 */
public class TaiKhoan_DAO {
    public TaiKhoan getTaiKhoan(String maNV){
        TaiKhoan taiKhoan = null;
        String sql = "SELECT * FROM TaiKhoan WHERE maNhanVien = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, maNV);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                taiKhoan = new TaiKhoan(rs.getString("maTaiKhoan"));
                taiKhoan.setMaNhanVien(new NhanVien(rs.getString("maNhanVien")));
                taiKhoan.setTenTaiKhoan(rs.getString("tenTaiKhoan"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taiKhoan;
    }
    public static TaiKhoan getTaiKhoanTheoTen(String ten){
        TaiKhoan taiKhoan = null;
        String sql = "SELECT * FROM TaiKhoan WHERE tenTaiKhoan = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ten);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                taiKhoan = new TaiKhoan(rs.getString("maTaiKhoan"));
                taiKhoan.setMaNhanVien(new NhanVien(rs.getString("maNhanVien")));
                taiKhoan.setTenTaiKhoan(rs.getString("tenTaiKhoan"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taiKhoan;
    }
    public boolean addTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "INSERT INTO TaiKhoan (maTaiKhoan, maNhanVien, tenTaiKhoan, matKhau) VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement state = con.prepareStatement(sql)) {

            state.setString(1, taiKhoan.getMaTaiKhoan());
            state.setString(2, taiKhoan.getMaNhanVien().getMaNhanVien());
            state.setString(3, taiKhoan.getTenTaiKhoan());
            state.setString(4, taiKhoan.getMatKhau());


            int rowsAffected = state.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean updatePass(String newPass, String tenTaiKhoan) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenTaiKhoan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement state = con.prepareStatement(sql)) {

            state.setString(1, newPass);
            state.setString(2, tenTaiKhoan);

            int rowsAffected = state.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
