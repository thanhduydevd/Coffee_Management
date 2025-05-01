package com.damcafe.app.controller;

import com.damcafe.app.dao.Order_DAO;
import com.damcafe.app.entity.Order;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.gui.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class ListOrderController {
    @FXML
    private Button btnOrderDetail;

    @FXML
    private TableView<Order> tableDonHang1;

    @FXML
    private TableColumn<Order, Integer> colSTT1;
    @FXML
    private TableColumn<Order, String> colTenMon1,colNhanVIen,colGhiChu;

    @FXML
    private TableColumn<Order, LocalDate> colDate;

    @FXML
    private TableColumn<Order, String> colHinhThuc;

    @FXML
    private TableColumn<Order,Double> colThanhTien1;


    public void initialize(){
        loadOrderToTable();
        //Sự kiện click cho các button ở chức năng xem đơn hàng
        btnOrderDetail.setOnAction(e-> openDialog("chitietdonhang"));
    }

    private void loadOrderToTable() {
        ArrayList<Order> list = Order_DAO.loadOrderFromDB();
        for(Order o: list){
            addOrderToTable();
        }
    }

    private void addOrderToTable() {
        // Giả sử bạn có danh sách các Order từ DB
        ArrayList<Order> orders = Order_DAO.loadVaoOrderList();

        ObservableList<Order> orderList = FXCollections.observableArrayList(orders);

        // Thiết lập CellValueFactory
        colSTT1.setCellValueFactory(param -> {
            int index = tableDonHang1.getItems().indexOf(param.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(index).asObject();
        });
        colTenMon1.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrderID()));
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDate()));
        colNhanVIen.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserID()));
        colHinhThuc.setCellValueFactory(cellData -> {
            String ht = cellData.getValue().isBringBack() ? "Mang về" : "Tại quán";
            return new javafx.beans.property.SimpleStringProperty(ht);
        });
        colGhiChu.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTableID()));
        colThanhTien1.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTotal()));

        // Đưa dữ liệu vào bảng
        tableDonHang1.setItems(orderList);
    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }


}
