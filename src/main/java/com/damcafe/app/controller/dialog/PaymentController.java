package com.damcafe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class PaymentController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private Text tienThua, thanhTien;

    @FXML
    private TextField khachDua;

    private double traKhach = 0;

    public void initialize(){
        ButtonType cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Thanh toán", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(cancelButton);
        dialogPane.getButtonTypes().add(applyButton);

        khachDua.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER){
                tinhTienThua();
            }
        });

    }

    private void tinhTienThua() {
        try {
            double a = Double.parseDouble(khachDua.getText().replace(",", "").replace("đ", "").trim());
            double b = Double.parseDouble(thanhTien.getText().replace(",", "").replace("đ", "").trim());
            tienThua.setText(String.format("%sđ",b - a));
            traKhach = b-a;
        } catch (NumberFormatException e) {
            tienThua.setText("0đ");
            traKhach =0;
        }
    }


}
