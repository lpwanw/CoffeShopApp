package com.example.android_demo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android_demo.entity.PhieuPhucVu;
import java.util.Date;
import java.util.List;

@Dao
public interface PhieuPhucVuDao {
    
    @Query("SELECT * FROM phieu_phuc_vu ORDER BY thoiGianBatDau DESC")
    List<PhieuPhucVu> layTatCaPhieuPhucVu();
    
    @Query("SELECT * FROM phieu_phuc_vu WHERE id = :id")
    PhieuPhucVu layPhieuPhucVuTheoId(int id);
    
    @Query("SELECT * FROM phieu_phuc_vu WHERE banId = :banId ORDER BY thoiGianBatDau DESC")
    List<PhieuPhucVu> layPhieuPhucVuTheoBan(int banId);
    
    @Query("SELECT * FROM phieu_phuc_vu WHERE banId = :banId AND trangThai = 'DANG_PHUC_VU'")
    PhieuPhucVu layPhieuPhucVuHienTaiCuaBan(int banId);
    
    @Query("SELECT * FROM phieu_phuc_vu WHERE nguoiDungId = :nguoiDungId ORDER BY thoiGianBatDau DESC")
    List<PhieuPhucVu> layPhieuPhucVuTheoNguoiDung(int nguoiDungId);
    
    @Query("SELECT * FROM phieu_phuc_vu WHERE trangThai = :trangThai ORDER BY thoiGianBatDau DESC")
    List<PhieuPhucVu> layPhieuPhucVuTheoTrangThai(String trangThai);
    
    @Query("SELECT * FROM phieu_phuc_vu WHERE DATE(thoiGianBatDau) = DATE(:ngay) ORDER BY thoiGianBatDau DESC")
    List<PhieuPhucVu> layPhieuPhucVuTheoNgay(Date ngay);
    
    @Query("SELECT SUM(tongTien) FROM phieu_phuc_vu WHERE trangThai = 'HOAN_THANH' AND DATE(thoiGianBatDau) = DATE(:ngay)")
    Double tinhTongDoanhThuTheoNgay(Date ngay);
    
    @Query("SELECT AVG(tongTien) FROM phieu_phuc_vu WHERE trangThai = 'HOAN_THANH' AND banId = :banId")
    Double tinhTrungBinhDoanhThuTheoBan(int banId);
    
    @Query("UPDATE phieu_phuc_vu SET trangThai = :trangThai WHERE id = :id")
    void capNhatTrangThaiPhieuPhucVu(int id, String trangThai);
    
    @Query("UPDATE phieu_phuc_vu SET tongTien = :tongTien WHERE id = :id")
    void capNhatTongTienPhieuPhucVu(int id, double tongTien);
    
    @Query("UPDATE phieu_phuc_vu SET thoiGianKetThuc = :thoiGianKetThuc, trangThai = 'HOAN_THANH' WHERE id = :id")
    void dongPhieuPhucVu(int id, Date thoiGianKetThuc);
    
    @Insert
    long themPhieuPhucVu(PhieuPhucVu phieuPhucVu);
    
    @Update
    void capNhatPhieuPhucVu(PhieuPhucVu phieuPhucVu);
    
    @Delete
    void xoaPhieuPhucVu(PhieuPhucVu phieuPhucVu);
    
    @Query("DELETE FROM phieu_phuc_vu WHERE id = :id")
    void xoaPhieuPhucVuTheoId(int id);
    
    @Query("UPDATE phieu_phuc_vu SET thoiGianKetThuc = datetime('now'), trangThai = 'HOAN_THANH' WHERE trangThai = 'DANG_PHUC_VU'")
    void dongTatCaPhieuPhucVuDangMo();
}