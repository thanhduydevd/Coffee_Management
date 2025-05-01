package com.damcafe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SetNoteController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextArea abc;

    @FXML
    private ButtonType cancelButton, applyButton;

    public void initialize(){
        cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        applyButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(cancelButton);
        dialogPane.getButtonTypes().add(applyButton);

        // Gán sự kiện cho nút "Xác nhận"
        Button applyBtn = (Button) dialogPane.lookupButton(applyButton);
        applyBtn.setOnAction(event -> {
            suKienApply();
        });
    }

    public void suKienApply(){
        System.out.println(abc.getText());
    }
}
