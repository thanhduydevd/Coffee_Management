package com.damcafe.app.gui;

import com.damcafe.app.entity.OrderDetail;
import com.damcafe.app.entity.Size;

public class KiemNghiem {
    public static void main(String[] args) {
        OrderDetail d1 = new OrderDetail("DT001", "ORD001", "PRD001", Size.M, 2, 45000, "Không đá");
        OrderDetail d2 = new OrderDetail("DT002", "ORD001", "PRD002", Size.L, 1, 55000, "Ít đường");
        OrderDetail d3 = new OrderDetail("DT003", "ORD002", "PRD003", Size.S, 3, 30000, "");
        OrderDetail d4 = new OrderDetail("DT004", "ORD003", "PRD004", Size.L, 1, 60000, "Thêm sữa");
        OrderDetail d5 = new OrderDetail("DT005", "ORD004", "PRD005", Size.M, 2, 40000, "Mang về");

        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d3);
        System.out.println(d4);
        System.out.println(d5);
    }
}
