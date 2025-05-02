/*
 * @ (#) NhanVien_DAO.java   1.0 4/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
}
