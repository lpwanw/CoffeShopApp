package com.example.android_demo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android_demo.entity.NguoiDung;
import java.util.List;

@Dao
public interface NguoiDungDao {
    
    @Query("SELECT * FROM nguoi_dung")
    List<NguoiDung> layTatCaNguoiDung();
    
    @Query("SELECT * FROM nguoi_dung WHERE id = :id")
    NguoiDung layNguoiDungTheoId(int id);
    
    @Query("SELECT * FROM nguoi_dung WHERE tenDangNhap = :tenDangNhap AND matKhau = :matKhau")
    NguoiDung dangNhap(String tenDangNhap, String matKhau);
    
    @Query("SELECT * FROM nguoi_dung WHERE tenDangNhap = :tenDangNhap")
    NguoiDung kiemTraTenDangNhap(String tenDangNhap);
    
    @Query("SELECT * FROM nguoi_dung WHERE vaiTro = :vaiTro")
    List<NguoiDung> layNguoiDungTheoVaiTro(String vaiTro);
    
    @Insert
    long themNguoiDung(NguoiDung nguoiDung);
    
    @Update
    void capNhatNguoiDung(NguoiDung nguoiDung);
    
    @Delete
    void xoaNguoiDung(NguoiDung nguoiDung);
    
    @Query("DELETE FROM nguoi_dung WHERE id = :id")
    void xoaNguoiDungTheoId(int id);
}