package com.damcafe.app.controller;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateOrderController {
    @FXML
    private Button btnQuantity, btnNote, btnCancel, btnPayment;



    public void initialize(){

//        try{
//            ConnectDB.getInstance().connect();
//            Connection con = ConnectDB.getConnection();
//        }catch(SQLException e) {
//            throw new RuntimeException(e);
//        }

        //Sự kiện click cho các button ở chức năng tạo đơn hàng
        btnQuantity.setOnAction(e -> openDialog("dieuchinhsoluong"));
        btnNote.setOnAction(e -> openDialog("thietlapghichu"));
        btnCancel.setOnAction(e -> openDialog("huymon"));
        btnPayment.setOnAction(e-> openDialog("thanhtoan"));
    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }


}
