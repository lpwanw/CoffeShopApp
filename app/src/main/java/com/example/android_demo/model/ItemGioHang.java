package com.example.android_demo.model;

import com.example.android_demo.entity.SanPham;

public class ItemGioHang {
    private SanPham sanPham;
    private int soLuong;
    
    public ItemGioHang(SanPham sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }
    
    public SanPham getSanPham() {
        return sanPham;
    }
    
    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    public double tinhThanhTien() {
        return sanPham.getGia() * soLuong;
    }
}