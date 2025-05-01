package com.damcafe.app.controller.dialog;

import com.damcafe.app.controller.ListOrderController;
import com.damcafe.app.dao.OrderDetail_DAO;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Size;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class OrderDetailController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private TableView<OrderDetail> tableDonHang;

    @FXML
    private TableColumn<OrderDetail, Integer> colSTT, colSoLuong;

    @FXML
    private TableColumn<OrderDetail, String> colTenMon, colKichCo;

    @FXML
    private TableColumn<OrderDetail, Double> colDonGia, colThanhTien;

    private ObservableList<OrderDetail> list;

    public void initialize() {
        list = FXCollections.observableArrayList(OrderDetail_DAO.getODByOrderID(ListOrderController.maHD123));
        ButtonType applyButton = new ButtonType("Kết thúc", ButtonBar.ButtonData.APPLY);
        loadODToTable();
        dialogPane.getButtonTypes().add(applyButton);
    }

    public void loadODToTable() {
        colSTT.setCellValueFactory(param -> {
            int index = tableDonHang.getItems().indexOf(param.getValue()) + 1;
            return new SimpleIntegerProperty(index).asObject();
        });

        colTenMon.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        colKichCo.setCellValueFactory(cellData -> {
            String kc;
            if (cellData.getValue().getSize().equals(Size.M)) {
                kc = "Medium";
            } else if (cellData.getValue().getSize().equals(Size.S)) {
                kc = "Small";
            } else {
                kc = "Large";
            }
            return new SimpleStringProperty(kc);
        });

        colSoLuong.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuatity()).asObject());

        colDonGia.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        colThanhTien.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());

        tableDonHang.setItems(list);
    }
}
