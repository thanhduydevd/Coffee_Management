package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 5/1/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Size;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<OrderDetail> getODByOrderID(String ma) {
        ArrayList<OrderDetail> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT [maCTHoaDon],[maHoaDon],cthd.maSanPham " +
                    ",cthd.kichThuoc ,[soLuong] ,[donGia]  ,[thanhTien] " +
                    ",[ghiChu] ,sp.tenSanPham\n" +
                    "FROM \n" +
                    "    ChiTietHoaDon cthd\n" +
                    "JOIN \n" +
                    "    SanPham sp ON cthd.maSanPham = sp.maSanPham\n" +
                    "WHERE \n" +
                    "    cthd.maHoaDon = ?;";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ma);
            ResultSet re = state.executeQuery();
            while (re.next()) {
                String maCT = re.getString("maCTHoaDon");
                String maHD = re.getString("maHoaDon");
                String maSP = re.getString("maSanPham");
                String kichThuoc = re.getString("kichThuoc");
                int soLuong = re.getInt("soLuong");
                double donGia = re.getDouble("donGia");
                double thanhTien = re.getDouble("thanhTien");
                String ghiChu = re.getString("ghiChu");
                String name = re.getString("tenSanPham");
                Size kt ;
                if(kichThuoc.equals("M")){
                    kt = Size.M;
                }else if(kichThuoc.equals("S")){
                    kt = Size.S;
                }else{
                    kt = Size.L;
                }
                OrderDetail od = new OrderDetail(maCT,maHD,maSP,kt,soLuong,donGia,ghiChu,name);


                list.add(od);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void addODToDB(OrderDetail ct){
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO ChiTietHoaDon (maCTHoaDon, maHoaDon, maSanPham, kichThuoc, soLuong," +
                " donGia, thanhTien, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement psChiTiet = con.prepareStatement(sql);
                psChiTiet.setString(1, ct.getDetailID());
                psChiTiet.setString(2, ct.getOrderID());
                psChiTiet.setString(3, ct.getProductID());
                psChiTiet.setString(4, ct.getSize().getSize());
                psChiTiet.setInt(5, ct.getQuatity());
                psChiTiet.setDouble(6, ct.getPrice());
                psChiTiet.setDouble(7, ct.getTotal());
                psChiTiet.setString(8, ct.getComment());

                psChiTiet.addBatch();
            psChiTiet.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}

