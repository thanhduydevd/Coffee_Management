package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 5/1/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Size;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderDetail_DAO {
    public static ArrayList<OrderDetail> loadOrderFromDB() {
        ArrayList<OrderDetail> list = new ArrayList<>();

        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM ChiTietHoaDon";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String maCTHoaDon = rs.getString("maCTHoaDon");
                String maHoaDon = rs.getString("maHoaDon");
                String maSanPham = rs.getString("maSanPham");
                String kichThuoc = rs.getString("kichThuoc");
                int soLuong = rs.getInt("soLuong");
                double donGia = rs.getDouble("donGia");
                double thanhTien = rs.getDouble("thanhTien");
                String ghiChu = rs.getString("ghiChu");


                OrderDetail od = new OrderDetail(maCTHoaDon, maHoaDon, maSanPham,
                        Size.valueOf(kichThuoc), soLuong, donGia, ghiChu);
                list.add(od);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}
