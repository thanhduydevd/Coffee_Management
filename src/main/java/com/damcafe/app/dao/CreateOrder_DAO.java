package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 4/29/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateOrder_DAO {
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
}
