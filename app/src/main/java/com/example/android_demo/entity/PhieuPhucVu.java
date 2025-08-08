package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "phieu_phuc_vu",
        foreignKeys = {
                @ForeignKey(entity = Ban.class,
                        parentColumns = "id",
                        childColumns = "banId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = NguoiDung.class,
                        parentColumns = "id",
                        childColumns = "nguoiDungId",
                        onDelete = ForeignKey.CASCADE)
        })
public class PhieuPhucVu {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int banId;
    private int nguoiDungId;
    private Date thoiGianBatDau;
    private Date thoiGianKetThuc;
    private double tongTien;
    private String trangThai; // "DANG_PHUC_VU", "HOAN_THANH", "HUY"
    private String ghiChu;
    private int soKhach;

    public PhieuPhucVu() {}

    public PhieuPhucVu(int banId, int nguoiDungId, Date thoiGianBatDau, String trangThai, int soKhach) {
        this.banId = banId;
        this.nguoiDungId = nguoiDungId;
        this.thoiGianBatDau = thoiGianBatDau;
        this.trangThai = trangThai;
        this.soKhach = soKhach;
        this.tongTien = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBanId() {
        return banId;
    }

    public void setBanId(int banId) {
        this.banId = banId;
    }

    public int getNguoiDungId() {
        return nguoiDungId;
    }

    public void setNguoiDungId(int nguoiDungId) {
        this.nguoiDungId = nguoiDungId;
    }

    public Date getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(Date thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public Date getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(Date thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getSoKhach() {
        return soKhach;
    }

    public void setSoKhach(int soKhach) {
        this.soKhach = soKhach;
    }
}