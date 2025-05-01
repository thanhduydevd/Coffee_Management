/*
 * @ (#) NhanVien_DAO.java   1.0 4/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.damcafe.app.dao;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.NhanVien;

import java.sql.*;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 4/27/2025
 * version: 1.0
 */
public class NhanVien_DAO {
    public NhanVien getNhanVien(String id) {
        NhanVien nhanVien = null;
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, id);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                nhanVien = new NhanVien(rs.getString("maNhanVien"));
                nhanVien.setTenNhanVien(rs.getString("tenNhanVien"));
                nhanVien.setEmail(rs.getString("email"));
                nhanVien.setSdt(rs.getString("soDienThoai"));
                nhanVien.setDiaChi(rs.getString("diaChi"));
                nhanVien.setNgayVaoLam(rs.getDate("ngayVaoLam").toLocalDate());
                nhanVien.setLuong(rs.getDouble("luong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVien;
    }
    public static String getTenVoiTenTK(String ten) {
        String maNhanVien = "";
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT maNhanVien FROM TaiKhoan WHERE tenTaiKhoan = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ten);
            ResultSet re = state.executeQuery();

            if (re.next()) {
                maNhanVien = re.getString("maNhanVien");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return maNhanVien;
    }
}
