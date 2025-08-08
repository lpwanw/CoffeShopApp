package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ban")
public class Ban {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int soBan;
    private String viTri;
    private int soChoNgoi;
    private String trangThai; // "TRONG", "DANG_PHUC_VU", "CAN_DON_DEP"
    private String moTa;

    public Ban() {}

    public Ban(int soBan, String viTri, int soChoNgoi, String trangThai, String moTa) {
        this.soBan = soBan;
        this.viTri = viTri;
        this.soChoNgoi = soChoNgoi;
        this.trangThai = trangThai;
        this.moTa = moTa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoBan() {
        return soBan;
    }

    public void setSoBan(int soBan) {
        this.soBan = soBan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public int getSoChoNgoi() {
        return soChoNgoi;
    }

    public void setSoChoNgoi(int soChoNgoi) {
        this.soChoNgoi = soChoNgoi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}