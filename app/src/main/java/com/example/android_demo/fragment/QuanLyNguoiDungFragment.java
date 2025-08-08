package com.example.android_demo.fragment;

import android.app.AlertDialog;
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
import com.example.android_demo.adapter.NguoiDungAdapter;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.NguoiDung;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class QuanLyNguoiDungFragment extends Fragment implements NguoiDungAdapter.OnNguoiDungClickListener {
    
    private RecyclerView rvNguoiDung;
    private TextView tvKhongCoNguoiDung;
    private FloatingActionButton fabThemNguoiDung;
    private NguoiDungAdapter adapter;
    private QuanCaPheDatabase database;
    private List<NguoiDung> danhSachNguoiDung;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_nguoi_dung, container, false);
        
        khoiTaoView(view);
        khoiTaoDatabase();
        khoiTaoRecyclerView();
        suKienClick();
        taiDanhSachNguoiDung();
        
        return view;
    }
    
    private void khoiTaoView(View view) {
        rvNguoiDung = view.findViewById(R.id.rvNguoiDung);
        tvKhongCoNguoiDung = view.findViewById(R.id.tvKhongCoNguoiDung);
        fabThemNguoiDung = view.findViewById(R.id.fabThemNguoiDung);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(getContext());
    }
    
    private void khoiTaoRecyclerView() {
        danhSachNguoiDung = new ArrayList<>();
        adapter = new NguoiDungAdapter(danhSachNguoiDung);
        adapter.setOnNguoiDungClickListener(this);
        
        rvNguoiDung.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNguoiDung.setAdapter(adapter);
    }
    
    private void suKienClick() {
        fabThemNguoiDung.setOnClickListener(v -> hienThiDialogThemNguoiDung());
    }
    
    private void taiDanhSachNguoiDung() {
        danhSachNguoiDung = database.nguoiDungDao().layTatCaNguoiDung();
        adapter.capNhatDanhSach(danhSachNguoiDung);
        
        if (danhSachNguoiDung.isEmpty()) {
            tvKhongCoNguoiDung.setVisibility(View.VISIBLE);
            rvNguoiDung.setVisibility(View.GONE);
        } else {
            tvKhongCoNguoiDung.setVisibility(View.GONE);
            rvNguoiDung.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onSuaClick(NguoiDung nguoiDung) {
        hienThiDialogSuaNguoiDung(nguoiDung);
    }
    
    @Override
    public void onXoaClick(NguoiDung nguoiDung) {
        hienThiDialogXacNhanXoa(nguoiDung);
    }
    
    @Override
    public void onItemClick(NguoiDung nguoiDung) {
        hienThiDialogChiTietNguoiDung(nguoiDung);
    }
    
    private void hienThiDialogThemNguoiDung() {
        // TODO: Tạo dialog để thêm người dùng mới
        Toast.makeText(getContext(), "Chức năng thêm người dùng sẽ được phát triển", Toast.LENGTH_SHORT).show();
    }
    
    private void hienThiDialogSuaNguoiDung(NguoiDung nguoiDung) {
        // TODO: Tạo dialog để sửa thông tin người dùng
        Toast.makeText(getContext(), "Chức năng sửa người dùng sẽ được phát triển", Toast.LENGTH_SHORT).show();
    }
    
    private void hienThiDialogChiTietNguoiDung(NguoiDung nguoiDung) {
        StringBuilder chiTiet = new StringBuilder();
        chiTiet.append("Tên đăng nhập: ").append(nguoiDung.getTenDangNhap()).append("\n");
        chiTiet.append("Họ tên: ").append(nguoiDung.getHoTen()).append("\n");
        chiTiet.append("Email: ").append(nguoiDung.getEmail()).append("\n");
        chiTiet.append("Vai trò: ").append("ADMIN".equals(nguoiDung.getVaiTro()) ? "Quản trị viên" : "Nhân viên");
        
        new AlertDialog.Builder(getContext())
                .setTitle("Thông tin người dùng")
                .setMessage(chiTiet.toString())
                .setPositiveButton("Đóng", null)
                .show();
    }
    
    private void hienThiDialogXacNhanXoa(NguoiDung nguoiDung) {
        // Không cho phép xóa admin cuối cùng
        if ("ADMIN".equals(nguoiDung.getVaiTro())) {
            List<NguoiDung> danhSachAdmin = database.nguoiDungDao().layNguoiDungTheoVaiTro("ADMIN");
            if (danhSachAdmin.size() <= 1) {
                Toast.makeText(getContext(), "Không thể xóa admin cuối cùng", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa người dùng \"" + nguoiDung.getHoTen() + "\"?\n\nHành động này không thể hoàn tác.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    try {
                        database.nguoiDungDao().xoaNguoiDung(nguoiDung);
                        taiDanhSachNguoiDung();
                        Toast.makeText(getContext(), "Đã xóa người dùng", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}