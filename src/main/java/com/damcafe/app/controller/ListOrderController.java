package com.damcafe.app.controller;

import com.damcafe.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ListOrderController {
    @FXML
    private Button btnOrderDetail;


    public void initialize(){

        //Sự kiện click cho các button ở chức năng xem đơn hàng
        btnOrderDetail.setOnAction(e-> openDialog("chitietdonhang"));
    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }
}
