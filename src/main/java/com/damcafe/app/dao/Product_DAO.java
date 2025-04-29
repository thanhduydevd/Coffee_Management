package com.damcafe.app.dao;/*
 * @ (#) Product_DAO.java   1.0 29/4/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.dao.ProductCategory_DAO;
import com.damcafe.app.entity.Product;
import com.damcafe.app.entity.ProductCategory;
import com.damcafe.app.entity.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description
 * @author: Tran Tuan Hung
 * @date: 29/4/25
 * @version: 1.0
 */
public class Product_DAO {

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


