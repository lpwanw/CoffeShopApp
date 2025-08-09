package com.example.android_demo.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
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
        hienThiDialogNguoiDung(null, "Thêm người dùng");
    }
    
    private void hienThiDialogSuaNguoiDung(NguoiDung nguoiDung) {
        hienThiDialogNguoiDung(nguoiDung, "Sửa thông tin người dùng");
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
    
    private void hienThiDialogNguoiDung(NguoiDung nguoiDung, String tieuDe) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_them_sua_nguoi_dung, null);
        
        TextView tvTieuDe = dialogView.findViewById(R.id.tvTieuDe);
        TextInputEditText etTenDangNhap = dialogView.findViewById(R.id.etTenDangNhap);
        TextInputEditText etMatKhau = dialogView.findViewById(R.id.etMatKhau);
        TextInputEditText etHoTen = dialogView.findViewById(R.id.etHoTen);
        TextInputEditText etEmail = dialogView.findViewById(R.id.etEmail);
        Spinner spVaiTro = dialogView.findViewById(R.id.spVaiTro);
        Button btnLuu = dialogView.findViewById(R.id.btnLuu);
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        
        tvTieuDe.setText(tieuDe);
        
        String[] vaiTroArray = {"NHAN_VIEN", "ADMIN"};
        String[] vaiTroDisplayArray = {"Nhân viên", "Quản trị viên"};
        ArrayAdapter<String> vaiTroAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, vaiTroDisplayArray);
        vaiTroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVaiTro.setAdapter(vaiTroAdapter);
        
        boolean isEdit = nguoiDung != null;
        
        if (isEdit) {
            etTenDangNhap.setText(nguoiDung.getTenDangNhap());
            etMatKhau.setText(nguoiDung.getMatKhau());
            etHoTen.setText(nguoiDung.getHoTen());
            etEmail.setText(nguoiDung.getEmail());
            
            for (int i = 0; i < vaiTroArray.length; i++) {
                if (vaiTroArray[i].equals(nguoiDung.getVaiTro())) {
                    spVaiTro.setSelection(i);
                    break;
                }
            }
        }
        
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();
        
        btnHuy.setOnClickListener(v -> dialog.dismiss());
        
        btnLuu.setOnClickListener(v -> {
            String tenDangNhap = etTenDangNhap.getText().toString().trim();
            String matKhau = etMatKhau.getText().toString().trim();
            String hoTen = etHoTen.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String vaiTro = vaiTroArray[spVaiTro.getSelectedItemPosition()];
            
            if (kiemTraThongTin(tenDangNhap, matKhau, hoTen, email, isEdit ? nguoiDung.getTenDangNhap() : null)) {
                try {
                    if (isEdit) {
                        nguoiDung.setTenDangNhap(tenDangNhap);
                        nguoiDung.setMatKhau(matKhau);
                        nguoiDung.setHoTen(hoTen);
                        nguoiDung.setEmail(email);
                        nguoiDung.setVaiTro(vaiTro);
                        
                        database.nguoiDungDao().capNhatNguoiDung(nguoiDung);
                        Toast.makeText(getContext(), "Đã cập nhật thông tin người dùng", Toast.LENGTH_SHORT).show();
                    } else {
                        NguoiDung nguoiDungMoi = new NguoiDung(tenDangNhap, matKhau, vaiTro, hoTen, email);
                        database.nguoiDungDao().themNguoiDung(nguoiDungMoi);
                        Toast.makeText(getContext(), "Đã thêm người dùng mới", Toast.LENGTH_SHORT).show();
                    }
                    
                    taiDanhSachNguoiDung();
                    dialog.dismiss();
                    
                } catch (android.database.sqlite.SQLiteConstraintException e) {
                    Toast.makeText(getContext(), "Tên đăng nhập đã tồn tại trong hệ thống", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        
        dialog.show();
    }
    
    private boolean kiemTraThongTin(String tenDangNhap, String matKhau, String hoTen, String email, String tenDangNhapCu) {
        if (TextUtils.isEmpty(tenDangNhap)) {
            Toast.makeText(getContext(), "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (TextUtils.isEmpty(matKhau)) {
            Toast.makeText(getContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (matKhau.length() < 4) {
            Toast.makeText(getContext(), "Mật khẩu phải có ít nhất 4 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (TextUtils.isEmpty(hoTen)) {
            Toast.makeText(getContext(), "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (tenDangNhapCu == null || !tenDangNhap.equals(tenDangNhapCu)) {
            NguoiDung nguoiDungTonTai = database.nguoiDungDao().kiemTraTenDangNhap(tenDangNhap);
            if (nguoiDungTonTai != null) {
                Toast.makeText(getContext(), "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        
        return true;
    }
}