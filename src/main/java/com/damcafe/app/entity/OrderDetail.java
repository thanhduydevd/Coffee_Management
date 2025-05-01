package com.damcafe.app.entity;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 4/29/2025
 * @version: 1.0
 */

import java.util.Objects;

public class OrderDetail {

    private final String detailID;
    private String orderID;
    private String productID;
    private Size size;
    private int quatity;
    private double price;
    private double total;
    private String comment;
    private String name;
    private int stt;

    public OrderDetail(String detailID, String productID, Size size, int quatity, double price, String comment, String name, int stt) {
        this.detailID = detailID;
        this.productID = productID;
        this.size = size;
        this.quatity = quatity;
        this.price = price;
        this.comment = comment;
        this.name = name;
        this.stt = stt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public OrderDetail(String detailID, String orderID, String productID, Size size, int quatity, double price, String comment) {
        this.detailID = detailID;
        this.orderID = orderID;
        this.productID = productID;
        this.size = size;
        this.quatity = quatity;
        this.price = price;
        this.total = getTotal();
        this.comment = comment;
    }

    public OrderDetail(String detailID, String orderID, String productID, Size size, int quatity, double price, String comment,String name) {
        this.detailID = detailID;
        this.orderID = orderID;
        this.productID = productID;
        this.size = size;
        this.quatity = quatity;
        this.price = price;
        this.total = getTotal();
        this.comment = comment;
        this.name = name;
    }

    public OrderDetail(String id, Size value, int i1, String s, double giaGoc) {
        detailID = id;
        size = value;
        quatity = i1;
        comment = s;
        price = giaGoc;
        total = getTotal();
    }

    public OrderDetail(String detailID) {
        this.detailID = detailID;
        size = Size.S;
        total = getTotal();
    }

    public String getDetailID() {
        return detailID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return price*quatity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(detailID, that.detailID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(detailID);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s %s %s",detailID,orderID,productID,size,quatity,price,total,comment);
    }
}
