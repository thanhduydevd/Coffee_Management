package com.damcafe.app.controller;

import com.damcafe.app.connectDB.ConnectDB;
import com.damcafe.app.dao.TrangChinh_DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

public class MainPageController {
    private TrangChinh_DAO trangChinh_DAO;

    @FXML
    private PieChart drinkChart;

    @FXML
    private BarChart revenueChart;

    @FXML
    private CategoryAxis dayAxis;

    @FXML
    private Text totalOrderToday, percentageOrders, totalMoneyToday, percentageMoneyByDay, totalMoneyThisMonth, percentageMoneyThisMonth;

    public void initialize(){
        try {
            Connection con = ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        trangChinh_DAO = new TrangChinh_DAO();

        /** Tải dữ liệu cho 2 biểu đồ **/
        loadPieChart();
        loadBarChart();


        /** Thông tin doanh thu **/
        DecimalFormat currencyUnit = new DecimalFormat("#,###.## VNĐ");
        DecimalFormat percent = new DecimalFormat("#.00");

        /** Tổng hóa đơn của ngày nay và phần trăm so với ngày trước **/
        totalOrderToday.setText(trangChinh_DAO.getTotalOrderToday() + " hoá đơn");
        if (trangChinh_DAO.getPercentageOrders() == 0.0){
            percentageOrders.setText("Chưa có hóa đơn");
        }else {
            percentageOrders.setText(trangChinh_DAO.getPercentageOrders() >= 0 ? "+" + percent.format(trangChinh_DAO.getPercentageOrders()) + "%" : percent.format(trangChinh_DAO.getPercentageOrders()) + "%");
        }
        if (trangChinh_DAO.getPercentageOrders() < 0.0)
            percentageOrders.setStyle(String.format("-fx-fill: red;"));

        /** Tổng tiền của ngày nay và phần trăm so với ngày trước **/
        totalMoneyToday.setText(currencyUnit.format(trangChinh_DAO.getTotalMoneyToday()));
        if (trangChinh_DAO.getPercentageMoneyByDay() == 0.0){
            percentageMoneyByDay.setText("Chưa có hóa đơn");
        }else {
            percentageMoneyByDay.setText(trangChinh_DAO.getPercentageMoneyByDay() >= 0 ? "+" + percent.format(trangChinh_DAO.getPercentageMoneyByDay()) + "%" : percent.format(trangChinh_DAO.getPercentageMoneyByDay()) + "%");
        }
        if (trangChinh_DAO.getPercentageMoneyByDay() < 0.0)
            percentageMoneyByDay.setStyle(String.format("-fx-fill: red;"));

        /** Tổng tiền của tháng nay và phần trăm so với tháng trước **/
        totalMoneyThisMonth.setText(currencyUnit.format(trangChinh_DAO.getTotalMoneyThisMonth()));
        if (trangChinh_DAO.getPercentageMoneyThisMonth() == 0.0){
            percentageMoneyThisMonth.setText("Chưa có hóa đơn");
        }else {
            percentageMoneyThisMonth.setText(trangChinh_DAO.getPercentageMoneyThisMonth() >= 0 ? "+" + percent.format(trangChinh_DAO.getPercentageMoneyThisMonth()) + "%" : percent.format(trangChinh_DAO.getPercentageMoneyThisMonth()) + "%");
        }
        if (trangChinh_DAO.getPercentageMoneyThisMonth() < 0.0)
            percentageMoneyThisMonth.setStyle(String.format("-fx-fill: red;"));
    }

    /** Biểu đồ tròn **/
    private void loadPieChart() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                TrangChinh_DAO.getQuantityByCategory()
        );

        drinkChart.setData(pieData);

        for (PieChart.Data data : pieData) {
            data.getNode().setOnMouseEntered(e -> {
                drinkChart.setTitle(data.getName() + ": " + data.getPieValue());
            });
        }
    }

    /** Biểu đồ cột **/
    private void loadBarChart() {
        revenueChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu tháng " + LocalDate.now().getMonthValue());

        Map<Integer, Double> data = TrangChinh_DAO.getRevenueByDayInCurrentMonth();
        int days = LocalDate.now().lengthOfMonth();

        ObservableList<String> categories = FXCollections.observableArrayList();
        for (int i = 1; i <= days; i++) {
            categories.add("Ngày " + i);
            double doanhThu = data.getOrDefault(i, 0.0);
            series.getData().add(new XYChart.Data<>("Ngày " + i, doanhThu));
        }
        dayAxis.setCategories(categories);
        revenueChart.getData().add(series);
    }
}
