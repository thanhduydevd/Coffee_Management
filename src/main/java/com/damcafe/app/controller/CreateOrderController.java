package com.damcafe.app.controller;

import com.damcafe.app.dao.CreateOrder_DAO;
import com.damcafe.app.entity.Order;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Size;
import com.damcafe.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CreateOrderController {
    @FXML
    private Button btnQuantity, btnNote, btnCancel, btnPayment;

    @FXML
    private TableView<Order> table;

    @FXML
    private ComboBox<Size> cbbSize;

    @FXML
    private TableColumn<OrderDetail,String> colTenMon,colGhiChu;

    @FXML
    private TableColumn<OrderDetail, Integer> colSTT,colSoLuong;

    @FXML
    private TableColumn<OrderDetail,Size> colKichCo;

    @FXML
    private TableColumn<OrderDetail, Double> ccolDonGia,colThanhTien;

    public static int hashOrderDetail = CreateOrder_DAO.getMaxHash();

    public void initialize(){

        cbbSize.getItems().addAll(Size.Small, Size.Medium, Size.Large);
        cbbSize.setValue(Size.Medium);

        //Sự kiện click cho các button ở chức năng tạo đơn hàng
        btnQuantity.setOnAction(e -> openDialog("dieuchinhsoluong"));
        btnNote.setOnAction(e -> openDialog("thietlapghichu"));
        btnCancel.setOnAction(e -> openDialog("huymon"));
        btnPayment.setOnAction(e-> openDialog("thanhtoan"));


    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }

//    public OrderDetail createOrderDetail(){
//        DecimalFormat deFomat = new DecimalFormat("000");
//        String cthd = String.format("CTHD%s",deFomat.format(hashOrderDetail++));
//        OrderDetail e = new OrderDetail();
//        return e;
//    }

}
