package com.damcafe.app.controller;

import animatefx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class AuthenticationPageController {
    @FXML
    private Pane mainPane;

    @FXML
    private Button btnLogin, btnRegister;

    @FXML
    private ImageView imgLogo;

    @FXML
    public void initialize(){
        new BounceInDown(mainPane).playOnFinished(new Shake(imgLogo)).play();
        loadPage("/com/damcafe/app/views/authentication/login.fxml");

        btnLogin.setOnAction(event -> {loadPage("/com/damcafe/app/views/authentication/login.fxml");});
        btnRegister.setOnAction(event -> {loadPage("/com/damcafe/app/views/authentication/register.fxml");});
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
        Window window = mainPane.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).setIconified(true);
        }
    }



    public void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node node = loader.load();

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);

            mainPane.getChildren().setAll(node);

            //Chờ 1 khoảng thời gian để node gắn vào screen
            Platform.runLater(() -> new BounceInUp(node).play());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}