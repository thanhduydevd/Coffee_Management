package com.damcafe.app.entity;

import java.util.ArrayList;

public class Tang {
    private final String maTang;
    private boolean isFull;
    private ArrayList<Ban> dsBan;

    public Tang(String maTang) {
        this.maTang = maTang;
        dsBan = new ArrayList<>();
        isFull = false;
    }

    public Tang(String maTang, ArrayList<Ban> dsBan) {
        this.maTang = maTang;
        this.isFull = false;
        this.dsBan = dsBan;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean addBan(Ban e){
        if (dsBan.contains(e)){
            return false;
        }else{
            dsBan.add(e);
            return true;
        }
    }

    public boolean delBanVoiMa(String e){
        for (int i = 0; i < dsBan.size(); i++) {
            if (dsBan.get(i).getMaBan().equals(e)){
                dsBan.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean updateBan(Ban e){
        for (int i = 0; i < dsBan.size(); i++) {
            if (dsBan.get(i).getMaBan().equals(e.getMaBan())){
                dsBan.add(i,e);
                return true;
            }
        }
        return false;
    }
}
