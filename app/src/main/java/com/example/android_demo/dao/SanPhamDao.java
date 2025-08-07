package com.example.android_demo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android_demo.entity.SanPham;
import java.util.List;

@Dao
public interface SanPhamDao {
    
    @Query("SELECT * FROM san_pham")
    List<SanPham> layTatCaSanPham();
    
    @Query("SELECT * FROM san_pham WHERE id = :id")
    SanPham laySanPhamTheoId(int id);
    
    @Query("SELECT * FROM san_pham WHERE danhMucId = :danhMucId")
    List<SanPham> laySanPhamTheoDanhMuc(int danhMucId);
    
    @Query("SELECT * FROM san_pham WHERE tenSanPham LIKE :tenSanPham")
    List<SanPham> timSanPhamTheoTen(String tenSanPham);
    
    @Query("SELECT * FROM san_pham WHERE conHang = 1")
    List<SanPham> laySanPhamConHang();
    
    @Query("UPDATE san_pham SET conHang = :conHang WHERE id = :id")
    void capNhatTinhTrangSanPham(int id, boolean conHang);
    
    @Insert
    long themSanPham(SanPham sanPham);
    
    @Update
    void capNhatSanPham(SanPham sanPham);
    
    @Delete
    void xoaSanPham(SanPham sanPham);
    
    @Query("DELETE FROM san_pham WHERE id = :id")
    void xoaSanPhamTheoId(int id);
}