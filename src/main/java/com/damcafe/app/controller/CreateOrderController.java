package com.damcafe.app.controller;

import com.damcafe.app.controller.dialog.SetNoteController;
import com.damcafe.app.controller.dialog.SetQuantityController;
import com.damcafe.app.dao.Ban_DAO;
import com.damcafe.app.dao.Product_DAO;
import com.damcafe.app.entity.Ban;
import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Product;
import com.damcafe.app.entity.Size;
import com.damcafe.app.gui.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;

public class CreateOrderController {
    @FXML
    private Button btnQuantity, btnNote, btnCancel, btnPayment;

    @FXML
    private TableView<OrderDetail> tableDonHang;

    @FXML
    private ComboBox<Size> cbbSize;
    @FXML
    private RadioButton isHere;

    @FXML
    private TableColumn<OrderDetail,String> colTenMon,colGhiChu;

    @FXML
    private TableColumn<OrderDetail, Integer> colSTT,colSoLuong;

    @FXML
    private TableColumn<OrderDetail,Size> colKichCo;

    @FXML
    private TableColumn<OrderDetail, Double> colDonGia,colThanhTien;

    @FXML
    private FlowPane mon,ban;

    @FXML
    private TabPane right;

    @FXML
    private Label txtViTri ,txtTime,txtNhanVien,txtTotal;


    @FXML
    private Tab khuVuc,menu;

    public static int hashOrderDetail = Product_DAO.getMaxHash();
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

        //lấy bàn về từ sql;
        loadBanToPane();
        // Thiết lập các cột của TableView
        colSTT.setCellValueFactory(new PropertyValueFactory<>("stt"));
        colTenMon.setCellValueFactory(new PropertyValueFactory<>("name"));
        colKichCo.setCellValueFactory(new PropertyValueFactory<>("size"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("quatity"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("price"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Tạo một danh sách các OrderDetail và thêm vào TableView
        ObservableList<OrderDetail> orderDetails = FXCollections.observableArrayList();
        tableDonHang.setItems(orderDetails);

        //xử lí tắt bàn thông qua radio mang về
        isHere.setOnAction(e -> {
            if (isHere.isSelected()) {
                khuVuc.setDisable(true);
                right.getSelectionModel().select(menu);  // Chuyển sang tab Menu
            } else {
                khuVuc.setDisable(false);
            }
        });

        //Xử lí thay đổi thông tin trực tiếp trên bảng
        tableDonHang.setEditable(true);

        colSoLuong.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSoLuong.setOnEditCommit(event -> {
            OrderDetail od = event.getRowValue();
            od.setQuatity(event.getNewValue());
            tableDonHang.refresh();
        });

        colGhiChu.setCellFactory(TextFieldTableCell.forTableColumn());
        colGhiChu.setOnEditCommit(event -> {
            OrderDetail od = event.getRowValue();
            od.setComment(event.getNewValue());
        });

        if (!tableDonHang.getItems().isEmpty()) {
            OrderDetail od = tableDonHang.getItems().getFirst();
            od.setQuatity(od.getQuatity() + 1);
            tableDonHang.refresh();
        }
        //thay đổi thông tin thông qua button
        btnNote.setOnAction(e -> {
            OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setComment(showNoteDialog());
                tableDonHang.refresh();
            } else {}
        });
        btnQuantity.setOnAction(e->{
            OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setQuatity(showQuantityDialog());
                tableDonHang.refresh();
            } else {}
        });
        cbbSize.setOnAction(e ->{
            OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setSize(cbbSize.getValue());
                tableDonHang.refresh();
            } else {}
        });


    }

    //Thêm sản phẩm vào hóa đơn
    private void addProductToOrder(Product product, int i , String mess) {

        ObservableList<OrderDetail> currentList = tableDonHang.getItems();

        for (OrderDetail od : currentList) {
            if (od.getName().equals(product.getTenSanPham()) && od.getSize() == cbbSize.getValue()) {
                od.setQuatity(od.getQuatity() + 1);
                tableDonHang.refresh();
                return;
            }
        }

        OrderDetail orderDetail = new OrderDetail(
                getHashOrderDetail(),
                currentList.size() + 1,
                product.getTenSanPham(),
                cbbSize.getValue(),
                i,
                mess,
                product.getGiaGoc()
        );
        currentList.add(orderDetail);
    }


    private ArrayList<Ban> loadBanToPane() {
        ArrayList<Ban> danhSach = Ban_DAO.loadBanFromDB();
        for (Ban p : danhSach) {
            addBanToFlowPane(p);
        }
        return danhSach;
    }

    //load bàn vào danh sách
    private void addBanToFlowPane(Ban b) {
        VBox box = new VBox();
        box.getStyleClass().add("box-vitri");
        if (b.isUse()) {
            box.getStyleClass().add("box-vitri-active");
        }
        box.getStylesheets().add(getClass().getResource("/com/damcafe/app/styles/dashboard_style.css").toExternalForm());

        Text textTenBan = new Text(b.getMaBan());
        Label labelTrangThai = new Label(b.isUse()?"Có khách":"Trống");

        box.getChildren().addAll(textTenBan, labelTrangThai);
        ban.getChildren().add(box);

        ban.setOnMouseClicked(e -> {
            if(b.isUse()){
                b.setUse(false);
                txtViTri.setText("");
            }else{
                txtViTri.setText( b.getMaBan()+ " - "+ b.getTang().getTenTang());
            }
        });
        box.getStyleClass().add("box-vitri");
        if (b.isUse()) {
            box.getStyleClass().add("box-vitri-active");
        }
    }

    private ArrayList<Product> loadProductsToPane() {
        ArrayList<Product> danhSach = Product_DAO.loadProductFromDB();
        for (Product p : danhSach) {
            addProductToPane(p);
        }
        return danhSach;
    }

    //load sản phẩm vào danh sách
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
                imageView.setId(product.getMaSanPham());
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
            int i = showQuantityDialog();
            String mess = showNoteDialog();
            addProductToOrder(product, i, mess);
        });

        mon.getChildren().add(box);

    }

    public void openDialog(String chucNang) {
        ShowDialog dialog = new ShowDialog(chucNang);
        dialog.showAndWait();
    }

    private String getHashOrderDetail(){
        DecimalFormat deFomat = new DecimalFormat("000");
        return String.format("CTHD%s",deFomat.format(hashOrderDetail++));
    }

    public String showNoteDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/damcafe/app/views/order/dialogs/set_note.fxml"));
            DialogPane pane = loader.load();

            SetNoteController controller = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Nhập ghi chú");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
                return controller.getNoteText(); // Lấy nội dung ghi chú từ TextArea
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // Trả về chuỗi rỗng nếu huỷ hoặc có lỗi
    }

    public int showQuantityDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/damcafe/app/views/order/dialogs/set_quantity.fxml"));
            DialogPane pane = loader.load();


            SetQuantityController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Nhập số lượng");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
                return controller.getQuantity();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1; // Mặc định nếu người dùng huỷ hoặc xảy ra lỗi
    }

}
