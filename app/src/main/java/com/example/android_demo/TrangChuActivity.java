package com.example.android_demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.activity.OnBackPressedCallback;
import com.example.android_demo.fragment.DatHangFragment;
import com.example.android_demo.fragment.LichSuDonHangFragment;
import com.example.android_demo.fragment.QuanLySanPhamFragment;
import com.example.android_demo.fragment.QuanLyNguoiDungFragment;
import com.example.android_demo.fragment.SoDoBanFragment;
import com.example.android_demo.fragment.QuanLyBanFragment;
import com.example.android_demo.fragment.BaoCaoFragment;
import com.google.android.material.navigation.NavigationView;

public class TrangChuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView tvHoTen, tvVaiTro;
    private SharedPreferences sharedPreferences;
    private String vaiTroHienTai;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        
        khoiTaoView();
        khoiTaoToolbar();
        khoiTaoNavigationDrawer();
        layThongTinNguoiDung();
        caiDatMenu();
        hienThiFragmentMacDinh();
        caiDatXuLyBackPress();
    }
    
    private void khoiTaoView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        
        // Header navigation view
        tvHoTen = navigationView.getHeaderView(0).findViewById(R.id.tvHoTen);
        tvVaiTro = navigationView.getHeaderView(0).findViewById(R.id.tvVaiTro);
    }
    
    private void khoiTaoToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quán Cà Phê");
        }
    }
    
    private void khoiTaoNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        navigationView.setNavigationItemSelectedListener(this);
    }
    
    private void layThongTinNguoiDung() {
        sharedPreferences = getSharedPreferences("QuanCaPhe", MODE_PRIVATE);
        String hoTen = sharedPreferences.getString("ho_ten", "");
        vaiTroHienTai = sharedPreferences.getString("vai_tro", "");
        
        tvHoTen.setText(hoTen);
        tvVaiTro.setText(vaiTroHienTai.equals("ADMIN") ? "Quản trị viên" : "Nhân viên");
    }
    
    private void caiDatMenu() {
        Menu menu = navigationView.getMenu();
        boolean laAdmin = "ADMIN".equals(vaiTroHienTai);
        
        // Hiển thị menu dành cho admin
        menu.findItem(R.id.nav_quan_ly_san_pham).setVisible(laAdmin);
        menu.findItem(R.id.nav_quan_ly_nguoi_dung).setVisible(laAdmin);
        menu.findItem(R.id.nav_quan_ly_ban).setVisible(laAdmin);
        menu.findItem(R.id.nav_bao_cao).setVisible(laAdmin);
    }
    
    private void hienThiFragmentMacDinh() {
        // Hiển thị sơ đồ bàn làm màn hình chính
        Fragment fragment = new SoDoBanFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        navigationView.setCheckedItem(R.id.nav_so_do_ban);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sơ Đồ Bàn");
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = "";
        
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_so_do_ban) {
            fragment = new SoDoBanFragment();
            title = "Sơ Đồ Bàn";
        } else if (itemId == R.id.nav_dat_hang) {
            fragment = new DatHangFragment();
            title = "Đặt Hàng";
        } else if (itemId == R.id.nav_lich_su_don_hang) {
            fragment = new LichSuDonHangFragment();
            title = "Lịch Sử Đơn Hàng";
        } else if (itemId == R.id.nav_quan_ly_san_pham) {
            fragment = new QuanLySanPhamFragment();
            title = "Quản Lý Sản Phẩm";
        } else if (itemId == R.id.nav_quan_ly_nguoi_dung) {
            fragment = new QuanLyNguoiDungFragment();
            title = "Quản Lý Người Dùng";
        } else if (itemId == R.id.nav_quan_ly_ban) {
            fragment = new QuanLyBanFragment();
            title = "Quản Lý Bàn";
        } else if (itemId == R.id.nav_bao_cao) {
            fragment = new BaoCaoFragment();
            title = "Báo Cáo";
        } else if (itemId == R.id.nav_dang_xuat) {
            dangXuat();
            return true;
        }
        
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
                    
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    
    private void dangXuat() {
        // Xóa thông tin đăng nhập
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        
        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(this, DangNhapActivity.class);
        startActivity(intent);
        finish();
    }
    
    private void caiDatXuLyBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });
    }
}