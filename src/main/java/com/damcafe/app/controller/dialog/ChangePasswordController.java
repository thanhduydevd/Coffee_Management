package com.damcafe.app.controller.dialog;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.entity.UserSession;
import dao.TaiKhoan_DAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ChangePasswordController {
    private TaiKhoan_DAO tk_dao;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private PasswordField currentpassword, newpassword, confirmpassword;

    public void initialize() {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tk_dao = new TaiKhoan_DAO();

        ButtonType cancelButton = new ButtonType("Huỷ bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType applyButton = new ButtonType("Thay đổi", ButtonBar.ButtonData.APPLY);

        dialogPane.getButtonTypes().add(cancelButton);
        dialogPane.getButtonTypes().add(applyButton);

        String name = UserSession.getUsername();

        Button applyBtn = (Button) dialogPane.lookupButton(applyButton);
        applyBtn.addEventFilter(ActionEvent.ACTION, event -> {
            boolean isValid = checkpassword(name);

            if (!isValid) {
                event.consume();
            }else {
                System.out.println(name);
                System.out.println(newpassword.getText());
                if (tk_dao.updatePass(newpassword.getText(), name)){
                    showAlert(Alert.AlertType.INFORMATION, "Thay đổi mật khẩu thành công.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Thay đổi mật khẩu không thành công.");
                }
            }

        });
    }

    public boolean checkpassword(String name) {
        String currentPass = currentpassword.getText();
        String newPass = newpassword.getText();
        String confirmPass = confirmpassword.getText();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng nhập đầy đủ tất cả các trường.");
            return false;
        }

        if (!tk_dao.getTaiKhoanTheoTen(name).getMatKhau().equals(currentPass)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu hiện tại không chính xác.");
            return false;
        }

        if (!newPass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w])[^\\s]{8,}$")) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu mới không hợp lệ (ít nhất 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt).");
            return false;
        }

        if (!newPass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu xác nhận không khớp với mật khẩu mới.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
