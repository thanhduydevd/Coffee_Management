package com.damcafe.app.controller;

import com.damcafe.app.controller.dialog.SetNoteController;
import com.damcafe.app.controller.dialog.SetQuantityController;
import com.damcafe.app.dao.*;
import com.damcafe.app.entity.*;
import com.damcafe.app.gui.ShowDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

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

    @FXML
    private TextField txtTim;

    @FXML
    private ComboBox<String> cbbDanhMuc,cbbSort;

    private String name = UserSession.getUsername();
    private TaiKhoan taiKhoanHienTai = TaiKhoan_DAO.getTaiKhoanTheoTen(name);

    public static int hashOrderDetail = Product_DAO.getMaxHash();
    public NhanVien nhanVien = NhanVien_DAO.getNhanVien(taiKhoanHienTai.getMaNhanVien().getMaNhanVien());

    private ArrayList<Product> allProducts = Product_DAO.loadProductFromDB();;
    private VBox selectedBox = null;

    public static double tongTienUI = 0;
    public void initialize(){

        cbbSize.getItems().addAll(Size.S, Size.M, Size.L);
        cbbSize.setValue(Size.M);

        //nhân viên hiện tại
//        if (nhanVien != null){
//            txtNhanVien.setText(nhanVien.getTenNhanVien());
//            System.out.println("Nhân viên: "+UserSession.getUsername());
//        }else{
//            txtNhanVien.setText("Không có");
//        }

        //Sự kiện click cho các button ở chức năng tạo đơn hàng
        btnPayment.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/com/damcafe/app/views/order/dialogs/payment.fxml"
                ));
                tongTienUI = getTotal();
                DialogPane dialogPane = loader.load();

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Xác nhận thanh toán");
                dialog.setDialogPane(dialogPane);

                // Hiển thị và đợi kết quả
                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
                    xuLiTaoHoaDon(); // Gọi phương thức tạo hóa đơn nếu xác nhận
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                ShowDialog.showMessageDialog(btnPayment, "Không thể mở hộp thoại thanh toán!");
            }
        });


        //lấy product về từ sql
        ArrayList<Product> productList = loadProductsToPane(allProducts);

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
        /**
         * @return: 1 nếu có nhập không đúng địng dạng.
         */
        btnQuantity.setOnAction(e->{
            OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setQuatity(showQuantityDialog());
                tableDonHang.refresh();
            } else {}
        });
        btnCancel.setOnAction(e -> {
            cancelOrder();
        });
        cbbSize.setOnAction(e ->{
            OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setSize(cbbSize.getValue());
                tableDonHang.refresh();
            } else {}
        });

        cbbDanhMuc.getItems().addAll("Tất cả", "Cà phê", "Trà", "Sinh tố", "Khác");
        cbbDanhMuc.setValue("Tất cả");

        cbbSort.getItems().addAll("Tên A-Z", "Tên Z-A", "Giá tăng dần", "Giá giảm dần");
        cbbSort.setValue("Tên A-Z");

// Gọi lọc khi có thay đổi
        txtTim.textProperty().addListener((obs, oldText, newText) -> filterProductList());
        cbbSort.setOnAction(e -> filterProductList());
        cbbDanhMuc.setOnAction(e -> filterProductList());
    }


    private void xuLiTaoHoaDon() {
        if (tableDonHang.getItems().isEmpty()) {
            ShowDialog.showMessageDialog(btnPayment, "Chưa có món để thanh toán!");
            return;
        }

        if (txtViTri.getText().isEmpty()) {
            ShowDialog.showMessageDialog(btnPayment, "Vui lòng chọn bàn trước khi thanh toán!");
            return;
        }

        // Tạo mã hoá đơn mới (ví dụ tạm thời)
        String maHD = "HD" + System.currentTimeMillis();

        // Tính tổng tiền
        double tongTien = getTotal();

        // Lấy mã bàn
        String maBan = txtViTri.getText().split(" - ")[0];

        // Tạo hoá đơn
        Order hoaDon = new Order(maHD);
        hoaDon.setDate(LocalDate.now());
        hoaDon.setUserID(nhanVien==null?"none":nhanVien.getMaNhanVien());
        hoaDon.setTableID(maBan);
//        hoaDon.setSaleID();  //Khuyến mãi cần sửa đổi
        hoaDon.setTotal(tongTien);
        hoaDon.setBringBack(isHere.isSelected());

        boolean hoaDonOK = Order_DAO.addOrderToDB(hoaDon);

        if (!hoaDonOK) {
            ShowDialog.showMessageDialog(btnPayment, "Tạo hoá đơn thất bại!");
            return;
        }

        // Tạo chi tiết hoá đơn
        for (OrderDetail od : tableDonHang.getItems()) {
            OrderDetail ct = new OrderDetail(getHashOrderDetail());
            ct.setOrderID(maHD);
            ct.setProductID(od.getProductID());
            ct.setQuatity(od.getQuatity());
            ct.setPrice(od.getPrice());
            ct.setComment(od.getComment());
            ct.setSize(od.getSize());

            OrderDetail_DAO.addODToDB(ct);
        }

        // Cập nhật trạng thái bàn (nếu cần)
//        Ban_DAO.setUse(maBan, true);

        // Xoá dữ liệu hiện tại
        tableDonHang.getItems().clear();
        txtViTri.setText("");
        ShowDialog.showMessageDialog(btnPayment, "Thanh toán thành công!");
    }



    public void cancelOrder() {
        OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/com/damcafe/app/views/order/dialogs/cancel_order.fxml"
                ));
                DialogPane pane = loader.load();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(pane);
                dialog.setTitle("Xác nhận huỷ món");

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
                    tableDonHang.getItems().remove(selected);
                    // Cập nhật lại số thứ tự sau khi xóa
                    for (int i = 0; i < tableDonHang.getItems().size(); i++) {
                        tableDonHang.getItems().get(i).setStt(i + 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ShowDialog.showMessageDialog(btnCancel, "Chưa chọn món để huỷ!");
        }
    }


    //Thêm sản phẩm vào hóa đơn
    private void addProductToOrder(Product product, int i , String mess) {

        ObservableList<OrderDetail> currentList = tableDonHang.getItems();

        for (OrderDetail od : currentList) {
            if (od.getName().equals(product.getTenSanPham()) && od.getSize() == cbbSize.getValue()) {
                od.setQuatity(od.getQuatity() + 1);
                tableDonHang.refresh();  // Cập nhật lại TableView
                return;
            }
        }
        OrderDetail orderDetail = new OrderDetail(
                getHashOrderDetail(),
                product.getMaSanPham(),
                cbbSize.getValue(),
                i,
                product.getGiaGoc(),
                mess,
                product.getTenSanPham(),currentList.size() + 1
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

        box.getStylesheets().add(getClass().getResource("/com/damcafe/app/styles/dashboard_style.css").toExternalForm());

        Text textTenBan = new Text(b.getMaBan());
        Label labelTrangThai = new Label(b.isUse() ? "Có khách" : "Trống");

        box.getChildren().addAll(textTenBan, labelTrangThai);
        ban.getChildren().add(box);

        // Nếu bàn đang dùng, giữ màu active ban đầu
        if (b.isUse()) {
            box.getStyleClass().add("box-vitri-active");
        }

        box.setOnMouseClicked(e -> {
            // Gỡ bỏ class active khỏi bàn trước
            if (selectedBox != null) {
                selectedBox.getStyleClass().remove("box-vitri-active");
            }

            // Thêm class active cho bàn mới
            if (!box.getStyleClass().contains("box-vitri-active")) {
                box.getStyleClass().add("box-vitri-active");
            }

            selectedBox = box;

            // Cập nhật vị trí bàn được chọn
            txtViTri.setText(b.getMaBan() + " - " + b.getTang().getTenTang());
            System.out.println("Đã chọn: " + b.getMaBan());
        });
    }

    private ArrayList<Product> loadProductsToPane(ArrayList<Product> danhSach) {
        mon.getChildren().clear();
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
                ImageView imageView = new ImageView(getClass().getResource(
                        "/com/damcafe/app/images/products/" + imgPath
                ).toExternalForm());
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

    /**
     * Dùng để gọi thử có dialog khi test
     * @param chucNang
     */
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

    private void filterProductList() {
        String keyword = txtTim.getText().toLowerCase();
        String selectedCategory = cbbDanhMuc.getValue();
        String sortOption = cbbSort.getValue();

        ArrayList<Product> filtered = new ArrayList<>();

        for (Product p : allProducts) {
            boolean matchKeyword = p.getTenSanPham().toLowerCase().contains(keyword);
            boolean matchCategory = selectedCategory.equals("Tất cả") || p.getLoaiSanPham().getTenLoaiSanPham().equalsIgnoreCase(selectedCategory);

            if (matchKeyword && matchCategory) {
                filtered.add(p);
            }
        }

        // Sắp xếp
        filtered.sort((p1, p2) -> {
            switch (sortOption) {
                case "Tên A-Z":
                    return p1.getTenSanPham().compareToIgnoreCase(p2.getTenSanPham());
                case "Tên Z-A":
                    return p2.getTenSanPham().compareToIgnoreCase(p1.getTenSanPham());
                case "Giá tăng dần":
                    return Double.compare(p1.getGiaGoc(), p2.getGiaGoc());
                case "Giá giảm dần":
                    return Double.compare(p2.getGiaGoc(), p1.getGiaGoc());
                default:
                    return 0;
            }
        });

        // Hiển thị lại
        loadProductsToPane(filtered);
    }

    private double getTotal(){
        double a = 0;
        for (OrderDetail chiTiet : tableDonHang.getItems()) {
            a += chiTiet.getTotal();
        }
        return a;
    }
}
