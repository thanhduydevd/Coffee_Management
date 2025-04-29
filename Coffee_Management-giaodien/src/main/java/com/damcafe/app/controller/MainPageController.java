package com.damcafe.app.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.time.LocalDate;
import java.util.Random;

public class MainPageController {
    @FXML
    private PieChart drinkChart;

    @FXML
    private BarChart revenueChart;

    @FXML
    private CategoryAxis dayAxis;

    @FXML
    private NumberAxis revenueAxis;

    public void initialize(){
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

        // Tạo dữ liệu giả
        ObservableList<ObservableList<Object>> fakeData = FXCollections.observableArrayList();
        String[] danhSachMon = {"Cà phê sữa đá", "Espresso", "Trà sữa matcha", "Sinh tố bơ", "Nước ép cam"};

        // Tạo 10 dòng dữ liệu
        for (int i = 1; i <= 50; i++) {
            String tenMon = danhSachMon[rand.nextInt(danhSachMon.length)];
            int soLuong = rand.nextInt(5) + 1; // 1 - 5 ly
            double donGia = rand.nextDouble() * 50 + 20; // Giá từ 20k - 70k
            double thanhTien = soLuong * donGia;

            ObservableList<Object> row = FXCollections.observableArrayList(i, tenMon, soLuong, donGia, thanhTien);
            fakeData.add(row);
        }
    }
}
