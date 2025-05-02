package com.damcafe.app.controller.dialog;

import com.damcafe.app.controller.CreateOrderController;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class PaymentController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField txtKhachDua;
    @FXML
    private Text txtTotal,txtTra;

    public void initialize() {
        dialogPane.getButtonTypes().clear(); // Xoá nút mặc định nếu có

        ButtonType cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Thanh toán", ButtonBar.ButtonData.APPLY);

        txtTotal.setText(String.valueOf(CreateOrderController.tongTienUI));
        dialogPane.getButtonTypes().addAll(cancelButton, applyButton);

        txtKhachDua.setOnKeyReleased(e -> xuLiTraTien());
    }

    private void xuLiTraTien() {
        try {
            double tongTien = Double.parseDouble(txtTotal.getText());
            double khachDua = Double.parseDouble(txtKhachDua.getText());

            if (khachDua < tongTien) {
                txtTra.setText("Chưa đủ tiền");
            } else {
                double tienTra = khachDua - tongTien;
                txtTra.setText(String.format("Trả lại: %.0f đ", tienTra));
            }
        } catch (NumberFormatException e) {
            txtTra.setText("Vui lòng nhập số hợp lệ");
        }
    }
}
