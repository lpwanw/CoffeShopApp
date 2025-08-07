package com.example.android_demo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android_demo.entity.ChiTietDonHang;
import java.util.List;

@Dao
public interface ChiTietDonHangDao {
    
    @Query("SELECT * FROM chi_tiet_don_hang WHERE donHangId = :donHangId")
    List<ChiTietDonHang> layChiTietTheoDonHang(int donHangId);
    
    @Query("SELECT * FROM chi_tiet_don_hang WHERE id = :id")
    ChiTietDonHang layChiTietTheoId(int id);
    
    @Query("SELECT ct.*, sp.tenSanPham " +
           "FROM chi_tiet_don_hang ct " +
           "INNER JOIN san_pham sp ON ct.sanPhamId = sp.id " +
           "WHERE ct.donHangId = :donHangId")
    List<Object> layChiTietDonHangVoiTenSanPham(int donHangId);
    
    @Query("SELECT SUM(soLuong * giaBan) FROM chi_tiet_don_hang WHERE donHangId = :donHangId")
    Double tinhTongTienDonHang(int donHangId);
    
    @Insert
    long themChiTietDonHang(ChiTietDonHang chiTiet);
    
    @Insert
    void themDanhSachChiTiet(List<ChiTietDonHang> danhSachChiTiet);
    
    @Update
    void capNhatChiTietDonHang(ChiTietDonHang chiTiet);
    
    @Delete
    void xoaChiTietDonHang(ChiTietDonHang chiTiet);
    
    @Query("DELETE FROM chi_tiet_don_hang WHERE donHangId = :donHangId")
    void xoaChiTietTheoDonHang(int donHangId);
}