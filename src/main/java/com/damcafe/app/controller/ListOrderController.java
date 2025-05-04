package com.damcafe.app.controller;

import com.damcafe.app.dao.NhanVien_DAO;
import com.damcafe.app.dao.Order_DAO;
import com.damcafe.app.entity.NhanVien;
import com.damcafe.app.entity.Order;
import com.damcafe.app.gui.ShowDialog;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class ListOrderController {

    @FXML private Button btnOrderDetail;
    @FXML private TableView<Order> tableDonHang1;
    @FXML private TableColumn<Order, Integer> colSTT1;
    @FXML private TableColumn<Order, String> colTenMon1, colNhanVIen, colGhiChu, colHinhThuc;
    @FXML private TableColumn<Order, LocalDate> colDate;
    @FXML private TableColumn<Order, Double> colThanhTien1;
    @FXML private TextField txtTim;
    @FXML private DatePicker txtNgay;
    @FXML private ComboBox<String> choise;

    private ObservableList<Order> danhSachDonHang = FXCollections.observableArrayList();
    private ObservableList<Order> danhSachGoc = FXCollections.observableArrayList();

    public static String maHD123;

    public void initialize() {
        setupTableColumns();
        loadOrderToTable();
        setupListeners();
    }

    private void setupTableColumns() {
        colSTT1.setCellValueFactory(param -> {
            int index = tableDonHang1.getItems().indexOf(param.getValue()) + 1;
            return new SimpleIntegerProperty(index).asObject();
        });

        colTenMon1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderID()));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));

        colNhanVIen.setCellValueFactory(cellData -> {
            NhanVien nv = NhanVien_DAO.getNhanVien(cellData.getValue().getUserID());
            return new SimpleStringProperty(nv != null ? nv.getTenNhanVien() : "Không rõ");
        });

        colHinhThuc.setCellValueFactory(cellData -> {
            String ht = cellData.getValue().isBringBack() ? "Mang về" : "Tại quán";
            return new SimpleStringProperty(ht);
        });

        colGhiChu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTableID()));
        colThanhTien1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotal()));
    }

    private void loadOrderToTable() {
        ArrayList<Order> list = Order_DAO.loadOrderFromDB();
        danhSachDonHang = FXCollections.observableArrayList(list);
        danhSachGoc = FXCollections.observableArrayList(list);

        tableDonHang1.setItems(danhSachDonHang);
    }

    private void setupListeners() {
        // Xem chi tiết đơn hàng
        btnOrderDetail.setOnAction(e -> {
            Order selectedOrder = tableDonHang1.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                maHD123 = selectedOrder.getOrderID();
                openDialog("chitietdonhang");
            } else {
                showAlert("Vui lòng chọn một hóa đơn để xem chi tiết!");
            }
        });

        // Tìm kiếm theo mã hóa đơn
        txtTim.textProperty().addListener((obs, oldText, newText) -> {
            tableDonHang1.getSelectionModel().clearSelection();
            for (Order a : tableDonHang1.getItems()) {
                if (a.getOrderID().toLowerCase().contains(newText.toLowerCase())) {
                    tableDonHang1.getSelectionModel().select(a);
                    tableDonHang1.scrollTo(a);
                }
            }
        });

        // Lọc theo ngày
        txtNgay.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                tableDonHang1.getSelectionModel().clearSelection();
                for (Order a : tableDonHang1.getItems()) {
                    if (a.getDate() != null && a.getDate().equals(newDate)) {
                        tableDonHang1.getSelectionModel().select(a);
                        tableDonHang1.scrollTo(a);
                    }
                }
            }
        });

        // Sắp xếp danh sách
        choise.getItems().addAll("Mặc định", "Tên A → Z", "Tên Z → A", "Giá thấp → cao", "Giá cao → thấp");
        choise.setOnAction(event -> {
            String selected = choise.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            Comparator<Order> comparator = switch (selected) {
                case "Tên A → Z" -> Comparator.comparing(Order::getOrderID, String.CASE_INSENSITIVE_ORDER);
                case "Tên Z → A" -> Comparator.comparing(Order::getOrderID, String.CASE_INSENSITIVE_ORDER).reversed();
                case "Giá thấp → cao" -> Comparator.comparingDouble(Order::getTotal);
                case "Giá cao → thấp" -> Comparator.comparingDouble(Order::getTotal).reversed();
                default -> null;
            };

            if (comparator != null) {
                FXCollections.sort(danhSachDonHang, comparator);
                tableDonHang1.setItems(danhSachDonHang);
            } else {
                tableDonHang1.setItems(FXCollections.observableArrayList(danhSachGoc));
            }
        });

        tableDonHang1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }
}
