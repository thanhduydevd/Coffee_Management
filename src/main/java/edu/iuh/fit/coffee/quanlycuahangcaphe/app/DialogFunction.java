package edu.iuh.fit.coffee.quanlycuahangcaphe.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class DialogFunction extends Dialog {
    private String mainFunction;

    public DialogFunction(String mainFunction){
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
            case "dieuchinhsoluong" -> "/edu/iuh/fit/coffee/quanlycuahangcaphe/views/order_management/dialogs/set_quantity.fxml";
            case "thietlapghichu" -> "/edu/iuh/fit/coffee/quanlycuahangcaphe/views/order_management/dialogs/set_note.fxml";
            case "huymon" -> "/edu/iuh/fit/coffee/quanlycuahangcaphe/views/order_management/dialogs/cancel_order.fxml";
            default -> throw new IllegalArgumentException("Chức năng không tồn tại: " + mainFunction);
        };
    }

}
