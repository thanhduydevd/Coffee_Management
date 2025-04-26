/*
 * @ (#) DashboardController.java       1.0 03/03/2025
 *
 * Copyright (c) 2025 IUH All rights reserved.
 */
package com.damcafe.app.controller;


import animatefx.animation.BounceInDown;
import animatefx.animation.BounceInRight;
import animatefx.animation.BounceInUp;
import animatefx.animation.SlideInRight;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/*
 * @description: This class is used to ...
 * @author: Thanh Duy
 * @date:   03/03/2025
 * @version:  1.0
 */
public class DashboardController {
    @FXML
    private VBox subMenuDonHang;

    @FXML AnchorPane paneNoiDung;

    public void initialize(){
        // Sub menu
        subMenuDonHang.setVisible(false);
        subMenuDonHang.setManaged(false);

        loadPage("/com/damcafe/app/views/main/main_page.fxml");
    }

    @FXML
    protected void toggleSubMenuDonHang() {
        boolean isVisible = subMenuDonHang.isVisible();
        new SlideInRight(subMenuDonHang).play();
        subMenuDonHang.setVisible(!isVisible);
        subMenuDonHang.setManaged(!isVisible);
    }


    @FXML
    protected void trangChinhClick(){
        loadPage("/com/damcafe/app/views/main/main_page.fxml");
    }

    @FXML
    protected void themDonHangClick(){
        loadPage("/com/damcafe/app/views/order/create_order.fxml");
    }

    @FXML
    protected void danhSachDonHangClick(){
        loadPage("/com/damcafe/app/views/order/list_order.fxml");
    }

    @FXML
    protected void quanLySanPhamClick(){
        loadPage("/com/damcafe/app/views/product/product_management.fxml");
    }

    @FXML
    protected void quanLyTaiKhoanClick(){
        loadPage("/com/damcafe/app/views/account/account_management.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node node = loader.load();

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);

            paneNoiDung.getChildren().setAll(node);

            //Chờ 1 khoảng thời gian để node gắn vào screen
            Platform.runLater(() -> new BounceInDown(node).play());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
