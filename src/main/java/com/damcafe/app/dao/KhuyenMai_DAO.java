/*
 * @ (#) KhuyenMai_DAO.java       1.0 04/05/2025
 *
 * Copyright (c) 2025 IUH All rights reserved.
 */
package com.damcafe.app.dao;


import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.KhuyenMai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description: This class is used to ...
 * @author: Thanh Duy
 * @date:   04/05/2025
 * @version:  1.0
 */
public class KhuyenMai_DAO {
    public static ArrayList<KhuyenMai> getKhuyenMai(){
            ArrayList<KhuyenMai> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        String sql = "select * from KhuyenMai";
        try {
            PreparedStatement ptsm = con.prepareStatement(sql);
            ResultSet rs = ptsm.executeQuery();
            while (rs.next()){
                String maKhuyenMai = rs.getString("maKhuyenMai");
                double tiGia = rs.getDouble("discount");

                KhuyenMai km = new KhuyenMai(maKhuyenMai,tiGia);
                list.add(km);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
