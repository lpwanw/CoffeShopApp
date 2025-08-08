package com.example.android_demo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android_demo.entity.DonHang;
import java.util.Date;
import java.util.List;

@Dao
public interface DonHangDao {
    
    @Query("SELECT * FROM don_hang ORDER BY thoiGianDat DESC")
    List<DonHang> layTatCaDonHang();
    
    @Query("SELECT * FROM don_hang WHERE id = :id")
    DonHang layDonHangTheoId(int id);
    
    @Query("SELECT * FROM don_hang WHERE nguoiDungId = :nguoiDungId ORDER BY thoiGianDat DESC")
    List<DonHang> layDonHangTheoNguoiDung(int nguoiDungId);
    
    @Query("SELECT * FROM don_hang WHERE trangThai = :trangThai ORDER BY thoiGianDat DESC")
    List<DonHang> layDonHangTheoTrangThai(String trangThai);
    
    @Query("SELECT * FROM don_hang WHERE banId = :banId ORDER BY thoiGianDat DESC")
    List<DonHang> layDonHangTheoBan(int banId);
    
    @Query("SELECT * FROM don_hang WHERE phieuPhucVuId = :phieuPhucVuId ORDER BY thoiGianDat DESC")
    List<DonHang> layDonHangTheoPhieuPhucVu(int phieuPhucVuId);
    
    @Query("SELECT * FROM don_hang WHERE banId = :banId AND trangThai IN ('DANG_CHO', 'DANG_CHE_BIEN') ORDER BY thoiGianDat ASC")
    List<DonHang> layDonHangDangXuLyTheoBan(int banId);
    
    @Query("SELECT * FROM don_hang WHERE DATE(thoiGianDat) = DATE(:ngay) ORDER BY thoiGianDat DESC")
    List<DonHang> layDonHangTheoNgay(Date ngay);
    
    @Query("SELECT SUM(tongTien) FROM don_hang WHERE trangThai = 'HOAN_THANH' AND DATE(thoiGianDat) = DATE(:ngay)")
    Double tinhTongDoanhThuTheoNgay(Date ngay);
    
    @Query("SELECT SUM(tongTien) FROM don_hang WHERE banId = :banId AND trangThai = 'HOAN_THANH'")
    Double tinhTongDoanhThuTheoBan(int banId);
    
    @Query("UPDATE don_hang SET trangThai = :trangThai WHERE id = :id")
    void capNhatTrangThaiDonHang(int id, String trangThai);
    
    @Insert
    long themDonHang(DonHang donHang);
    
    @Update
    void capNhatDonHang(DonHang donHang);
    
    @Delete
    void xoaDonHang(DonHang donHang);
    
    @Query("DELETE FROM don_hang WHERE id = :id")
    void xoaDonHangTheoId(int id);
    
    @Query("UPDATE don_hang SET tongTien = tongTien + :giaThem WHERE id = :id")
    void capNhatTongTienDonHang(int id, double giaThem);
    
    @Query("UPDATE don_hang SET tongTien = (SELECT SUM(soLuong * giaBan) FROM chi_tiet_don_hang WHERE donHangId = :id) WHERE id = :id")
    void tinhLaiTongTienDonHang(int id);
}