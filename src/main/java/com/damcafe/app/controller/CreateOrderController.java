package com.damcafe.app.controller;

import com.damcafe.app.gui.DialogFunction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CreateOrderController {
    @FXML
    private Button btnQuantity, btnNote, btnCancel, btnPayment;


    public void initialize(){

        //Sự kiện click cho các button ở chức năng tạo đơn hàng
        btnQuantity.setOnAction(e -> openDialog("dieuchinhsoluong"));
        btnNote.setOnAction(e -> openDialog("thietlapghichu"));
        btnCancel.setOnAction(e -> openDialog("huymon"));
        btnPayment.setOnAction(e-> openDialog("thanhtoan"));
    }

    public void openDialog(String chucNang) {
        DialogFunction dialog = new DialogFunction(chucNang);
        dialog.showAndWait();
    }
}
