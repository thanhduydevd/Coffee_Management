package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 5/1/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.Order;
import com.damcafe.app.entity.OrderDetail;

import java.sql.*;
import java.util.ArrayList;

public class Order_DAO {
    public static ArrayList<Order> loadOrderFromDB() {
        ArrayList<Order> list = new ArrayList<>();
        ArrayList<OrderDetail> detaiList = OrderDetail_DAO.loadOrderFromDB();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM HoaDon";  // Bạn có thể thay đổi tên bảng nếu khác
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                Date ngayTao = rs.getDate("ngayTao");
                String maNhanVien = rs.getString("maNhanVien");
                int mangVe = rs.getInt("hinhThuc");
                String maBan = rs.getString("maBan");
                String maKhuyenMai = rs.getString("maKhuyenMai");
                double tongTien =0;
                for (OrderDetail o: detaiList){
                    if(o.getOrderID().equals(maHoaDon)){
                        tongTien = o.getTotal();
                    }
                }
                Order order = new Order(maHoaDon,
                        ngayTao.toLocalDate(),
                        maNhanVien,
                        mangVe == 1,
                        maBan,
                        maKhuyenMai,
                        tongTien);
                list.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tải dữ liệu hóa đơn", e);
        }

        return list;
    }
}


