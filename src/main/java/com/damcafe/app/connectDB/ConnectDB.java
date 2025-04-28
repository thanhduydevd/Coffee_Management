package com.damcafe.app.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectDB {
    public static Connection con = null;
    private static ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }
    public Connection connect() throws SQLException {
        String url = "jdbc:sqlserver://35.238.175.153:1433;databasename=QuanCafe";
        String user = "sqlserver";
        String password = "admin";
        con = DriverManager.getConnection(url, user, password);
        return con;
    }
    public void disconnect() {
        if (con != null)
            try {
                con.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public static Connection getConnection() {
        return con;
    }
}
