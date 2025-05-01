package com.damcafe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class SetQuantityController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField txtSoLuong;

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().addAll(cancelButton, applyButton);
    }

    public int getQuantity() {
        try {
            return Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            return 1; // Mặc định nếu không hợp lệ
        }
    }
}

