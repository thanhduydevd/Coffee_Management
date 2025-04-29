package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 4/29/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.Product;
import com.damcafe.app.entity.ProductCategory;
import com.damcafe.app.entity.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreateOrder_DAO {

    static ArrayList<ProductCategory> dsLoaiSP = ProductCategory_DAO.getAllProductCategories();

    public static int getMaxHash(){
        int hash = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT MAX(CAST(RIGHT([maCTHoaDon], 3) AS INT)) AS SoLonNhat " +
                    "FROM ChiTietHoaDon;";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet result = state.executeQuery();
            if (result.next()){
                hash = result.getInt("SoLonNhat");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }

    public static ArrayList<Product> loadProductFromDB(){
        ArrayList<Product> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "select * from SanPham";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet resultSet = state.executeQuery();

            // Duyệt qua kết quả và tạo danh sách sản phẩm
            while (resultSet.next()) {
                String id = resultSet.getString("maSanPham");
                String name = resultSet.getString("tenSanPham");
                String loaiSanPham = resultSet.getString("maLoaiSanPham");
                String kichThuoc = resultSet.getString("kichThuoc");
                String moTa = resultSet.getString("moTa");
                int trangThai = resultSet.getInt("trangThai");
                double price = resultSet.getDouble("giaGoc");
                String imagePath = resultSet.getString("hinhAnh");

                Size size = switch (kichThuoc) {
                    case "L" -> Size.L;
                    case "M" -> Size.M;
                    case "S" -> Size.S;
                    default -> Size.NONE;
                };
                for (ProductCategory loai : dsLoaiSP){
                    if (loaiSanPham.equals(loai.getMaLoaiSanPham())){
                        Product product = new Product(id,name,loai,size,price,moTa,imagePath,trangThai);
                        list.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
