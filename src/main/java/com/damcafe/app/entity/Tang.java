package com.damcafe.app.entity;

import java.util.ArrayList;
import java.util.Objects;

public class Tang {
    private final String maTang;
    private String tenTang;


    public Tang(String maTang) {
        this.maTang = maTang;
    }

    public Tang(String maTang, String tenTang) {
        this.maTang = maTang;
        this.tenTang = tenTang;
    }

    public String getMaTang() {
        return maTang;
    }

    public String getTenTang() {
        return tenTang;
    }

    public void setTenTang(String tenTang) {
        this.tenTang = tenTang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tang tang = (Tang) o;
        return Objects.equals(maTang, tang.maTang);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maTang);
    }
}
