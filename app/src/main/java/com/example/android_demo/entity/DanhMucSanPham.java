package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "danh_muc_san_pham")
public class DanhMucSanPham {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String tenDanhMuc;
    private String moTa;

    public DanhMucSanPham() {}

    public DanhMucSanPham(String tenDanhMuc, String moTa) {
        this.tenDanhMuc = tenDanhMuc;
        this.moTa = moTa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}