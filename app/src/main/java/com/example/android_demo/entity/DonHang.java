package com.example.android_demo.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "don_hang",
        foreignKeys = {
                @ForeignKey(entity = NguoiDung.class,
                        parentColumns = "id",
                        childColumns = "nguoiDungId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Ban.class,
                        parentColumns = "id",
                        childColumns = "banId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = PhieuPhucVu.class,
                        parentColumns = "id",
                        childColumns = "phieuPhucVuId",
                        onDelete = ForeignKey.CASCADE)
        })
public class DonHang {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int nguoiDungId;
    private Integer banId; // Nullable for take-away orders
    private Integer phieuPhucVuId; // Nullable for orders without service ticket
    private Date thoiGianDat;
    private double tongTien;
    private String trangThai; // "DANG_CHO", "DANG_CHE_BIEN", "HOAN_THANH", "HUY"
    private String ghiChu;

    public DonHang() {}

    public DonHang(int nguoiDungId, Integer banId, Integer phieuPhucVuId, Date thoiGianDat, double tongTien, String trangThai) {
        this.nguoiDungId = nguoiDungId;
        this.banId = banId;
        this.phieuPhucVuId = phieuPhucVuId;
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

    public Integer getBanId() {
        return banId;
    }

    public void setBanId(Integer banId) {
        this.banId = banId;
    }

    public Integer getPhieuPhucVuId() {
        return phieuPhucVuId;
    }

    public void setPhieuPhucVuId(Integer phieuPhucVuId) {
        this.phieuPhucVuId = phieuPhucVuId;
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
