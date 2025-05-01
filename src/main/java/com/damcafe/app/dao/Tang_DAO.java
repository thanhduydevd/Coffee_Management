package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 5/1/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.Tang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Tang_DAO {
    public static ArrayList<Tang> loadTangFromDB(){
        ArrayList<Tang> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "select * from Tang";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet re = state.executeQuery();

            while(re.next()){
                String ma = re.getString("maTang");
                String ten = re.getString("tenTang");
                Tang a = new Tang(ma,ten);
                list.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
