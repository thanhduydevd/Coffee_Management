package com.damcafe.app.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Ban {
    private final String maBan;
    private boolean isUse;
    private Tang tang;
    private String maOrder;

    public Ban(String maBan) {
        this.maBan = maBan;
        isUse = false;
    }

    public Ban(String maBan, boolean isUse, Tang tang, String maOrder) {
        this.maBan = maBan;
        this.isUse = isUse;
        this.tang = tang;
        this.maOrder = maOrder;
    }

    public String getMaBan() {
        return maBan;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    public Tang getTang() {
        return tang;
    }

    public void setTang(Tang tang) {
        this.tang = tang;
    }

    public String getMaOrder() {
        return maOrder;
    }

    public void setMaOrder(String maOrder) {
        this.maOrder = maOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ban ban = (Ban) o;
        return Objects.equals(maBan, ban.maBan);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maBan);
    }
}
