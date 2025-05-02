package com.damcafe.app.controller.dialog;

import com.damcafe.app.gui.ShowDialog;
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
        // Lấy nút "Xác nhận" sau khi dialog được render
        dialogPane.lookupButton(applyButton).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!isValidInput()) {
                event.consume(); // Ngăn không cho đóng dialog
                ShowDialog.showMessageDialog(dialogPane,"Vui lòng nhập số nguyên dương hợp lệ!");
            }
        });
    }

    public int getQuantity() {
        try {

            return Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            return 1; // Mặc định nếu không hợp lệ
        }
    }
    private boolean isValidInput() {
        String input = txtSoLuong.getText().trim();
        if (input.isEmpty()) {
            txtSoLuong.setText("1"); // Gán mặc định nếu để trống
            return true;
        }
        try {
            int value = Integer.parseInt(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

