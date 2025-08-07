package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "chi_tiet_don_hang",
        foreignKeys = {
                @ForeignKey(entity = DonHang.class,
                        parentColumns = "id",
                        childColumns = "donHangId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = SanPham.class,
                        parentColumns = "id",
                        childColumns = "sanPhamId",
                        onDelete = ForeignKey.CASCADE)
        })
public class ChiTietDonHang {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int donHangId;
    private int sanPhamId;
    private int soLuong;
    private double giaBan;

    public ChiTietDonHang() {}

    public ChiTietDonHang(int donHangId, int sanPhamId, int soLuong, double giaBan) {
        this.donHangId = donHangId;
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonHangId() {
        return donHangId;
    }

    public void setDonHangId(int donHangId) {
        this.donHangId = donHangId;
    }

    public int getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(int sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }
}