package com.example.android_demo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android_demo.entity.DanhMucSanPham;
import java.util.List;

@Dao
public interface DanhMucSanPhamDao {
    
    @Query("SELECT * FROM danh_muc_san_pham")
    List<DanhMucSanPham> layTatCaDanhMuc();
    
    @Query("SELECT * FROM danh_muc_san_pham WHERE id = :id")
    DanhMucSanPham layDanhMucTheoId(int id);
    
    @Query("SELECT * FROM danh_muc_san_pham WHERE tenDanhMuc LIKE :tenDanhMuc")
    List<DanhMucSanPham> timDanhMucTheoTen(String tenDanhMuc);
    
    @Insert
    long themDanhMuc(DanhMucSanPham danhMuc);
    
    @Update
    void capNhatDanhMuc(DanhMucSanPham danhMuc);
    
    @Delete
    void xoaDanhMuc(DanhMucSanPham danhMuc);
    
    @Query("DELETE FROM danh_muc_san_pham WHERE id = :id")
    void xoaDanhMucTheoId(int id);
}