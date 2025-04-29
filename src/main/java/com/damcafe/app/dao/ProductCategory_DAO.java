/*
 * @ (#) ProductCategory_DAO.java   1.0 28/4/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.damcafe.app.dao;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.ProductCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description
 * @author: Tran Tuan Hung
 * @date: 28/4/25
 * @version: 1.0
 */
public class ProductCategory_DAO {
    public static ArrayList<ProductCategory> getAllProductCategories() {
        ArrayList<ProductCategory> productCategories = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSanPham";
        try {
            // Kết nối đến cơ sở dữ liệu
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                ProductCategory productCategory = new ProductCategory(rs.getString("maLoaiSanPham"), rs.getString("tenLoaiSanPham"));
                productCategories.add(productCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productCategories;
    }
}
