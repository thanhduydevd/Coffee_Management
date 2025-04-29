package com.damcafe.app.entity;

/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 4/29/2025
 * @version: 1.0
 */

public enum Size {
    Medium("Medium"),
    Large("Large"),
    Small("Small");

    private String size;

    Size(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
