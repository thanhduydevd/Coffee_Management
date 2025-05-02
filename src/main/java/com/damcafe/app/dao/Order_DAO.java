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
    public static ArrayList<Order> loadVaoOrderList() {

        ArrayList<Order> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            String sql = "SELECT \n" +
                    "    hd.maHoaDon,\n" +
                    "    hd.ngayTao,\n" +
                    "    hd.maNhanVien,\n" +
                    "    hd.hinhThuc,\n" +
                    "\thd.maKhuyenMai,\n" +
                    "    hd.maBan AS maBan,\n" +
                    "    SUM(ct.thanhTien) AS tongTien\n" +
                    "FROM HoaDon hd\n" +
                    "JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon\n" +
                    "GROUP BY \n" +
                    "    hd.maHoaDon,\n" +
                    "    hd.ngayTao,\n" +
                    "    hd.maNhanVien,\n" +
                    "\thd.maKhuyenMai,\n" +
                    "    hd.hinhThuc,\n" +
                    "    hd.maBan\n" +
                    "ORDER BY hd.ngayTao DESC;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                Date ngayTao = rs.getDate("ngayTao");
                String maNhanVien = rs.getString("maNhanVien");
                int mangVe = rs.getInt("hinhThuc");
                String maBan = rs.getString("maBan");
                String maKhuyenMai = rs.getString("maKhuyenMai");
                double tongTien = rs.getDouble("tongTien");

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
    public static boolean addOrderToDB(Order hd) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Connection conn = ConnectDB.getConnection();
        PreparedStatement psHoaDon = null;

        try {
            conn.setAutoCommit(false); // Bắt đầu transaction

            String sql = "INSERT INTO HoaDon (maHoaDon, ngayTao, maNhanVien, hinhThuc, maBan,maKhuyenMai) VALUES (?, ?, ?, ?, ?, ?)";
            psHoaDon = conn.prepareStatement(sql);

            psHoaDon.setString(1, hd.getOrderID());
            psHoaDon.setDate(2, java.sql.Date.valueOf(hd.getDate()));
            psHoaDon.setString(3, hd.getUserID());
            psHoaDon.setBoolean(4, hd.isBringBack());
            psHoaDon.setString(5, hd.getTableID());
            // Kiểm tra giá trị của maKhuyenMai và set giá trị NULL nếu nó là null
            if (hd.getSaleID() == null) {
                psHoaDon.setString(6, "null");  // Set NULL thực sự cho maKhuyenMai
            } else {
                psHoaDon.setString(6, hd.getSaleID());    // Set giá trị của maKhuyenMai nếu có
            }
            psHoaDon.executeUpdate();


            conn.commit(); // Commit nếu mọi thứ OK
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (psHoaDon != null) psHoaDon.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

}


