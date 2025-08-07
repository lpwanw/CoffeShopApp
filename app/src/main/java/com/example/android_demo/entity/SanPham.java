package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "san_pham",
        foreignKeys = @ForeignKey(entity = DanhMucSanPham.class,
                parentColumns = "id",
                childColumns = "danhMucId",
                onDelete = ForeignKey.CASCADE))
public class SanPham {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String tenSanPham;
    private double gia;
    private int danhMucId;
    private String hinhAnh;
    private String moTa;
    private boolean conHang;

    public SanPham() {}

    public SanPham(String tenSanPham, double gia, int danhMucId, String hinhAnh, String moTa) {
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.danhMucId = danhMucId;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.conHang = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getDanhMucId() {
        return danhMucId;
    }

    public void setDanhMucId(int danhMucId) {
        this.danhMucId = danhMucId;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isConHang() {
        return conHang;
    }

    public void setConHang(boolean conHang) {
        this.conHang = conHang;
    }
}