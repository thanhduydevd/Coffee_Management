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
import java.util.HashMap;
import java.util.Map;

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

    /** Lấy tổng số hoá đơn của ngày hôm nay **/
    public int getTotalOrderToday(){
        String sql = "SELECT COUNT(*) AS SoLuongHoaDonHomNay FROM HoaDon WHERE CAST(ngayTao AS DATE) = CAST(GETDATE() AS DATE)";
        int soHoaDonHomNay = 0;
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                soHoaDonHomNay = rs.getInt("SoLuongHoaDonHomNay");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return soHoaDonHomNay;
    }

    /** Lấy phần trăm số hoá đơn của ngày hôm nay so với ngày hôm qua **/
    public double getPercentageOrders(){
        String sql = "SELECT (SELECT COUNT(*) FROM HoaDon " +
                "WHERE CAST(ngayTao AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE)) AS SoHoaDonHomQua, " +
                "(SELECT COUNT(*) FROM HoaDon WHERE CAST(ngayTao AS DATE) = CAST(GETDATE() AS DATE)) AS SoHoaDonHomNay";
        double ketQua = 0.0;
        double soHoaDonHomQua = 0.0;
        double soHoaDonHomNay = 0.0;
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                soHoaDonHomQua = rs.getDouble("SoHoaDonHomQua");
                soHoaDonHomNay = rs.getDouble("SoHoaDonHomNay");
                if (soHoaDonHomQua == 0) {
                    if (soHoaDonHomNay == 0) {
                        ketQua = 0.0; // Không thay đổi
                    } else {
                        ketQua = 100.0; // Tăng đột biến từ 0
                    }
                } else {
                    ketQua = ((soHoaDonHomNay - soHoaDonHomQua) / soHoaDonHomQua) * 100;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return ketQua;
    }

    /** Lấy tổng tiền của tất cả các hoá đơn trong ngày hôm nay **/
    public double getTotalMoneyToday(){
        String sql = "SELECT SUM(ChiTietHoaDon.soLuong * ChiTietHoaDon.donGia) AS tongTienHomNay " +
                "FROM HoaDon JOIN ChiTietHoaDon ON HoaDon.maHoaDon = ChiTietHoaDon.maHoaDon " +
                "WHERE CAST(HoaDon.ngayTao AS DATE) = CAST(GETDATE() AS DATE)";
        double tongTienHomNay = 0;
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                tongTienHomNay = rs.getDouble("tongTienHomNay");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return tongTienHomNay;
    }

    /** Lấy phần trăm số tiền của ngày hôm nay so với ngày hôm qua **/
    public double getPercentageMoneyByDay(){
        String sql = "SELECT " +
                "(SELECT SUM(ChiTietHoaDon.soLuong * ChiTietHoaDon.donGia) FROM HoaDon " +
                "JOIN ChiTietHoaDon ON HoaDon.maHoaDon = ChiTietHoaDon.maHoaDon " +
                "WHERE CAST(HoaDon.ngayTao AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE)) AS TongTienHomQua, " +
                "(SELECT SUM(ChiTietHoaDon.soLuong * ChiTietHoaDon.donGia) " +
                "FROM HoaDon " +
                "JOIN ChiTietHoaDon ON HoaDon.maHoaDon = ChiTietHoaDon.maHoaDon " +
                "WHERE CAST(HoaDon.ngayTao AS DATE) = CAST(GETDATE() AS DATE)) AS TongTienHomNay";
        double ketQua = 0.0;
        double soTienHomQua = 0.0;
        double soTienHomNay = 0.0;
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                soTienHomQua = rs.getDouble("TongTienHomQua");
                soTienHomNay = rs.getDouble("TongTienHomNay");
                if (soTienHomQua == 0) {
                    if (soTienHomNay == 0) {
                        ketQua = 0.0; // Không thay đổi
                    } else {
                        ketQua = 100.0; // Tăng đột biến từ 0
                    }
                } else {
                    ketQua = ((soTienHomNay - soTienHomQua) / soTienHomQua) * 100;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return ketQua;
    }

    /** Lấy tổng tiền của tất cả các hoá đơn trong tháng này **/
    public double getTotalMoneyThisMonth(){
        String sql = "SELECT SUM(ChiTietHoaDon.soLuong * ChiTietHoaDon.donGia) AS TongTienThangNay FROM HoaDon " +
                "JOIN ChiTietHoaDon ON HoaDon.maHoaDon = ChiTietHoaDon.maHoaDon " +
                "WHERE MONTH(HoaDon.ngayTao) = MONTH(GETDATE()) AND YEAR(HoaDon.ngayTao) = YEAR(GETDATE())";
        double tongTienThangNay = 0.0;
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                tongTienThangNay = rs.getDouble("TongTienThangNay");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return tongTienThangNay;
    }

    /** Lấy phần trăm số tiền của tháng này so với tháng trước **/
    public double getPercentageMoneyThisMonth(){
        String sql = "SELECT " +
                "(SELECT SUM(ChiTietHoaDon.soLuong * ChiTietHoaDon.donGia) FROM HoaDon JOIN ChiTietHoaDon ON HoaDon.maHoaDon = ChiTietHoaDon.maHoaDon WHERE MONTH(HoaDon.ngayTao) = MONTH(DATEADD(MONTH, -1, GETDATE())) AND YEAR(HoaDon.ngayTao) = YEAR(DATEADD(MONTH, -1, GETDATE())) ) AS TongTienThangTruoc, " +
                "(SELECT SUM(ChiTietHoaDon.soLuong * ChiTietHoaDon.donGia) FROM HoaDon JOIN ChiTietHoaDon ON HoaDon.maHoaDon = ChiTietHoaDon.maHoaDon WHERE MONTH(HoaDon.ngayTao) = MONTH(GETDATE()) AND YEAR(HoaDon.ngayTao) = YEAR(GETDATE()) ) AS TongTienThangNay";
        double ketQua = 0.0;
        double soTienThangTruoc = 0.0;
        double soTienThangNay = 0.0;
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                soTienThangTruoc = rs.getDouble("TongTienThangTruoc");
                soTienThangNay = rs.getDouble("TongTienThangNay");
                if (soTienThangTruoc == 0) {
                    if (soTienThangNay == 0) {
                        ketQua = 0.0; // Không thay đổi
                    } else {
                        ketQua = 100.0; // Tăng đột biến từ 0
                    }
                } else {
                    ketQua = ((soTienThangNay - soTienThangTruoc) / soTienThangTruoc) * 100;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return ketQua;
    }

    /** Thống kê doanh thu theo ngày trong tháng hiện tại **/
    public static Map<Integer, Double> getRevenueByDayInCurrentMonth() {
        Map<Integer, Double> data = new HashMap<>();
        String sql = "SELECT DAY(ngayTao) AS ngay, SUM(soLuong * donGia) AS tongTien " +
                "FROM HoaDon " +
                "JOIN ChiTietHoaDon ON HoaDon.maHoaDon = ChiTietHoaDon.maHoaDon " +
                "WHERE MONTH(ngayTao) = MONTH(GETDATE()) AND YEAR(ngayTao) = YEAR(GETDATE()) " +
                "GROUP BY DAY(ngayTao)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int ngay = rs.getInt("ngay");
                double tongTien = rs.getDouble("tongTien");
                data.put(ngay, tongTien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}


