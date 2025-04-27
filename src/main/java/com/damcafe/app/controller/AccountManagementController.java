package com.damcafe.app.controller;

import com.damcafe.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AccountManagementController {
    @FXML
    private Button btnChangePassword;


    public void initialize(){

        //Sự kiện click cho các button ở chức năng quản lý tài khoản
        btnChangePassword.setOnAction(e -> openDialog("thaydoimatkhau"));
    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }
}
