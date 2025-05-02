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

    public Product_DAO() {
    }

    public static ArrayList<Product> loadProductFromDB(){
        ArrayList<Product> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "select * from SanPham WHERE trangThai = 1";
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
                    default -> null;
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

    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM SanPham WHERE trangThai = 1";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet resultSet = state.executeQuery();

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
                    default -> null;
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


    public static boolean addProductToDB(Product product) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int result = 0;
        try {
            // Kiểm tra nếu sản phẩm đã tồn tại và đang ACTIVE (trangThai = 1)
            String sql = "SELECT trangThai FROM SanPham WHERE maSanPham = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, product.getMaSanPham());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int status = rs.getInt("trangThai");
                stmt.close();
                if (status == 1) {
                    return false; // Trùng mã và đang hoạt động
                } else {
                    // Nếu đã bị ẩn (trangThai = 0) → cập nhật lại thay vì insert
                    String updateSQL = "UPDATE SanPham SET tenSanPham=?, maLoaiSanPham=?, kichThuoc=?, giaGoc=?, moTa=?, hinhAnh=?, trangThai=1 WHERE maSanPham=?";
                    stmt = con.prepareStatement(updateSQL);
                    stmt.setString(1, product.getTenSanPham());
                    stmt.setString(2, product.getLoaiSanPham().getMaLoaiSanPham());
                    stmt.setString(3, product.getKichThuoc().toString());
                    stmt.setDouble(4, product.getGiaGoc());
                    stmt.setString(5, product.getMoTa());
                    stmt.setString(6, product.getHinhAnh());
                    stmt.setString(7, product.getMaSanPham());
                    result = stmt.executeUpdate();
                    return result > 0;
                }
            }

            // Nếu chưa có thì INSERT mới
            stmt.close();
            String sqlInsert = "INSERT INTO SanPham (maSanPham, tenSanPham, maLoaiSanPham, kichThuoc, giaGoc, moTa, hinhAnh, trangThai) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sqlInsert);
            stmt.setString(1, product.getMaSanPham());
            stmt.setString(2, product.getTenSanPham());
            stmt.setString(3, product.getLoaiSanPham().getMaLoaiSanPham());
            stmt.setString(4, product.getKichThuoc().toString());
            stmt.setDouble(5, product.getGiaGoc());
            stmt.setString(6, product.getMoTa());
            stmt.setString(7, product.getHinhAnh());
            stmt.setInt(8, product.getTrangThai());
            result = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result > 0;
    }


    public static boolean updateProductToDB(Product product) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int result = 0;
        try {
            String sql = "UPDATE SanPham SET tenSanPham = ?, maLoaiSanPham = ?, kichThuoc = ?, giaGoc = ?, moTa = ?, hinhAnh = ?, trangThai = ? WHERE maSanPham = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, product.getTenSanPham());
            state.setString(2, product.getLoaiSanPham().getMaLoaiSanPham());
            state.setString(3, product.getKichThuoc().toString());
            state.setDouble(4, product.getGiaGoc());
            state.setString(5, product.getMoTa());
            state.setString(6, product.getHinhAnh());
            state.setInt(7, product.getTrangThai());
            state.setString(8, product.getMaSanPham());
            result = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result > 0; // trả về true nếu cập nhật thành công
    }

    public static boolean deleteProductToDB(String maSanPham) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int result = 0;
        try {
            String sql = "UPDATE SanPham SET trangThai = 0 WHERE maSanPham = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maSanPham);
            result = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result > 0;
    }

}


