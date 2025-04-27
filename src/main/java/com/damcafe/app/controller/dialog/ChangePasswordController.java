package com.damcafe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class ChangePasswordController {
    @FXML
    private DialogPane dialogPane;

    public void initialize(){
        ButtonType cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Thay đổi", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(cancelButton);
        dialogPane.getButtonTypes().add(applyButton);
    }
}
