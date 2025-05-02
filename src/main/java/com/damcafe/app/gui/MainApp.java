package com.damcafe.app.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/damcafe/app/views/authentication_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        /** Trang đăng ký/đăng nhập chạy 2 lệnh này **/
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMaximized(false);
        /**----------------------------------------**/

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}