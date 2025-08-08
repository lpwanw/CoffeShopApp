package com.example.android_demo.fragment;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.adapter.DonHangAdapter;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.ChiTietDonHang;
import com.example.android_demo.entity.DonHang;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class LichSuDonHangFragment extends Fragment implements DonHangAdapter.OnDonHangClickListener {
    
    private RecyclerView rvLichSuDonHang;
    private TextView tvKhongCoDonHang;
    private DonHangAdapter adapter;
    private QuanCaPheDatabase database;
    private List<DonHang> danhSachDonHang;
    private boolean laAdmin = false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su_don_hang, container, false);
        
        khoiTaoView(view);
        khoiTaoDatabase();
        kiemTraVaiTro();
        khoiTaoRecyclerView();
        taiDanhSachDonHang();
        
        return view;
    }
    
    private void khoiTaoView(View view) {
        rvLichSuDonHang = view.findViewById(R.id.rvLichSuDonHang);
        tvKhongCoDonHang = view.findViewById(R.id.tvKhongCoDonHang);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(getContext());
    }
    
    private void kiemTraVaiTro() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("QuanCaPhe", MODE_PRIVATE);
        String vaiTro = sharedPreferences.getString("vai_tro", "");
        laAdmin = "ADMIN".equals(vaiTro);
    }
    
    private void khoiTaoRecyclerView() {
        danhSachDonHang = new ArrayList<>();
        adapter = new DonHangAdapter(danhSachDonHang, laAdmin);
        adapter.setOnDonHangClickListener(this);
        
        rvLichSuDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLichSuDonHang.setAdapter(adapter);
    }
    
    private void taiDanhSachDonHang() {
        if (laAdmin) {
            // Admin xem tất cả đơn hàng
            danhSachDonHang = database.donHangDao().layTatCaDonHang();
        } else {
            // Nhân viên chỉ xem đơn hàng của mình
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("QuanCaPhe", MODE_PRIVATE);
            int nguoiDungId = sharedPreferences.getInt("nguoi_dung_id", -1);
            danhSachDonHang = database.donHangDao().layDonHangTheoNguoiDung(nguoiDungId);
        }
        
        adapter.capNhatDanhSach(danhSachDonHang);
        
        if (danhSachDonHang.isEmpty()) {
            tvKhongCoDonHang.setVisibility(View.VISIBLE);
            rvLichSuDonHang.setVisibility(View.GONE);
        } else {
            tvKhongCoDonHang.setVisibility(View.GONE);
            rvLichSuDonHang.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onXemChiTiet(DonHang donHang) {
        hienThiDialogChiTietDonHang(donHang);
    }
    
    @Override
    public void onCapNhatTrangThai(DonHang donHang) {
        if (laAdmin) {
            hienThiDialogCapNhatTrangThai(donHang);
        }
    }
    
    private void hienThiDialogChiTietDonHang(DonHang donHang) {
        // Lấy chi tiết đơn hàng
        List<ChiTietDonHang> danhSachChiTiet = database.chiTietDonHangDao().layChiTietTheoDonHang(donHang.getId());
        
        StringBuilder chiTietText = new StringBuilder();
        chiTietText.append("Mã đơn hàng: #").append(String.format("%03d", donHang.getId())).append("\n\n");
        chiTietText.append("Chi tiết sản phẩm:\n");
        
        for (ChiTietDonHang chiTiet : danhSachChiTiet) {
            // Lấy tên sản phẩm
            String tenSanPham = database.sanPhamDao().laySanPhamTheoId(chiTiet.getSanPhamId()).getTenSanPham();
            chiTietText.append("- ").append(tenSanPham)
                      .append(" x").append(chiTiet.getSoLuong())
                      .append(" = ").append(String.format("%,.0f", chiTiet.getGiaBan() * chiTiet.getSoLuong())).append(" VNĐ\n");
        }
        
        chiTietText.append("\nTổng tiền: ").append(String.format("%,.0f", donHang.getTongTien())).append(" VNĐ");
        
        new AlertDialog.Builder(getContext())
                .setTitle("Chi tiết đơn hàng")
                .setMessage(chiTietText.toString())
                .setPositiveButton("Đóng", null)
                .show();
    }
    
    private void hienThiDialogCapNhatTrangThai(DonHang donHang) {
        String[] trangThaiOptions = {"Hoàn thành", "Hủy đơn hàng"};
        String[] trangThaiValues = {"HOAN_THANH", "HUY"};
        
        new AlertDialog.Builder(getContext())
                .setTitle("Cập nhật trạng thái đơn hàng")
                .setItems(trangThaiOptions, (dialog, which) -> {
                    String trangThaiMoi = trangThaiValues[which];
                    capNhatTrangThaiDonHang(donHang, trangThaiMoi);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void capNhatTrangThaiDonHang(DonHang donHang, String trangThaiMoi) {
        try {
            database.donHangDao().capNhatTrangThaiDonHang(donHang.getId(), trangThaiMoi);
            taiDanhSachDonHang(); // Refresh danh sách
            
            String thongBao = trangThaiMoi.equals("HOAN_THANH") ? "Đã hoàn thành đơn hàng" : "Đã hủy đơn hàng";
            Toast.makeText(getContext(), thongBao, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi khi cập nhật trạng thái: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}