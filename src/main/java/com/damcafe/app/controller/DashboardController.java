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
import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.dao.NhanVien_DAO;
import com.damcafe.app.dao.TaiKhoan_DAO;
import com.damcafe.app.entity.NhanVien;
import com.damcafe.app.entity.TaiKhoan;
import com.damcafe.app.entity.UserSession;
import com.damcafe.app.gui.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * @description: This class is used to ...
 * @author: Thanh Duy
 * @date:   03/03/2025
 * @version:  1.0
 */
public class DashboardController {
    private NhanVien_DAO nv_dao;
    private TaiKhoan_DAO tk_dao;
    @FXML
    private VBox subMenuDonHang;

    @FXML AnchorPane paneNoiDung;

    @FXML
    private Button close_btn;

    @FXML
    private Text lb_name;

    public void initialize(){
        // Sub menu
        subMenuDonHang.setVisible(false);
        subMenuDonHang.setManaged(false);

        loadPage("/com/damcafe/app/views/main/main_page.fxml");
        close_btn.setOnAction(e -> closeApp());

        String name = UserSession.getUsername();
        if (name != null) {
            setLb_name(name);
        }
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
    public void closeApp() {
        UserSession.setUsername(null);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/damcafe/app/views/authentication_page.fxml"));
            Parent root = fxmlLoader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.setMaximized(false);
            newStage.show();

            Stage oldStage = (Stage) close_btn.getScene().getWindow();
            oldStage.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void setLb_name(String name) {
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        nv_dao = new NhanVien_DAO();
        tk_dao = new TaiKhoan_DAO();
        TaiKhoan taiKhoan = tk_dao.getTaiKhoanTheoTen(name);
        NhanVien nhanVien = nv_dao.getNhanVien(taiKhoan.getMaNhanVien().getMaNhanVien());
        String[] arr = nhanVien.getTenNhanVien().split(" ");
        String strname = arr[arr.length - 2] + " " + arr[arr.length - 1];
        String str = "Xin chào, " + strname + "!";
        lb_name.setText(str);
    }
}
