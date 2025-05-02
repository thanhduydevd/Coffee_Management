package com.damcafe.app.dao;

import com.damcafe.app.connectDB.ConnectDB;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrangChinh_DAO {

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

    /** Thống kê doanh thu theo loại đồ uống **/
    public static List<PieChart.Data> getQuantityByCategory() {
        List<PieChart.Data> list = new ArrayList<>();
        String sql = "SELECT LoaiSanPham.tenLoaiSanPham, SUM(ChiTietHoaDon.soLuong) AS tongSoLuong " +
                "FROM ChiTietHoaDon " +
                "JOIN SanPham ON ChiTietHoaDon.maSanPham = SanPham.maSanPham " +
                "JOIN LoaiSanPham ON SanPham.maLoaiSanPham = LoaiSanPham.maLoaiSanPham " +
                "GROUP BY LoaiSanPham.tenLoaiSanPham";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String tenLoai = rs.getString("tenLoaiSanPham");
                double soLuong = rs.getDouble("tongSoLuong");
                list.add(new PieChart.Data(tenLoai, soLuong));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
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
