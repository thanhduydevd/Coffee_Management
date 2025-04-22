package edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class CancelOrderController {
    @FXML
    private DialogPane dialogPane;

    public void initialize(){
        ButtonType cancelButton = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Có", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(cancelButton);
        dialogPane.getButtonTypes().add(applyButton);
    }
}
