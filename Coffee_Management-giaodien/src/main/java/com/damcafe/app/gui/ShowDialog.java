package com.damcafe.app.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class ShowDialog extends Dialog {
    private String mainFunction;

    public ShowDialog(String mainFunction){
        this.mainFunction = mainFunction;
        setHeaderText(null);
        setGraphic(null);

        try {
            String fxmlPath = getFxmlPath(mainFunction);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            DialogPane dialogPane = loader.load();
            setDialogPane(dialogPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFxmlPath(String mainFunction){
        return switch (mainFunction.toLowerCase()){
            case "dieuchinhsoluong" -> "/com/damcafe/app/views/order/dialogs/set_quantity.fxml";
            case "thietlapghichu" -> "/com/damcafe/app/views/order/dialogs/set_note.fxml";
            case "huymon" -> "/com/damcafe/app/views/order/dialogs/cancel_order.fxml";
            case "thanhtoan" -> "/com/damcafe/app/views/order/dialogs/payment.fxml";
            case "chitietdonhang" -> "/com/damcafe/app/views/order/dialogs/order_detail.fxml";
            case "thaydoimatkhau" -> "/com/damcafe/app/views/account/dialogs/change_password.fxml";
            default -> throw new IllegalArgumentException("Chức năng không tồn tại: " + mainFunction);
        };
    }

}
