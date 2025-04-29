package com.damcafe.app.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Ban {
    private final String maBan;
    private boolean isUse;
    private LocalDate start;
    private LocalDate end;
    private int soGhe;

    public Ban(String maBan) {
        this.maBan = maBan;
        isUse = false;
    }

    public Ban(String maBan, boolean isUse, LocalDate start, LocalDate end, int soGhe) {
        this.maBan = maBan;
        this.isUse = isUse;
        this.start = start;
        this.end = end;
        this.soGhe = soGhe;
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

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
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
