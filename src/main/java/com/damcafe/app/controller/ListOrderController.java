package com.damcafe.app.controller;

import com.damcafe.app.dao.Order_DAO;
import com.damcafe.app.entity.Order;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.gui.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class ListOrderController {
    @FXML
    private Button btnOrderDetail;

    @FXML
    private TableView<Order> tableDonHang1;

    private ObservableList<Order> danhSachDonHang = FXCollections.observableArrayList();
    private ObservableList<Order> danhSachGoc = FXCollections.observableArrayList();

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

    @FXML
    private TextField txtTim;

    @FXML
    private DatePicker txtNgay;

    @FXML
    private ComboBox<String> choise;

    ArrayList<Order> orders = Order_DAO.loadVaoOrderList();
    public static String maHD123 ;

    public void initialize(){
        loadOrderToTable();
        //Sự kiện click cho các button ở chức năng xem đơn hàng
        btnOrderDetail.setOnAction(e-> {
            Order selectedOrder = tableDonHang1.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                maHD123 = selectedOrder.getOrderID(); // Lấy mã hóa đơn được chọn
                openDialog("chitietdonhang");
            } else {
                // Nếu chưa chọn hóa đơn nào
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn một hóa đơn để xem chi tiết!");
                alert.showAndWait();
            }
        });
        tableDonHang1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Bật chế độ chọn nhiều
        txtTim.textProperty().addListener((obs, oldText, newText) -> {

            tableDonHang1.getSelectionModel().clearSelection();
            for (Order a : tableDonHang1.getItems()) {
                if (a.getOrderID().toLowerCase().contains(newText.toLowerCase())) {
                    tableDonHang1.getSelectionModel().select(a);
                    tableDonHang1.scrollTo(a); // Cuộn đến dòng
                }
            }
            if (txtTim.getText().equals("")){
                tableDonHang1.getSelectionModel().clearSelection();
            }
        });
        txtNgay.valueProperty().addListener((obs, oldDate, newDate) -> {
            // Kiểm tra nếu người dùng chọn một ngày hợp lệ
            if (newDate != null) {
                // Lọc các đơn hàng dựa trên ngày
                tableDonHang1.getSelectionModel().clearSelection(); // Xóa tất cả lựa chọn trước khi tìm kiếm

                for (Order a : tableDonHang1.getItems()) {
                    // Kiểm tra nếu ngày của đơn hàng trùng với ngày người dùng chọn
                    if (a.getDate() != null && a.getDate().equals(newDate)) {
                        tableDonHang1.getSelectionModel().select(a); // Chọn dòng nếu trùng ngày
                        tableDonHang1.scrollTo(a); // Cuộn đến dòng
                    }
                }
            }
        });
        choise.getItems().addAll(   "Mặc định", "Tên A → Z", "Tên Z → A", "Giá thấp → cao", "Giá cao → thấp" );
        choise.setOnAction(event -> {
            String selected = choise.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            Comparator<Order> comparator = null;

            switch (selected) {
                case "Tên A → Z":
                    comparator = Comparator.comparing(Order::getOrderID, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "Tên Z → A":
                    comparator = Comparator.comparing(Order::getOrderID, String.CASE_INSENSITIVE_ORDER).reversed();
                    break;
                case "Giá thấp → cao":
                    comparator = Comparator.comparingDouble(Order::getTotal);
                    break;
                case "Giá cao → thấp":
                    comparator = Comparator.comparingDouble(Order::getTotal).reversed();
                    break;
                case "Mặc định":
                    tableDonHang1.setItems(FXCollections.observableArrayList(danhSachGoc)); // trả về thứ tự ban đầu
                    return;
            }

            if (comparator != null) {
                FXCollections.sort(danhSachDonHang, comparator);
                tableDonHang1.setItems(danhSachDonHang);
            }
        });
    }


    private void loadOrderToTable() {
        ArrayList<Order> list = Order_DAO.loadOrderFromDB();
        for(Order o: list){
            addOrderToTable();
        }
    }

    private void addOrderToTable() {
        // Giả sử bạn có danh sách các Order từ DB

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
        danhSachDonHang = orderList;
        tableDonHang1.setItems(orderList);
    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }


}
