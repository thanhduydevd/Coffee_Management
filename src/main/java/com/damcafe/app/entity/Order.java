package com.damcafe.app.entity;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Order {
    public final double TAX = 0.05;
    private final String OrderID;
    private LocalDate date;
    private String userID;
    private boolean isBringBack;
    private String tableID;
    private String saleID;

    public Order(String orderID, String userID, boolean isBringBack, String tableID, String saleID) {
        OrderID = orderID;
        this.date = LocalDate.now();
        this.userID = userID;
        this.isBringBack = isBringBack;
        this.tableID = tableID;
        this.saleID = saleID;

    }

    public Order(String orderID, LocalDate date, String userID, boolean isBringBack, String tableID, String saleID) {
        OrderID = orderID;
        this.date = date;
        this.userID = userID;
        this.isBringBack = isBringBack;
        this.tableID = tableID;
        this.saleID = saleID;
    }

    public Order(String orderID) {
        OrderID = orderID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isBringBack() {
        return isBringBack;
    }

    public void setBringBack(boolean bringBack) {
        isBringBack = bringBack;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public String getSaleID() {
        return saleID;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(OrderID, order.OrderID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(OrderID);
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("%s %s %s %s %s %s",getOrderID(),df.format(date),userID,isBringBack,tableID,saleID);
    }
}
