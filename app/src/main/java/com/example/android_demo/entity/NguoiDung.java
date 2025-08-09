package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "nguoi_dung", 
        indices = {@Index(value = {"tenDangNhap"}, unique = true)})
public class NguoiDung {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro; // "ADMIN" hoặc "NHAN_VIEN"
    private String hoTen;
    private String email;

    public NguoiDung() {}

    public NguoiDung(String tenDangNhap, String matKhau, String vaiTro, String hoTen, String email) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.hoTen = hoTen;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}