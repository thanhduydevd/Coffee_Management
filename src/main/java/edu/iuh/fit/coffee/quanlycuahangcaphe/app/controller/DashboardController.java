/*
 * @ (#) DashboardController.java       1.0 03/03/2025
 *
 * Copyright (c) 2025 IUH All rights reserved.
 */
package edu.iuh.fit.coffee.quanlycuahangcaphe.app.controller;


import animatefx.animation.SlideInRight;
import edu.iuh.fit.coffee.quanlycuahangcaphe.app.DialogFunction;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Random;

/*
 * @description: This class is used to ...
 * @author: Thanh Duy
 * @date:   03/03/2025
 * @version:  1.0
 */
public class DashboardController {
    @FXML
    private VBox subMenuDonHang, subMenuSanPham;

    @FXML
    private PieChart drinkChart;

    @FXML
    private BarChart revenueChart;

    @FXML
    private CategoryAxis dayAxis;

    @FXML
    private NumberAxis revenueAxis;

    @FXML AnchorPane paneNoiDung, paneTrangChinh, paneThemDonHang;

    @FXML
    private Button btnQuantity, btnNote, btnCancel;

    @FXML
    private TableView<ObservableList<Object>> tableDonHang;
    @FXML
    private TableColumn<ObservableList<Object>, Number> colSTT;
    @FXML
    private TableColumn<ObservableList<Object>, String> colTenMon;
    @FXML
    private TableColumn<ObservableList<Object>, Number> colSoLuong;
    @FXML
    private TableColumn<ObservableList<Object>, Number> colDonGia;
    @FXML
    private TableColumn<ObservableList<Object>, Number> colThanhTien;

    public void initialize(){
        // Sub menu
        subMenuDonHang.setVisible(false);
        subMenuDonHang.setManaged(false);

        subMenuSanPham.setVisible(false);
        subMenuSanPham.setManaged(false);

        // Dữ liệu cho biểu đồ tròn
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Cà phê sữa đá", 30),
                new PieChart.Data("Espresso", 25),
                new PieChart.Data("Trà sữa matcha", 20),
                new PieChart.Data("Sinh tố bơ", 15),
                new PieChart.Data("Nước ép cam", 10)
        );

        // Gán dữ liệu vào PieChart
        drinkChart.setData(pieChartData);

        // Thêm hiệu ứng khi rê chuột vào từng phần
        for (PieChart.Data data : drinkChart.getData()) {
            data.getNode().setOnMouseEntered(event -> {
                drinkChart.setTitle(data.getName() + ": " + data.getPieValue() + "%");
            });
        }

        // Xác định số ngày trong tháng hiện tại
        int daysInMonth = LocalDate.now().lengthOfMonth();

        // Danh sách ngày trong tháng
        dayAxis.setCategories(FXCollections.observableArrayList());
        for (int i = 1; i <= daysInMonth; i++) {
            dayAxis.getCategories().add("Ngày " + i);
        }

        // Tạo series dữ liệu doanh thu
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Doanh thu tháng " + LocalDate.now().getMonthValue());

        // Sinh dữ liệu mẫu: Doanh thu từ 50 - 200 triệu mỗi ngày
        Random rand = new Random();
        for (int i = 1; i <= daysInMonth; i++) {
            int revenue = rand.nextInt(150) + 50; // Random từ 50 đến 200 triệu
            revenueSeries.getData().add(new XYChart.Data<>("Ngày " + i, revenue));
        }

        // Thêm dữ liệu vào BarChart
        revenueChart.getData().add(revenueSeries);

        // Cấu hình TableColumn
        colSTT.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue().get(0)));
        colTenMon.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get(1)));
        colSoLuong.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue().get(2)));
        colDonGia.setCellValueFactory(data -> new SimpleDoubleProperty((Double) data.getValue().get(3)));
        colThanhTien.setCellValueFactory(data -> new SimpleDoubleProperty((Double) data.getValue().get(4)));

        // Tạo dữ liệu giả
        ObservableList<ObservableList<Object>> fakeData = FXCollections.observableArrayList();
        String[] danhSachMon = {"Cà phê sữa đá", "Espresso", "Trà sữa matcha", "Sinh tố bơ", "Nước ép cam"};


        for (int i = 1; i <= 50; i++) { // Tạo 10 dòng dữ liệu
            String tenMon = danhSachMon[rand.nextInt(danhSachMon.length)];
            int soLuong = rand.nextInt(5) + 1; // 1 - 5 ly
            double donGia = rand.nextDouble() * 50 + 20; // Giá từ 20k - 70k
            double thanhTien = soLuong * donGia;

            ObservableList<Object> row = FXCollections.observableArrayList(i, tenMon, soLuong, donGia, thanhTien);
            fakeData.add(row);
        }

        // Thêm dữ liệu vào TableView
        tableDonHang.setItems(fakeData);


        //Sự kiện click cho các button ở chức năng tạo đơn hàng
        btnQuantity.setOnAction(e -> openDialog("dieuchinhsoluong"));
        btnNote.setOnAction(e -> openDialog("thietlapghichu"));
        btnCancel.setOnAction(e -> openDialog("huymon"));
    }

    @FXML
    protected void toggleSubMenuDonHang() {
        boolean isVisible = subMenuDonHang.isVisible();
        new SlideInRight(subMenuDonHang).play();
        subMenuDonHang.setVisible(!isVisible);
        subMenuDonHang.setManaged(!isVisible);
    }

    @FXML
    protected void toggleSubMenuSanPham() {
        boolean isVisible = subMenuSanPham.isVisible();
        new SlideInRight(subMenuSanPham).play();
        subMenuSanPham.setVisible(!isVisible);
        subMenuSanPham.setManaged(!isVisible);
    }

    @FXML
    protected void trangChinhClick(){
        for (Node node : paneNoiDung.getChildren()) {
            node.setVisible(false);
        }
        paneTrangChinh.setVisible(true);
    }

    @FXML
    protected void themDonHangClick(){
        for (Node node : paneNoiDung.getChildren()) {
            node.setVisible(false);
        }
        paneThemDonHang.setVisible(true);
    }

    private void openDialog(String chucNang) {
        DialogFunction dialog = new DialogFunction(chucNang);
        dialog.showAndWait();
    }
}
