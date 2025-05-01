package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 5/1/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Size;

import java.sql.*;
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


}
