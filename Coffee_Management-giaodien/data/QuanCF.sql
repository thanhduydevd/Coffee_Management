-- Tạo database
CREATE DATABASE QuanCafe;
GO

-- Sử dụng database
USE QuanCafe;
GO

-- Bảng loại khách hàng (enum)
CREATE TABLE LoaiKhachHang (
    loaiKhachHang NVARCHAR(50) PRIMARY KEY
);

-- Bảng khách hàng
CREATE TABLE KhachHang (
    maKhachHang VARCHAR(20) PRIMARY KEY,
    loaiKhachHang NVARCHAR(50),
    hoVaTen NVARCHAR(100),
    diaChi NVARCHAR(200),
    soDienThoai VARCHAR(15),
    diemTichLuy FLOAT,
    FOREIGN KEY (loaiKhachHang) REFERENCES LoaiKhachHang(loaiKhachHang)
);

-- Bảng nhân viên
CREATE TABLE NhanVien (
    maNhanVien VARCHAR(20) PRIMARY KEY,
    tenNhanVien NVARCHAR(100),
    ngayVaoLam DATE,
    luong FLOAT,
	email NVARCHAR(100)
);

-- Bảng tầng
CREATE TABLE Tang (
    maTang VARCHAR(20) PRIMARY KEY,
    trangThai BIT
);

-- Bảng bàn
CREATE TABLE Ban (
    maBan VARCHAR(20) PRIMARY KEY,
    gioHoatDong TIME,
    gioNghi TIME,
    soGhe INT,
    viTri NVARCHAR(100),
    trangThai BIT,
    maTang VARCHAR(20),
    maNhanVien VARCHAR(20), -- Bàn do nhân viên nào quản lý
    FOREIGN KEY (maTang) REFERENCES Tang(maTang),
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

-- Bảng loại sản phẩm (enum)
CREATE TABLE LoaiSanPham (
    loaiSanPham NVARCHAR(50) PRIMARY KEY
);

-- Bảng sản phẩm
CREATE TABLE SanPham (
    maSanPham VARCHAR(20) PRIMARY KEY,
    tenSanPham NVARCHAR(100),
    giaGoc FLOAT,
    isBanChay BIT,
    soLuong INT,
    loaiSanPham NVARCHAR(50),
	hinhAnh NVARCHAR(100),
    FOREIGN KEY (loaiSanPham) REFERENCES LoaiSanPham(loaiSanPham)
);

-- Bảng khuyến mãi
CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(20) PRIMARY KEY,
    discount FLOAT
);

-- Bảng hóa đơn
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    khachHang VARCHAR(20),
    maBan VARCHAR(20), -- Thêm liên kết hóa đơn với bàn
    ngayTao DATE,
    isMangVe BIT,
    maNhanVien VARCHAR(20),
    ghiChu NVARCHAR(255),
    FOREIGN KEY (khachHang) REFERENCES KhachHang(maKhachHang),
    FOREIGN KEY (maBan) REFERENCES Ban(maBan),
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

-- Bảng chi tiết hóa đơn
CREATE TABLE ChiTietHoaDon (
    maChiTiet VARCHAR(20) PRIMARY KEY,
    maHoaDon VARCHAR(20),
    maSanPham VARCHAR(20),
    soLuong INT,
    thanhTien FLOAT,
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
);

CREATE TABLE KhuyenMai_SanPham (
    maKhuyenMai VARCHAR(20),
    maSanPham VARCHAR(20),
    ngayTao DATE,
    PRIMARY KEY (maKhuyenMai, maSanPham),
    FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai),
    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
);

-- Bảng tài khoản (chỉ dành cho nhân viên)
CREATE TABLE TaiKhoan (
    maTaiKhoan VARCHAR(20) PRIMARY KEY,
    maNhanVien VARCHAR(20) UNIQUE, 
    tenTaiKhoan NVARCHAR(50) UNIQUE,
    matKhau NVARCHAR(255),
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);
