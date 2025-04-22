package edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class SetNoteController {
    @FXML
    private DialogPane dialogPane;

    public void initialize(){
        ButtonType cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(cancelButton);
        dialogPane.getButtonTypes().add(applyButton);
    }
}
