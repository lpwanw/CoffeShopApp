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
    
    @Query("SELECT ct.* " +
           "FROM chi_tiet_don_hang ct " +
           "WHERE ct.donHangId = :donHangId")
    List<ChiTietDonHang> layChiTietDonHangVoiTenSanPham(int donHangId);
    
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
    
    @Query("SELECT ct.* FROM chi_tiet_don_hang ct " +
           "INNER JOIN don_hang dh ON ct.donHangId = dh.id " +
           "WHERE dh.phieuPhucVuId = :phieuPhucVuId AND ct.sanPhamId = :sanPhamId " +
           "AND dh.trangThai != 'HUY' LIMIT 1")
    ChiTietDonHang layChiTietTheoSanPhamVaPhieuPhucVu(int phieuPhucVuId, int sanPhamId);
    
    @Query("UPDATE chi_tiet_don_hang SET soLuong = soLuong + :soLuongThem WHERE id = :id")
    void tangSoLuongChiTiet(int id, int soLuongThem);
    
    @Query("SELECT donHangId FROM chi_tiet_don_hang WHERE id = :chiTietId")
    int layDonHangIdTuChiTiet(int chiTietId);
    
    @Query("SELECT sp.id as sanPhamId, sp.tenSanPham, sp.gia, SUM(ct.soLuong) as tongSoLuong, " +
           "SUM(ct.soLuong * ct.giaBan) as tongTien, " +
           "CASE WHEN COUNT(DISTINCT dh.trangThai) = 1 THEN MAX(dh.trangThai) ELSE 'MIXED' END as trangThai " +
           "FROM chi_tiet_don_hang ct " +
           "INNER JOIN don_hang dh ON ct.donHangId = dh.id " +
           "INNER JOIN san_pham sp ON ct.sanPhamId = sp.id " +
           "WHERE dh.phieuPhucVuId = :phieuPhucVuId AND dh.trangThai != 'HUY' " +
           "GROUP BY sp.id, sp.tenSanPham, sp.gia " +
           "ORDER BY MIN(dh.thoiGianDat)")
    List<GroupedOrderItem> layDonHangNhomTheoSanPham(int phieuPhucVuId);
    
    public static class GroupedOrderItem {
        public int sanPhamId;
        public String tenSanPham;
        public double gia;
        public int tongSoLuong;
        public double tongTien;
        public String trangThai;
    }
}