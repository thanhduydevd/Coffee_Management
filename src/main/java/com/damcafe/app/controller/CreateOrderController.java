package com.damcafe.app.controller;

import com.damcafe.app.dao.CreateOrder_DAO;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Product;
import com.damcafe.app.entity.Size;
import com.damcafe.app.gui.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateOrderController {
    @FXML
    private Button btnQuantity, btnNote, btnCancel, btnPayment;

    @FXML
    private TableView<OrderDetail> tableDonHang;

    @FXML
    private ComboBox<Size> cbbSize;

    @FXML
    private TableColumn<OrderDetail,String> colTenMon,colGhiChu;

    @FXML
    private TableColumn<OrderDetail, Integer> colSTT,colSoLuong;

    @FXML
    private TableColumn<OrderDetail,Size> colKichCo;

    @FXML
    private TableColumn<OrderDetail, Double> colDonGia,colThanhTien;

    @FXML
    private FlowPane mon;
    public static int hashOrderDetail = CreateOrder_DAO.getMaxHash();
    private ArrayList<Product> productList;

    public void initialize(){

        cbbSize.getItems().addAll(Size.S, Size.M, Size.L);
        cbbSize.setValue(Size.M);

        //Sự kiện click cho các button ở chức năng tạo đơn hàng
        btnQuantity.setOnAction(e -> openDialog("dieuchinhsoluong"));
        btnNote.setOnAction(e -> openDialog("thietlapghichu"));
        btnCancel.setOnAction(e -> openDialog("huymon"));
        btnPayment.setOnAction(e-> openDialog("thanhtoan"));

        //lấy product về từ sql
        ArrayList<Product> productList = loadProductsToPane();

        // Thiết lập các cột của TableView
        colSTT.setCellValueFactory(new PropertyValueFactory<>("stt"));
        colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        colKichCo.setCellValueFactory(new PropertyValueFactory<>("kichCo"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        // Tạo một danh sách các OrderDetail và thêm vào TableView
        ObservableList<OrderDetail> orderDetails = FXCollections.observableArrayList();
        tableDonHang.setItems(orderDetails);
        mon.setOnMouseClicked(e -> {
            System.out.println(xuLiChuoiTB(e.getTarget().toString()));
//            addProductToOrder(clickedProduct);
        });
    }

//    private void addProductToOrder(Product product) {
//
//        ObservableList<OrderDetail> currentList = table.getItems();
//
//        for (OrderDetail od : currentList) {
//            if (od.getName().equals(product.getTenSanPham()) && od.getSize() == cbbSize.getValue()) {
//                od.setQuatity(od.getQuatity() + 1);
//                table.refresh();
//                return;
//            }
//        }
//
//        OrderDetail orderDetail = new OrderDetail(
//                getHashOrderDetail(),
//                currentList.size() + 1,
//                product.getTenSanPham(),
//                cbbSize.getValue(),
//                1,
//                "",
//                product.getGiaGoc()
//        );
//        currentList.add(orderDetail);
//    }


    private ArrayList<Product> loadProductsToPane() {
        ArrayList<Product> danhSach = CreateOrder_DAO.loadProductFromDB();
        for (Product p : danhSach) {
            addProductToPane(p);
        }
        return danhSach;
    }

    private void addProductToPane(Product product) {
        VBox box = new VBox();
        box.getStyleClass().add("box-sanpham");
        box.getStylesheets().add(getClass().getResource("/com/damcafe/app/styles/dashboard_style.css").toExternalForm());

        // Tên sản phẩm
        Text name = new Text(product.getTenSanPham());
        name.setWrappingWidth(100);
        name.setTextAlignment(TextAlignment.CENTER);

        // Giá sản phẩm
        Label price = new Label(String.format("%.0fđ", product.getGiaGoc()));

        // Nếu có hình ảnh thì tạo ImageView
        String imgPath = product.getHinhAnh();
        if (imgPath != null && !imgPath.isBlank()) {
            try {
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
                imageView.setFitHeight(100);
                imageView.setFitWidth(70);
                imageView.setPreserveRatio(true);
                imageView.setPickOnBounds(true);
                box.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("Không thể tải ảnh: " + imgPath);
                // Không thêm ImageView nếu lỗi
            }
        }

        box.getChildren().addAll(name, price);

        // Click để thêm vào đơn hàng
        box.setOnMouseClicked(e -> {
            System.out.println("Clicked on: " + product.getTenSanPham());
            // openDialog("chitietdonhang");
        });

        mon.getChildren().add(box);

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
    private String getHashOrderDetail(){
        DecimalFormat deFomat = new DecimalFormat("000");
        return String.format("CTHD%s",deFomat.format(hashOrderDetail++));
    }

    private String xuLiChuoiTB(String chuoi){
        Pattern pattern = Pattern.compile("text=\"(.*)\"");
        Matcher matcher = pattern.matcher(chuoi);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
