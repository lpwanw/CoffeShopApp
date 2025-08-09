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
import com.example.android_demo.adapter.GioHangAdapter;
import com.example.android_demo.adapter.SanPhamDatHangAdapter;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.ChiTietDonHang;
import com.example.android_demo.entity.DonHang;
import com.example.android_demo.entity.SanPham;
import com.example.android_demo.model.ItemGioHang;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class DatHangFragment extends Fragment 
    implements SanPhamDatHangAdapter.OnThemVaoGioClickListener, GioHangAdapter.OnGioHangChangeListener {
    
    private RecyclerView rvSanPhamDatHang;
    private RecyclerView rvGioHang;
    private TextView tvKhongCoSanPham;
    private TextView tvTongTien;
    private MaterialButton btnDatHang;
    
    private SanPhamDatHangAdapter sanPhamAdapter;
    private GioHangAdapter gioHangAdapter;
    private QuanCaPheDatabase database;
    private List<SanPham> danhSachSanPham;
    private List<ItemGioHang> danhSachGioHang;
    private double tongTien = 0;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dat_hang, container, false);
        
        khoiTaoView(view);
        khoiTaoDatabase();
        khoiTaoRecyclerView();
        suKienClick();
        taiDanhSachSanPham();
        
        return view;
    }
    
    private void khoiTaoView(View view) {
        rvSanPhamDatHang = view.findViewById(R.id.rvSanPhamDatHang);
        rvGioHang = view.findViewById(R.id.rvGioHang);
        tvKhongCoSanPham = view.findViewById(R.id.tvKhongCoSanPham);
        tvTongTien = view.findViewById(R.id.tvTongTien);
        btnDatHang = view.findViewById(R.id.btnDatHang);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(getContext());
    }
    
    private void khoiTaoRecyclerView() {
        // Khởi tạo danh sách sản phẩm
        danhSachSanPham = new ArrayList<>();
        sanPhamAdapter = new SanPhamDatHangAdapter(danhSachSanPham);
        sanPhamAdapter.setOnThemVaoGioClickListener(this);
        rvSanPhamDatHang.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSanPhamDatHang.setAdapter(sanPhamAdapter);
        
        // Khởi tạo giỏ hàng
        danhSachGioHang = new ArrayList<>();
        gioHangAdapter = new GioHangAdapter(danhSachGioHang);
        gioHangAdapter.setOnGioHangChangeListener(this);
        rvGioHang.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGioHang.setAdapter(gioHangAdapter);
    }
    
    private void suKienClick() {
        btnDatHang.setOnClickListener(v -> xacNhanDatHang());
    }
    
    private void taiDanhSachSanPham() {
        new Thread(() -> {
            try {
                List<SanPham> danhSach = database.sanPhamDao().laySanPhamConHang();
                
                getActivity().runOnUiThread(() -> {
                    danhSachSanPham = danhSach;
                    sanPhamAdapter.capNhatDanhSach(danhSachSanPham);
                    
                    if (danhSachSanPham.isEmpty()) {
                        tvKhongCoSanPham.setVisibility(View.VISIBLE);
                        rvSanPhamDatHang.setVisibility(View.GONE);
                    } else {
                        tvKhongCoSanPham.setVisibility(View.GONE);
                        rvSanPhamDatHang.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> 
                    Toast.makeText(getContext(), "Lỗi khi tải danh sách sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    
    @Override
    public void onThemVaoGio(SanPham sanPham) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        ItemGioHang itemTonTai = null;
        for (ItemGioHang item : danhSachGioHang) {
            if (item.getSanPham().getId() == sanPham.getId()) {
                itemTonTai = item;
                break;
            }
        }
        
        if (itemTonTai != null) {
            itemTonTai.setSoLuong(itemTonTai.getSoLuong() + 1);
        } else {
            danhSachGioHang.add(new ItemGioHang(sanPham, 1));
        }
        
        capNhatGioHang();
        Toast.makeText(getContext(), "Đã thêm " + sanPham.getTenSanPham() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onTangSoLuong(ItemGioHang item) {
        item.setSoLuong(item.getSoLuong() + 1);
        capNhatGioHang();
    }
    
    @Override
    public void onGiamSoLuong(ItemGioHang item) {
        if (item.getSoLuong() > 1) {
            item.setSoLuong(item.getSoLuong() - 1);
            capNhatGioHang();
        }
    }
    
    @Override
    public void onXoaKhoiGio(ItemGioHang item) {
        danhSachGioHang.remove(item);
        capNhatGioHang();
        Toast.makeText(getContext(), "Đã xóa " + item.getSanPham().getTenSanPham() + " khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
    }
    
    private void capNhatGioHang() {
        gioHangAdapter.capNhatDanhSach(danhSachGioHang);
        tinhTongTien();
        
        btnDatHang.setEnabled(!danhSachGioHang.isEmpty());
    }
    
    private void tinhTongTien() {
        tongTien = 0;
        for (ItemGioHang item : danhSachGioHang) {
            tongTien += item.tinhThanhTien();
        }
        
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        tvTongTien.setText("Tổng tiền: " + df.format(tongTien));
    }
    
    private void xacNhanDatHang() {
        if (danhSachGioHang.isEmpty()) {
            Toast.makeText(getContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận đặt hàng")
                .setMessage("Tổng tiền: " + new DecimalFormat("#,### VNĐ").format(tongTien) + 
                           "\nBạn có chắc chắn muốn đặt hàng?")
                .setPositiveButton("Đặt hàng", (dialog, which) -> taoDonHang())
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void taoDonHang() {
        // Execute database operations in background thread
        new Thread(() -> {
            try {
                // Lấy thông tin người dùng
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("QuanCaPhe", MODE_PRIVATE);
                int nguoiDungId = sharedPreferences.getInt("nguoi_dung_id", -1);
                
                if (nguoiDungId == -1) {
                    getActivity().runOnUiThread(() -> 
                        Toast.makeText(getContext(), "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }
                
                // Tạo đơn hàng take-away với banId và phieuPhucVuId null
                DonHang donHang = new DonHang();
                donHang.setNguoiDungId(nguoiDungId);
                donHang.setBanId(null); // Null for take-away orders
                donHang.setPhieuPhucVuId(null); // Null for orders without service ticket
                donHang.setThoiGianDat(new Date());
                donHang.setTongTien(tongTien);
                donHang.setTrangThai("DANG_CHO");
                
                long donHangId = database.donHangDao().themDonHang(donHang);
                
                // Tạo chi tiết đơn hàng
                List<ChiTietDonHang> danhSachChiTiet = new ArrayList<>();
                for (ItemGioHang item : danhSachGioHang) {
                    ChiTietDonHang chiTiet = new ChiTietDonHang(
                        (int) donHangId,
                        item.getSanPham().getId(),
                        item.getSoLuong(),
                        item.getSanPham().getGia()
                    );
                    danhSachChiTiet.add(chiTiet);
                }
                
                database.chiTietDonHangDao().themDanhSachChiTiet(danhSachChiTiet);
                
                // Update UI on main thread
                getActivity().runOnUiThread(() -> {
                    // Xóa giỏ hàng
                    danhSachGioHang.clear();
                    capNhatGioHang();
                    
                    Toast.makeText(getContext(), "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                });
                
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> 
                    Toast.makeText(getContext(), "Lỗi khi đặt hàng: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
