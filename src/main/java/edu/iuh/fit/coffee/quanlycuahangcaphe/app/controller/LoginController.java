package edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller;

import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class LoginController {
    @FXML
    private Pane pRegister;
    @FXML
    private Pane pLogin;
    @FXML
    private ImageView imgLogo;

    @FXML
    public void initialize(){
        new BounceInDown(pLogin).playOnFinished(new Shake(imgLogo)).play();
    }

    @FXML
    protected void onRegisterNow() {
        pLogin.setVisible(false);
        new BounceInUp(pRegister).play();
        pRegister.setVisible(true);
    }

    @FXML
    protected void onLoginNow() {
        pRegister.setVisible(false);
        new BounceInDown(pLogin).play();
        pLogin.setVisible(true);
    }

    @FXML
    protected void onExit() {
        Alert alertExit = new Alert(Alert.AlertType.CONFIRMATION);
        alertExit.setTitle("Xác nhận thoát");
        alertExit.setContentText("Bạn có chắc chắn muốn thoát?");
        alertExit.setHeaderText(null);
        alertExit.getButtonTypes().setAll(ButtonType.YES,ButtonType.NO);

        Optional<ButtonType> result = alertExit.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES)
            System.exit(0);
    }

    @FXML
    protected void onHide() {
        Window window = pLogin.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).setIconified(true);
        }
    }
}