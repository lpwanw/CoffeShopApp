package com.example.android_demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.NguoiDung;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class DangNhapActivity extends AppCompatActivity {
    
    private TextInputEditText etTenDangNhap;
    private TextInputEditText etMatKhau;
    private MaterialButton btnDangNhap;
    private QuanCaPheDatabase database;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        
        khoiTaoView();
        khoiTaoDatabase();
        kiemTraDangNhap();
        suKienClick();
    }
    
    private void khoiTaoView() {
        etTenDangNhap = findViewById(R.id.etTenDangNhap);
        etMatKhau = findViewById(R.id.etMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(this);
        sharedPreferences = getSharedPreferences("QuanCaPhe", MODE_PRIVATE);
    }
    
    private void kiemTraDangNhap() {
        // Kiểm tra nếu đã đăng nhập trước đó
        int nguoiDungId = sharedPreferences.getInt("nguoi_dung_id", -1);
        if (nguoiDungId != -1) {
            chuyenDenTrangChu();
        }
    }
    
    private void suKienClick() {
        btnDangNhap.setOnClickListener(v -> xuLyDangNhap());
    }
    
    private void xuLyDangNhap() {
        String tenDangNhap = etTenDangNhap.getText().toString().trim();
        String matKhau = etMatKhau.getText().toString().trim();
        
        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Kiểm tra đăng nhập trong database
        NguoiDung nguoiDung = database.nguoiDungDao().dangNhap(tenDangNhap, matKhau);
        
        if (nguoiDung != null) {
            // Lưu thông tin đăng nhập
            luuThongTinDangNhap(nguoiDung);
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            chuyenDenTrangChu();
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void luuThongTinDangNhap(NguoiDung nguoiDung) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("nguoi_dung_id", nguoiDung.getId());
        editor.putString("ten_dang_nhap", nguoiDung.getTenDangNhap());
        editor.putString("ho_ten", nguoiDung.getHoTen());
        editor.putString("vai_tro", nguoiDung.getVaiTro());
        editor.apply();
    }
    
    private void chuyenDenTrangChu() {
        Intent intent = new Intent(this, TrangChuActivity.class);
        startActivity(intent);
        finish();
    }
    
}