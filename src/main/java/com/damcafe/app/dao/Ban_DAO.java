package com.damcafe.app.dao;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 5/1/2025
 * @version: 1.0
 */

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.Ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.damcafe.app.entity.Tang;

public class Ban_DAO {
    public static ArrayList<Ban> loadBanFromDB(){
        ArrayList<Ban> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "select * from Ban";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet re = state.executeQuery();

            while(re.next()){
                String ma = re.getString("maBan");
                boolean trangThai = re.getBoolean("trangThai");
                String maTang = re.getString("maTang");
                String maOrder = re.getString("maHoaDon");
                for (Tang a : Tang_DAO.loadTangFromDB() ){
                    if (maTang.equals(a.getMaTang())){
                        list.add(new Ban(ma,trangThai,a,maOrder));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
