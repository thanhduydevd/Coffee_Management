package com.damcafe.app.controller;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.NhanVien;
import com.damcafe.app.entity.TaiKhoan;
import com.damcafe.app.gui.MainApp;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public class RegisterController {
    private TaiKhoan_DAO tk_dao;
    private NhanVien_DAO nv_dao;

    @FXML
    private Label notification_register;
    @FXML
    private TextField txtid_register,txtname_register, txtpass_register, txtpassreset_register;
    @FXML
    private Button btn_register;
    @FXML
    private void initialize() {

        btn_register.setOnAction(e -> {
            if (checkregister() && checkTaiKhoan()) {
                notification_register.setText("Đăng ký thành công");
                notification_register.setStyle("-fx-text-fill: green;");
                try {
                    // Tạo 1 Stage mới
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/damcafe/app/views/authentication_page.fxml"));
                    Parent root = fxmlLoader.load();

                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.initStyle(StageStyle.UNDECORATED);
                    newStage.setMaximized(false);
                    newStage.show();


                    Stage oldStage = (Stage) btn_register.getScene().getWindow();
                    oldStage.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        txtid_register.setOnKeyReleased(e -> {
            checkregister();
        });
        txtname_register.setOnKeyReleased(e -> {
            checkregister();
        });
        txtpass_register.setOnKeyReleased(e -> {
            checkregister();
        });
        txtpassreset_register.setOnKeyReleased(e -> {
            checkregister();
        });
    }
    private boolean checkregister() {
        String id = txtid_register.getText();
        String name = txtname_register.getText();
        String pass = txtpass_register.getText();
        String pass_confirm = txtpassreset_register.getText();

        if (id.isEmpty()) {
            notification_register.setText("Mã nhân viên không được để trống");
            notification_register.setStyle("-fx-text-fill: red;");
            return false;
        }else if(!id.matches("^NV[0-9]{3}$")) {
            notification_register.setText("Mã nhân viên không hợp lệ (NVxxx)");
            notification_register.setStyle("-fx-text-fill: red;");
            if (!txtid_register.isFocused()) {
                txtid_register.requestFocus();
            }
            return false;
        }
        if (name.isEmpty()) {
            notification_register.setText("Tên nhân viên không được để trống");
            notification_register.setStyle("-fx-text-fill: red;");
            return false;
        } else if (!name.matches("^(?=.*[A-Z])(?=.*\\d)[^\\s]{8,}$")) {
            notification_register.setText("Tên nhân viên không hợp lệ (ít nhất 8 ký tự, có chữ hoa và số)");
            notification_register.setStyle("-fx-text-fill: red;");
            if (!txtname_register.isFocused()) {
                txtname_register.requestFocus();
            }
            return false;
        }
        if (pass.isEmpty()) {
            notification_register.setText("Mật khẩu không được để trống");
            notification_register.setStyle("-fx-text-fill: red;");
            return false;
        } else if (!pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w])[^\\s]{8,}$")) {
            notification_register.setText("Mật khẩu không hợp lệ (ít nhất 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt)");
            notification_register.setStyle("-fx-text-fill: red;");
            if (!txtpass_register.isFocused()) {
                txtpass_register.requestFocus();
            }
            return false;
        }
        if (pass_confirm.isEmpty()) {
            notification_register.setText("Xác nhận mật khẩu không được để trống");
            notification_register.setStyle("-fx-text-fill: red;");
            return false;
        } else if (!pass_confirm.equals(pass)) {
            notification_register.setText("Mật khẩu xác nhận không khớp");
            notification_register.setStyle("-fx-text-fill: red;");
            if (!txtpassreset_register.isFocused()) {
                txtpassreset_register.requestFocus();
            }
            return false;
        }
        notification_register.setText("");
        return true;
    }

    private boolean checkTaiKhoan() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tk_dao = new TaiKhoan_DAO();
        nv_dao = new NhanVien_DAO();
        String id = txtid_register.getText();
        String name = txtname_register.getText();
        String pass = txtpass_register.getText();
        if (nv_dao.getNhanVien(id) == null) {
            notification_register.setText("Nhan viên không tồn tại");
            notification_register.setStyle("-fx-text-fill: red;");
            if (!txtid_register.isFocused()) {
                txtid_register.requestFocus();
            }
            return false;
        }
        if (tk_dao.getTaiKhoan(id) != null) {
            notification_register.setText("Nhân viên đã có tài khoản");
            notification_register.setStyle("-fx-text-fill: red;");
            if (!txtid_register.isFocused()) {
                txtid_register.requestFocus();
            }
            return false;
        }
        if (tk_dao.getTaiKhoanTheoTen(name) != null) {
            notification_register.setText("Tên tài khoản đã tồn tại");
            notification_register.setStyle("-fx-text-fill: red;");
            if (!txtname_register.isFocused()) {
                txtname_register.requestFocus();
            }
            return false;
        }
        Random rand = new Random();
        int randomNumber = rand.nextInt(900) + 100;
        NhanVien nhanVien = nv_dao.getNhanVien(id);
        TaiKhoan taiKhoan = new TaiKhoan(id + randomNumber, nhanVien, name, pass);
        tk_dao.addTaiKhoan(taiKhoan);
        return true;
    }
}
