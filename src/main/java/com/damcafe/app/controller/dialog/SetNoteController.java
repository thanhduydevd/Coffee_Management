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

    private String noteText = ""; // <-- Biến lưu ghi chú

    @FXML
    private TextArea txtNote = new TextArea();

    private String currentNote = "";

    public void setCurrentNote(String note) {
        this.currentNote = note;
        txtNote.setText(note);
    }

    public String getNoteTextString() {
        return txtNote.getText(); // Trả về ghi chú mới
    }

    public void initialize() {
        cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        applyButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(cancelButton);
        dialogPane.getButtonTypes().add(applyButton);

        // Gán sự kiện cho nút "Xác nhận"
        Button applyBtn = (Button) dialogPane.lookupButton(applyButton);
        applyBtn.setOnAction(event -> {
            suKienApply();  // Không cần truyền tham số
        });
    }

    private void suKienApply() {
        noteText = abc.getText();  // Lưu ghi chú
    }

    public String getNoteText() {
        return noteText;
    }
}

