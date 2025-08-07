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
    
    @Query("SELECT * FROM don_hang WHERE DATE(thoiGianDat) = DATE(:ngay) ORDER BY thoiGianDat DESC")
    List<DonHang> layDonHangTheoNgay(Date ngay);
    
    @Query("SELECT SUM(tongTien) FROM don_hang WHERE trangThai = 'HOAN_THANH' AND DATE(thoiGianDat) = DATE(:ngay)")
    Double tinhTongDoanhThuTheoNgay(Date ngay);
    
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
}