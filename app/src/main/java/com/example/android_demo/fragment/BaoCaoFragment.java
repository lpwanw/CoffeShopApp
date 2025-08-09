package com.example.android_demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.android_demo.R;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.DonHang;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaoCaoFragment extends Fragment {
    
    private TextView tvTongDoanhThu;
    private TextView tvTongDonHang;
    private TextView tvDonHangHomNay;
    private TextView tvDoanhThuHomNay;
    private TextView tvDonHangHoanThanh;
    private TextView tvDonHangDaHuy;
    private TextView tvSanPhamBanChay;
    private TextView tvKhachHangThanThiet;
    
    private CardView cardTongDoanhThu;
    private CardView cardTongDonHang;
    private CardView cardHomNay;
    private CardView cardTrangThai;
    
    private QuanCaPheDatabase database;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bao_cao, container, false);
        
        khoiTaoView(view);
        khoiTaoDatabase();
        taiDuLieuBaoCao();
        
        return view;
    }
    
    private void khoiTaoView(View view) {
        // Text views for displaying statistics
        tvTongDoanhThu = view.findViewById(R.id.tvTongDoanhThu);
        tvTongDonHang = view.findViewById(R.id.tvTongDonHang);
        tvDonHangHomNay = view.findViewById(R.id.tvDonHangHomNay);
        tvDoanhThuHomNay = view.findViewById(R.id.tvDoanhThuHomNay);
        tvDonHangHoanThanh = view.findViewById(R.id.tvDonHangHoanThanh);
        tvDonHangDaHuy = view.findViewById(R.id.tvDonHangDaHuy);
        tvSanPhamBanChay = view.findViewById(R.id.tvSanPhamBanChay);
        tvKhachHangThanThiet = view.findViewById(R.id.tvKhachHangThanThiet);
        
        // Card views for visual grouping
        cardTongDoanhThu = view.findViewById(R.id.cardTongDoanhThu);
        cardTongDonHang = view.findViewById(R.id.cardTongDonHang);
        cardHomNay = view.findViewById(R.id.cardHomNay);
        cardTrangThai = view.findViewById(R.id.cardTrangThai);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(getContext());
    }
    
    private void taiDuLieuBaoCao() {
        new Thread(() -> {
            try {
                // Calculate statistics
                thongKeDoanhThu();
                thongKeDonHang();
                thongKeHomNay();
                thongKeTrangThai();
                thongKeSanPham();
                
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> {
                    // Handle error - could show error message in UI
                });
            }
        }).start();
    }
    
    private void thongKeDoanhThu() {
        try {
            List<DonHang> danhSachHoanThanh = database.donHangDao().layDonHangTheoTrangThai("HOAN_THANH");
            double tongDoanhThu = 0;
            
            for (DonHang donHang : danhSachHoanThanh) {
                tongDoanhThu += donHang.getTongTien();
            }
            
            DecimalFormat df = new DecimalFormat("#,### VNĐ");
            final double finalTongDoanhThu = tongDoanhThu;
            getActivity().runOnUiThread(() -> {
                tvTongDoanhThu.setText(df.format(finalTongDoanhThu));
            });
            
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> {
                tvTongDoanhThu.setText("Lỗi dữ liệu");
            });
        }
    }
    
    private void thongKeDonHang() {
        try {
            List<DonHang> tatCaDonHang = database.donHangDao().layTatCaDonHang();
            int tongSoDonHang = tatCaDonHang.size();
            
            final int finalTongSoDonHang = tongSoDonHang;
            getActivity().runOnUiThread(() -> {
                tvTongDonHang.setText(String.valueOf(finalTongSoDonHang));
            });
            
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> {
                tvTongDonHang.setText("0");
            });
        }
    }
    
    private void thongKeHomNay() {
        try {
            Date today = new Date();
            List<DonHang> donHangHomNay = database.donHangDao().layDonHangTheoNgay(today);
            
            int soDonHangHomNay = donHangHomNay.size();
            double doanhThuHomNay = 0;
            
            for (DonHang donHang : donHangHomNay) {
                if ("HOAN_THANH".equals(donHang.getTrangThai())) {
                    doanhThuHomNay += donHang.getTongTien();
                }
            }
            
            DecimalFormat df = new DecimalFormat("#,### VNĐ");
            final int finalSoDonHangHomNay = soDonHangHomNay;
            final double finalDoanhThuHomNay = doanhThuHomNay;
            getActivity().runOnUiThread(() -> {
                tvDonHangHomNay.setText(String.valueOf(finalSoDonHangHomNay));
                tvDoanhThuHomNay.setText(df.format(finalDoanhThuHomNay));
            });
            
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> {
                tvDonHangHomNay.setText("0");
                tvDoanhThuHomNay.setText("0 VNĐ");
            });
        }
    }
    
    private void thongKeTrangThai() {
        try {
            List<DonHang> donHangHoanThanh = database.donHangDao().layDonHangTheoTrangThai("HOAN_THANH");
            List<DonHang> donHangDaHuy = database.donHangDao().layDonHangTheoTrangThai("HUY");
            
            int soHoanThanh = donHangHoanThanh.size();
            int soDaHuy = donHangDaHuy.size();
            
            final int finalSoHoanThanh = soHoanThanh;
            final int finalSoDaHuy = soDaHuy;
            getActivity().runOnUiThread(() -> {
                tvDonHangHoanThanh.setText(String.valueOf(finalSoHoanThanh));
                tvDonHangDaHuy.setText(String.valueOf(finalSoDaHuy));
            });
            
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> {
                tvDonHangHoanThanh.setText("0");
                tvDonHangDaHuy.setText("0");
            });
        }
    }
    
    private void thongKeSanPham() {
        try {
            // For now, just show placeholder text
            // This could be enhanced to show actual best-selling products
            getActivity().runOnUiThread(() -> {
                tvSanPhamBanChay.setText("Cà phê đen, Bánh croissant, Trà chanh");
                tvKhachHangThanThiet.setText("Thống kê khách hàng đang phát triển...");
            });
            
        } catch (Exception e) {
            getActivity().runOnUiThread(() -> {
                tvSanPhamBanChay.setText("Chưa có dữ liệu");
                tvKhachHangThanThiet.setText("Chưa có dữ liệu");
            });
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        taiDuLieuBaoCao();
    }
}
