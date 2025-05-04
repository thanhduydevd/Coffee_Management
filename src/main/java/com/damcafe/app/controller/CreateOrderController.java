package com.damcafe.app.controller;

import com.damcafe.app.controller.dialog.SetNoteController;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
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
    private Label txtViTri ,txtNhanVien,txtTotal;


    @FXML
    private Tab khuVuc,menu;

    @FXML
    private TextField txtTim, tfMaKhuyenMai;

    @FXML
    private ComboBox<String> cbbDanhMuc,cbbSort;

    private String name = UserSession.getUsername();
    private TaiKhoan taiKhoanHienTai = TaiKhoan_DAO.getTaiKhoanTheoTen(name);

    public static int hashOrderDetail = Product_DAO.getMaxHash();
    public NhanVien nhanVien = NhanVien_DAO.getNhanVien(taiKhoanHienTai.getMaNhanVien().getMaNhanVien());

    private ArrayList<Product> allProducts = Product_DAO.loadProductFromDB();
    private ArrayList<KhuyenMai> allDiscount = KhuyenMai_DAO.getKhuyenMai();

    private VBox selectedBox = null;

    public static double tongTienUI = 0;

    public double discount = 0.0;
    public String khuyenMaiApDung = "null";
    public void initialize(){
        txtNhanVien.setText(nhanVien.getTenNhanVien());

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
            if (tableDonHang.getItems().isEmpty()) {
                ShowDialog.showMessageDialog(btnPayment, "Chưa có món để thanh toán!");
                return;
            }
            if (!isHere.isSelected() && txtViTri.getText().equals("Chưa chọn")) {
                ShowDialog.showMessageDialog(btnPayment, "Vui lòng chọn bàn trước khi thanh toán!");
                return;
            }
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

        txtViTri.setText("Chưa chọn");

        //xử lí tắt bàn thông qua radio mang về
        isHere.setOnAction(e -> {
            if (isHere.isSelected()) {
                khuVuc.setDisable(true);
                right.getSelectionModel().select(menu);
                txtViTri.setText("Chưa chọn");
                System.out.println(txtViTri.getText());
            } else {
                khuVuc.setDisable(false);
//                txtViTri.setText("Chưa chọn");
            }
        });

        //Xử lí thay đổi thông tin trực tiếp trên bảng
        tableDonHang.setEditable(true);

        colSoLuong.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSoLuong.setOnEditCommit(event -> {
            OrderDetail od = event.getRowValue();
            int oldQuantity = od.getQuatity(); // Lưu lại số lượng hiện tại
            int newQuantity = showQuantityDialog(oldQuantity); // Hiển thị dialog và lấy giá trị mới

            // Cập nhật số lượng nếu người dùng nhập giá trị mới
            if (newQuantity != oldQuantity) {
                od.setQuatity(newQuantity);
                tableDonHang.refresh(); // Cập nhật lại bảng
                updateTotal(); // Cập nhật tổng tiền
            }
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
        colGhiChu.setCellFactory(TextFieldTableCell.forTableColumn());
        colGhiChu.setOnEditCommit(event -> {
            OrderDetail od = event.getRowValue();
            String oldComment = od.getComment(); // Lưu lại ghi chú hiện tại
            String newComment = showNoteDialog(oldComment); // Hiển thị dialog và lấy ghi chú mới

            // Cập nhật ghi chú nếu người dùng nhập giá trị mới
            if (!newComment.equals(oldComment)) {
                od.setComment(newComment);
                tableDonHang.refresh(); // Cập nhật lại bảng
            }
        });

        /**
         * @return: 1 nếu có nhập không đúng địng dạng.
         */
        btnQuantity.setOnAction(e->{
            OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setQuatity(showQuantityDialog(selected.getQuatity()));
                tableDonHang.refresh();
            } else {}
        });
        btnNote.setOnAction(e->{
            OrderDetail od = tableDonHang.getSelectionModel().getSelectedItem();
            String oldComment = od.getComment(); // Lưu lại ghi chú hiện tại
            String newComment = showNoteDialog(oldComment); // Hiển thị dialog và lấy ghi chú mới

            // Cập nhật ghi chú nếu người dùng nhập giá trị mới
            if (!newComment.equals(oldComment)) {
                od.setComment(newComment);
                tableDonHang.refresh(); // Cập nhật lại bảng
            }
        });
        btnCancel.setOnAction(e -> {
            cancelOrder();
            updateTotal();
        });
        cbbSize.setOnAction(e ->{
            OrderDetail selected = tableDonHang.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setSize(cbbSize.getValue());
                tableDonHang.refresh();
            } else {}
        });

        cbbDanhMuc.getItems().addAll("Tất cả", "Cà phê", "Trà sữa", "Ước ép","Sinh tố", "Khác");
        cbbDanhMuc.setValue("Tất cả");

        cbbSort.getItems().addAll("Tên A-Z", "Tên Z-A", "Giá tăng dần", "Giá giảm dần");
        cbbSort.setValue("Tên A-Z");

// Gọi lọc khi có thay đổi
        txtTim.textProperty().addListener((obs, oldText, newText) -> filterProductList());
        cbbSort.setOnAction(e -> filterProductList());
        cbbDanhMuc.setOnAction(e -> filterProductList());


        //Sự kiện cho ô nhập mã khuyến mãi
        tfMaKhuyenMai.setOnKeyReleased(e ->{
            xuLiKhuyenMai(tfMaKhuyenMai.getText());
            updateTotal();
        });
    }

    private void xuLiKhuyenMai(String text){
        if (getTotal() == 0.0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setContentText("Chưa món nào được thêm!");
            alert.showAndWait();
        }else{
            getDiscount(text);
        }
    }

    private void xuLiTaoHoaDon() {


        if ( !isHere.isSelected() || txtViTri.getText().equalsIgnoreCase("Chưa chọn")) {
            ShowDialog.showMessageDialog(btnPayment, "Vui lòng chọn bàn trước khi thanh toán!");
            return;
        }

        // Tạo mã hoá đơn mới (ví dụ tạm thời)
        String maHD = "HD" + System.currentTimeMillis();

        // Tính tổng tiền
        double tongTien = getTotal();

        // Lấy mã bàn
        String maBan = "";
        if (maHD != null){
            maBan = txtViTri.getText().split(" - ")[0];
        }

        // Tạo hoá đơn
        Order hoaDon = new Order(maHD);
        hoaDon.setDate(LocalDate.now());
        hoaDon.setUserID(nhanVien==null?"none":nhanVien.getMaNhanVien());
        hoaDon.setTableID(maBan==""?null:maBan);
        hoaDon.setSaleID(khuyenMaiApDung);
        hoaDon.setTotal(tongTien);
        hoaDon.setBringBack(isHere.isSelected());


        boolean hoaDonOK = Order_DAO.addOrderToDB(hoaDon);

        if (!hoaDonOK) {
            ShowDialog.showMessageDialog(btnPayment, "Tạo hoá đơn thất bại!");
            return;
        }

        // Tạo chi tiết hoá đơn
        ArrayList<OrderDetail> a = new ArrayList<>();
        for (OrderDetail od : tableDonHang.getItems()) {
            OrderDetail ct = new OrderDetail(getHashOrderDetail());
            ct.setOrderID(maHD);
            ct.setProductID(od.getProductID());
            ct.setQuatity(od.getQuatity());
            ct.setPrice(od.getPrice());
            ct.setComment(od.getComment());
            ct.setSize(od.getSize());
            a.add(ct);
            OrderDetail_DAO.addODToDB(ct);
        }

        // Cập nhật trạng thái bàn (nếu cần)
//        Ban_DAO.setUse(maBan, true);

        // Xoá dữ liệu hiện tại
        tableDonHang.getItems().clear();
        txtViTri.setText("");
        ShowDialog.showMessageDialog(btnPayment, "Thanh toán thành công!");
        inHoaDon(hoaDon,a);
    }

    public void inHoaDon(Order order, ArrayList<OrderDetail> orderDetails) {
        StringBuilder invoice = new StringBuilder();

        // In tiêu đề hóa đơn
        invoice.append("====================================\n");
        invoice.append("              HÓA ĐƠN\n");
        invoice.append("====================================\n");

        // In thông tin hóa đơn
        invoice.append("Mã hóa đơn: ").append(order.getOrderID()).append("\n");
        invoice.append("Ngày tạo: ").append(order.getDate()).append("\n");
        invoice.append("Nhân viên: ").append(order.getUserID()).append("\n");

        if (order.getTableID() != null && !order.getTableID().isEmpty()) {
            invoice.append("Bàn: ").append(order.getTableID()).append("\n");
        }

        if (order.getSaleID() != null && !order.getSaleID().isEmpty()) {
            invoice.append("Khuyến mãi: ").append(order.getSaleID()).append("\n");
        }

        invoice.append("====================================\n");

        // In chi tiết hóa đơn (sản phẩm trong đơn hàng)
        invoice.append("Danh sách sản phẩm:\n");

        double totalAmount = 0;  // Tổng tiền
        for (OrderDetail detail : orderDetails) {
            invoice.append(detail.getProductID()).append(" - ")
                    .append("Số lượng: ").append(detail.getQuatity()).append(" - ")
                    .append("Giá: ").append(String.format("%.0fđ", detail.getPrice())).append(" - ")
                    .append("Tổng: ").append(String.format("%.0fđ", detail.getPrice() * detail.getQuatity())).append("\n");

            totalAmount += detail.getPrice() * detail.getQuatity();  // Cộng dồn tổng tiền
        }

        // In tổng tiền
        invoice.append("====================================\n");
        invoice.append("Tổng tiền: ").append(String.format("%.0fđ", totalAmount)).append("\n");

        // Nếu có mang về
        if (order.isBringBack()) {
            invoice.append("Ghi chú: Mang về\n");
        }

        invoice.append("====================================\n");
        invoice.append("Cảm ơn quý khách đã sử dụng dịch vụ!\n");
        invoice.append("====================================\n");

        // In ra console
        System.out.println(invoice.toString());
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
        updateTotal();
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
        Label labelTrangThai = new Label(b.getTang().getTenTang());

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

            right.getSelectionModel().select(menu); // Chuyển sang tab "Menu"
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

        // Nếu có hình ảnh thì tải Image từ thư mục ngoài resources
        String imgPath = product.getHinhAnh();
        if (imgPath != null && !imgPath.isBlank()) {
            try {
                // Đường dẫn tuyệt đối tới thư mục chứa hình ảnh
                String imagePath = "src/main/data_images/products/" + imgPath;  // Thay đổi đường dẫn ở đây

                // Tạo đối tượng File từ đường dẫn
                File file = new File(imagePath);

                // Kiểm tra xem tệp có tồn tại không
                if (file.exists() && file.isFile()) {
                    // Tải ảnh từ file
                    Image image = new Image(file.toURI().toString());  // Dùng URI để tạo Image
                    // Tạo ImageView từ Image
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(100);  // Đặt chiều cao của ảnh
                    imageView.setFitWidth(70);  // Đặt chiều rộng của ảnh
                    imageView.setPreserveRatio(true);  // Đảm bảo tỷ lệ hình ảnh được giữ

                    // Thêm ImageView vào VBox
                    box.getChildren().add(imageView);
                } else {
                    System.err.println("Không thể tìm thấy ảnh: " + imgPath);
                }

            } catch (Exception e) {
                System.err.println("Không thể tải ảnh: " + imgPath);
                // Xử lý lỗi nếu không thể tải ảnh
            }
        }

        box.getChildren().addAll(name, price);

        // Click để thêm vào đơn hàng
        box.setOnMouseClicked(e -> {
            int i = showQuantityDialog(1);
            String mess = showNoteDialog("");
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

    public String showNoteDialog(String currentComment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/damcafe/app/views/order/dialogs/set_note.fxml"));
            DialogPane pane = loader.load();

            SetNoteController controller = loader.getController();
            controller.setCurrentNote(currentComment); // Truyền ghi chú hiện tại vào dialog

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Nhập ghi chú");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
                return controller.getNoteText(); // Trả về ghi chú mới
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentComment; // Trả về ghi chú cũ nếu người dùng hủy
    }


    public int showQuantityDialog(int currentQuantity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/damcafe/app/views/order/dialogs/set_quantity.fxml"));
            DialogPane pane = loader.load();

            com.damcafe.app.controller.dialog.SetQuantityController controller = loader.getController();
            controller.setCurrentQuantity(currentQuantity); // Truyền số lượng hiện tại vào dialog

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Nhập số lượng");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
                return controller.getQuantity(); // Trả về số lượng mới
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentQuantity; // Trả về số lượng cũ nếu người dùng hủy
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
        return a * (1 - discount);
    }

    private void getDiscount(String maKhuyenMai) {
        boolean found = false;
        for (KhuyenMai d : allDiscount) {
            if (d.getMaKhuyenMai().equalsIgnoreCase(maKhuyenMai)) {
                discount = d.getTiGia();
                khuyenMaiApDung = d.getMaKhuyenMai();
                found = true;
                break;
            }
        }

        if (!found) {
            discount = 0.0;
            maKhuyenMai = "Không có";
        }
    }


    private void updateTotal() {
        tongTienUI = getTotal();
        // Định dạng tiền tệ
        DecimalFormat df = new DecimalFormat("#,###.##");
        // Cập nhật vào label txtTotal
        txtTotal.setText(df.format(tongTienUI) + "đ");
    }
    public class SetQuantityController {
        @FXML
        private TextField txtQuantity;

        private int currentQuantity;

        public void setCurrentQuantity(int quantity) {
            this.currentQuantity = quantity;
            txtQuantity.setText(String.valueOf(quantity)); // Hiển thị số lượng hiện tại
        }

        public int getQuantity() {
            try {
                return Integer.parseInt(txtQuantity.getText()); // Trả về số lượng mới
            } catch (NumberFormatException e) {
                return currentQuantity; // Nếu nhập không hợp lệ, trả về số lượng cũ
            }
        }
    }
}
