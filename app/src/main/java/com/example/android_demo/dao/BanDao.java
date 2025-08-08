package com.example.android_demo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.android_demo.entity.Ban;
import java.util.List;

@Dao
public interface BanDao {
    
    @Query("SELECT * FROM ban ORDER BY soBan ASC")
    List<Ban> layTatCaBan();
    
    @Query("SELECT * FROM ban WHERE id = :id")
    Ban layBanTheoId(int id);
    
    @Query("SELECT * FROM ban WHERE soBan = :soBan")
    Ban layBanTheoSoBan(int soBan);
    
    @Query("SELECT * FROM ban WHERE trangThai = :trangThai ORDER BY soBan ASC")
    List<Ban> layBanTheoTrangThai(String trangThai);
    
    @Query("SELECT * FROM ban WHERE viTri LIKE :viTri ORDER BY soBan ASC")
    List<Ban> layBanTheoViTri(String viTri);
    
    @Query("SELECT * FROM ban WHERE soChoNgoi >= :soChoNgoi AND trangThai = 'TRONG' ORDER BY soBan ASC")
    List<Ban> layBanTrongTheoSoChoNgoi(int soChoNgoi);
    
    @Query("UPDATE ban SET trangThai = :trangThai WHERE id = :id")
    void capNhatTrangThaiBan(int id, String trangThai);
    
    @Query("SELECT COUNT(*) FROM ban WHERE trangThai = :trangThai")
    int demBanTheoTrangThai(String trangThai);
    
    @Insert
    long themBan(Ban ban);
    
    @Update
    void capNhatBan(Ban ban);
    
    @Delete
    void xoaBan(Ban ban);
    
    @Query("DELETE FROM ban WHERE id = :id")
    void xoaBan(int id);
    
    @Query("SELECT COUNT(*) FROM ban WHERE soBan = :soBan")
    int kiemTraBanTonTai(int soBan);
    
    @Query("UPDATE ban SET soBan = :soBan, viTri = :viTri, soChoNgoi = :soChoNgoi, moTa = :moTa WHERE id = :id")
    void capNhatBan(int id, int soBan, String viTri, int soChoNgoi, String moTa);
    
    @Query("UPDATE ban SET trangThai = 'TRONG' WHERE trangThai = 'CAN_DON_DEP'")
    void capNhatTatCaBanCanDonDepThanhTrong();
    
    @Query("UPDATE ban SET trangThai = 'TRONG'")
    void capNhatTatCaBanVeTrong();
}