package com.damcafe.app.controller;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.NhanVien;
import com.damcafe.app.entity.TaiKhoan;
import com.damcafe.app.entity.UserSession;
import com.damcafe.app.gui.ShowDialog;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text; //

import java.sql.Connection;
import java.sql.SQLException;

public class AccountManagementController {
    private NhanVien_DAO nv_dao;
    private TaiKhoan_DAO tk_dao;
    @FXML
    private Button btnChangePassword;

    @FXML
    private Text txtname, txtaddress, txtphone, txtemail;

    public void initialize() {
        // Sự kiện click cho các button ở chức năng quản lý tài khoản
        btnChangePassword.setOnAction(e -> openDialog("thaydoimatkhau"));
        String name = UserSession.getUsername();
        if (name != null) {
            setUserInfo(name);
        }
    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }

    public void setUserInfo(String name) {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        nv_dao = new NhanVien_DAO();
        tk_dao = new TaiKhoan_DAO();
        TaiKhoan taiKhoan = tk_dao.getTaiKhoanTheoTen(name);
        NhanVien nhanVien = nv_dao.getNhanVien(taiKhoan.getMaNhanVien().getMaNhanVien());
        txtname.setText(nhanVien.getTenNhanVien());
        txtaddress.setText(nhanVien.getDiaChi());
        txtphone.setText(nhanVien.getSdt());
        txtemail.setText(nhanVien.getEmail());
    }
}
