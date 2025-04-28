package com.damcafe.app.controller;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.UserSession;
import com.damcafe.app.gui.MainApp;
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

public class LoginController{
    private TaiKhoan_DAO tk_dao;

    @FXML
    private Label notification_login;

    @FXML
    private TextField txtname_login, txtpass_login;
    @FXML
    private Button btn_login;

    @FXML
    private void initialize(){

        btn_login.setOnAction(e -> {
            if (checklogin()){
                if (checktnhanvien()){
                    notification_login.setText("Đăng nhập thành công");
                    notification_login.setStyle("-fx-text-fill: green;");
                    UserSession.setUsername(txtname_login.getText());
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/damcafe/app/views/dashboard.fxml"));
                        Parent root = fxmlLoader.load();

                        Stage newStage = new Stage();
                        newStage.setTitle("");
                        newStage.setScene(new Scene(root));
                        newStage.setMaximized(true);
                        newStage.initStyle(StageStyle.DECORATED);
                        newStage.show();


                        Stage oldStage = (Stage) btn_login.getScene().getWindow();
                        oldStage.close();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        txtname_login.setOnKeyReleased(e -> {
            checklogin();
        });
        txtpass_login.setOnKeyReleased(e -> {
            checklogin();
        });
    }

    private boolean checklogin(){
        // Kiểm tra tên đăng nhập
        if (txtname_login.getText().isEmpty()){
            notification_login.setText("Vui lòng nhập tên đăng nhập");
            notification_login.setStyle("-fx-text-fill: red;");
            return false;
        } else {
            if (!txtname_login.getText().matches("^(?=.*[A-Z])(?=.*\\d)[^\\s]{8,}$")){
                notification_login.setText("Tên đăng nhập phải chứa ít nhất 1 chữ cái viết hoa, 1 số và 8 ký tự");
                notification_login.setStyle("-fx-text-fill: red;");
                if (!txtname_login.isFocused()) {
                    txtname_login.requestFocus();
                }
                return false;
            }
        }

        // Kiểm tra mật khẩu
        if (txtpass_login.getText().isEmpty()) {
            notification_login.setText("Vui lòng nhập mật khẩu");
            notification_login.setStyle("-fx-text-fill: red;");
            return false;
        } else {
            if (!txtpass_login.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w])[^\\s]{8,}$")){
                notification_login.setText("Mật khẩu phải có 8 ký tự, 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt.");
                notification_login.setStyle("-fx-text-fill: red;");
                if (!txtpass_login.isFocused()) {
                    txtpass_login.requestFocus();
                }
                return false;
            }
        }


        notification_login.setText("");
        return true;
    }

    private boolean checktnhanvien(){
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tk_dao = new TaiKhoan_DAO();
        String id = txtname_login.getText();
        String pass = txtpass_login.getText();
        if (tk_dao.getTaiKhoanTheoTen(id) == null){
            notification_login.setText("Tên đăng nhập không tồn tại");
            notification_login.setStyle("-fx-text-fill: red;");
            return false;
        }else if (!tk_dao.getTaiKhoanTheoTen(id).getMatKhau().equals(pass)) {
            notification_login.setText("Mật khẩu không đúng");
            notification_login.setStyle("-fx-text-fill: red;");
            return false;
        }
        return true;
    }

}
