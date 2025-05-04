package com.damcafe.app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SetNoteController {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextArea txtNote;

    private ButtonType cancelButton, applyButton;

    private String currentNote = "";

    public void setCurrentNote(String note) {
        this.currentNote = note;
        if (note.isBlank() || note == null) {
            txtNote.setText("");
        }else{
            txtNote.setText(note);
        }

    }

    public String getNoteTextString() {
        return txtNote.getText();
    }

    public void initialize() {
        cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        applyButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.APPLY);
        txtNote.setText(currentNote);
        dialogPane.getButtonTypes().addAll(cancelButton, applyButton);

        Button applyBtn = (Button) dialogPane.lookupButton(applyButton);
        applyBtn.setOnAction(event -> {
            suKienApply();
        });
    }

    private void suKienApply() {
        currentNote = txtNote.getText();
    }

    public String getNoteText() {
        return currentNote;
    }
}
