package com.damcafe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class OrderDetailController {
    @FXML
    private DialogPane dialogPane;

    public void initialize(){
        ButtonType applyButton = new ButtonType("Kết thúc", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(applyButton);
    }
}
