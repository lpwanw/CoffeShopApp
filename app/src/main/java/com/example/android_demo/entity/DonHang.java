package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "don_hang",
        foreignKeys = @ForeignKey(entity = NguoiDung.class,
                parentColumns = "id",
                childColumns = "nguoiDungId",
                onDelete = ForeignKey.CASCADE))
public class DonHang {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int nguoiDungId;
    private Date thoiGianDat;
    private double tongTien;
    private String trangThai; // "DANG_CHO", "HOAN_THANH", "HUY"
    private String ghiChu;

    public DonHang() {}

    public DonHang(int nguoiDungId, Date thoiGianDat, double tongTien, String trangThai) {
        this.nguoiDungId = nguoiDungId;
        this.thoiGianDat = thoiGianDat;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNguoiDungId() {
        return nguoiDungId;
    }

    public void setNguoiDungId(int nguoiDungId) {
        this.nguoiDungId = nguoiDungId;
    }

    public Date getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(Date thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
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
}