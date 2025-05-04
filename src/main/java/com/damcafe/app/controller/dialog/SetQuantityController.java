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

    private int currentQuantity = 1;

    public void setCurrentQuantity(int quantity) {
        this.currentQuantity = quantity;
        txtSoLuong.setText(String.valueOf(quantity));
    }

    public int getQuantity() {
        try {
            return Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            return currentQuantity;
        }
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.APPLY);

        txtSoLuong.setText(String.valueOf(currentQuantity));
        dialogPane.getButtonTypes().addAll(cancelButton, applyButton);

        dialogPane.lookupButton(applyButton).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!isValidInput()) {
                event.consume();
                ShowDialog.showMessageDialog(dialogPane, "Vui lòng nhập số nguyên dương hợp lệ!");
            }
        });
    }

    private boolean isValidInput() {
        String input = txtSoLuong.getText().trim();
        if (input.isEmpty()) {
            txtSoLuong.setText("1");
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
