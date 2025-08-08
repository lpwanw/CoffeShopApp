package com.example.android_demo.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.OrderTheoBanActivity;
import com.example.android_demo.R;
import com.example.android_demo.adapter.BanAdapter;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.Ban;
import com.example.android_demo.entity.PhieuPhucVu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class SoDoBanFragment extends Fragment implements BanAdapter.OnBanClickListener {
    
    private RecyclerView rvSoDoBan;
    private TextView tvKhongCoBan;
    private TextView tvSoBanTrong;
    private TextView tvSoBanDangPhucVu;
    private TextView tvSoBanCanDonDep;
    private TextInputEditText etTimKiemBan;
    private Spinner spinnerLocTrangThaiBan;
    
    private BanAdapter adapter;
    private QuanCaPheDatabase database;
    private List<Ban> danhSachBan;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_so_do_ban, container, false);
        
        khoiTaoView(view);
        khoiTaoDatabase();
        khoiTaoRecyclerView();
        khoiTaoTimKiemVaLoc();
        taiDanhSachBan();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại danh sách bàn khi quay lại fragment
        taiDanhSachBan();
    }
    
    private void khoiTaoView(View view) {
        rvSoDoBan = view.findViewById(R.id.rvSoDoBan);
        tvKhongCoBan = view.findViewById(R.id.tvKhongCoBan);
        tvSoBanTrong = view.findViewById(R.id.tvSoBanTrong);
        tvSoBanDangPhucVu = view.findViewById(R.id.tvSoBanDangPhucVu);
        tvSoBanCanDonDep = view.findViewById(R.id.tvSoBanCanDonDep);
        etTimKiemBan = view.findViewById(R.id.etTimKiemBan);
        spinnerLocTrangThaiBan = view.findViewById(R.id.spinnerLocTrangThaiBan);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(getContext());
    }
    
    private void khoiTaoRecyclerView() {
        danhSachBan = new ArrayList<>();
        adapter = new BanAdapter(danhSachBan);
        adapter.setOnBanClickListener(this);
        
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvSoDoBan.setLayoutManager(gridLayoutManager);
        rvSoDoBan.setAdapter(adapter);
    }
    
    private void khoiTaoTimKiemVaLoc() {
        // Thiết lập search
        etTimKiemBan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.timKiem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Thiết lập spinner filter
        String[] trangThaiOptions = {"Tất cả", "Trống", "Đang phục vụ", "Cần dọn dẹp"};
        String[] trangThaiValues = {"TAT_CA", "TRONG", "DANG_PHUC_VU", "CAN_DON_DEP"};
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), 
            android.R.layout.simple_spinner_item, trangThaiOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocTrangThaiBan.setAdapter(spinnerAdapter);
        
        spinnerLocTrangThaiBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.locTheoTrangThai(trangThaiValues[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    
    private void taiDanhSachBan() {
        danhSachBan = database.banDao().layTatCaBan();
        adapter.capNhatDanhSach(danhSachBan);
        
        if (danhSachBan.isEmpty()) {
            tvKhongCoBan.setVisibility(View.VISIBLE);
            rvSoDoBan.setVisibility(View.GONE);
        } else {
            tvKhongCoBan.setVisibility(View.GONE);
            rvSoDoBan.setVisibility(View.VISIBLE);
        }
        
        capNhatThongKe();
    }
    
    private void capNhatThongKe() {
        int soBanTrong = database.banDao().demBanTheoTrangThai("TRONG");
        int soBanDangPhucVu = database.banDao().demBanTheoTrangThai("DANG_PHUC_VU");
        int soBanCanDonDep = database.banDao().demBanTheoTrangThai("CAN_DON_DEP");
        
        tvSoBanTrong.setText(String.valueOf(soBanTrong));
        tvSoBanDangPhucVu.setText(String.valueOf(soBanDangPhucVu));
        tvSoBanCanDonDep.setText(String.valueOf(soBanCanDonDep));
    }
    
    @Override
    public void onBanClick(Ban ban) {
        switch (ban.getTrangThai()) {
            case "TRONG":
                hienThiDialogMoBan(ban);
                break;
                
            case "DANG_PHUC_VU":
                chuyenDenOrderTheoBan(ban);
                break;
                
            case "CAN_DON_DEP":
                hienThiDialogDonDepBan(ban);
                break;
        }
    }
    
    @Override
    public void onBanLongClick(Ban ban) {
        hienThiMenuBan(ban);
    }
    
    private void hienThiDialogMoBan(Ban ban) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Mở bàn " + ban.getSoBan());
        builder.setMessage("Bàn có " + ban.getSoChoNgoi() + " chỗ ngồi\nVị trí: " + ban.getViTri() + 
                          "\n\nBạn muốn mở bàn này?");
        
        builder.setPositiveButton("Mở bàn", (dialog, which) -> {
            moBan(ban);
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void hienThiDialogDonDepBan(Ban ban) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Dọn dẹp bàn " + ban.getSoBan());
        builder.setMessage("Đánh dấu bàn này đã được dọn dẹp?");
        
        builder.setPositiveButton("Đã dọn", (dialog, which) -> {
            database.banDao().capNhatTrangThaiBan(ban.getId(), "TRONG");
            taiDanhSachBan();
            Toast.makeText(getContext(), "Đã dọn dẹp bàn " + ban.getSoBan(), Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void hienThiMenuBan(Ban ban) {
        String[] options;
        if ("DANG_PHUC_VU".equals(ban.getTrangThai())) {
            options = new String[]{"Xem chi tiết", "Đóng bàn", "Chuyển trạng thái"};
        } else {
            options = new String[]{"Xem thông tin", "Chuyển trạng thái"};
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bàn " + ban.getSoBan());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    if ("DANG_PHUC_VU".equals(ban.getTrangThai())) {
                        chuyenDenOrderTheoBan(ban);
                    } else {
                        hienThiThongTinBan(ban);
                    }
                    break;
                case 1:
                    if ("DANG_PHUC_VU".equals(ban.getTrangThai())) {
                        hienThiDialogDongBan(ban);
                    } else {
                        hienThiDialogChuyenTrangThai(ban);
                    }
                    break;
                case 2:
                    hienThiDialogChuyenTrangThai(ban);
                    break;
            }
        });
        builder.show();
    }
    
    private void moBan(Ban ban) {
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("QuanCaPhe", MODE_PRIVATE);
            int nguoiDungId = sharedPreferences.getInt("nguoi_dung_id", -1);
            
            if (nguoiDungId == -1) {
                Toast.makeText(getContext(), "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Tạo phiếu phục vụ mới
            PhieuPhucVu phieuPhucVu = new PhieuPhucVu(ban.getId(), nguoiDungId, new Date(), "DANG_PHUC_VU", ban.getSoChoNgoi());
            database.phieuPhucVuDao().themPhieuPhucVu(phieuPhucVu);
            
            // Cập nhật trạng thái bàn
            database.banDao().capNhatTrangThaiBan(ban.getId(), "DANG_PHUC_VU");
            
            taiDanhSachBan();
            Toast.makeText(getContext(), "Đã mở bàn " + ban.getSoBan(), Toast.LENGTH_SHORT).show();
            
            // Chuyển đến màn hình order
            chuyenDenOrderTheoBan(ban);
            
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi khi mở bàn: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void chuyenDenOrderTheoBan(Ban ban) {
        Intent intent = new Intent(getActivity(), OrderTheoBanActivity.class);
        intent.putExtra("ban_id", ban.getId());
        intent.putExtra("so_ban", ban.getSoBan());
        startActivity(intent);
    }
    
    private void hienThiThongTinBan(Ban ban) {
        StringBuilder thongTin = new StringBuilder();
        thongTin.append("Số bàn: ").append(ban.getSoBan()).append("\n");
        thongTin.append("Vị trí: ").append(ban.getViTri()).append("\n");
        thongTin.append("Số chỗ ngồi: ").append(ban.getSoChoNgoi()).append("\n");
        thongTin.append("Trạng thái: ").append(layTenTrangThai(ban.getTrangThai()));
        
        if (ban.getMoTa() != null && !ban.getMoTa().trim().isEmpty()) {
            thongTin.append("\nMô tả: ").append(ban.getMoTa());
        }
        
        new AlertDialog.Builder(getContext())
                .setTitle("Thông tin bàn")
                .setMessage(thongTin.toString())
                .setPositiveButton("Đóng", null)
                .show();
    }
    
    private void hienThiDialogDongBan(Ban ban) {
        new AlertDialog.Builder(getContext())
                .setTitle("Đóng bàn " + ban.getSoBan())
                .setMessage("Kết thúc phục vụ và đóng bàn này?")
                .setPositiveButton("Đóng bàn", (dialog, which) -> dongBan(ban))
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void hienThiDialogChuyenTrangThai(Ban ban) {
        String[] trangThaiOptions = {"Trống", "Cần dọn dẹp"};
        String[] trangThaiValues = {"TRONG", "CAN_DON_DEP"};
        
        new AlertDialog.Builder(getContext())
                .setTitle("Chuyển trạng thái bàn " + ban.getSoBan())
                .setItems(trangThaiOptions, (dialog, which) -> {
                    String trangThaiMoi = trangThaiValues[which];
                    database.banDao().capNhatTrangThaiBan(ban.getId(), trangThaiMoi);
                    taiDanhSachBan();
                    Toast.makeText(getContext(), "Đã cập nhật trạng thái bàn", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void dongBan(Ban ban) {
        try {
            // Tìm phiếu phục vụ hiện tại
            PhieuPhucVu phieuPhucVu = database.phieuPhucVuDao().layPhieuPhucVuHienTaiCuaBan(ban.getId());
            
            if (phieuPhucVu != null) {
                // Đóng phiếu phục vụ
                database.phieuPhucVuDao().dongPhieuPhucVu(phieuPhucVu.getId(), new Date());
            }
            
            // Cập nhật trạng thái bàn
            database.banDao().capNhatTrangThaiBan(ban.getId(), "CAN_DON_DEP");
            
            taiDanhSachBan();
            Toast.makeText(getContext(), "Đã đóng bàn " + ban.getSoBan(), Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi khi đóng bàn: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private String layTenTrangThai(String trangThai) {
        switch (trangThai) {
            case "TRONG": return "Trống";
            case "DANG_PHUC_VU": return "Đang phục vụ";
            case "CAN_DON_DEP": return "Cần dọn dẹp";
            default: return trangThai;
        }
    }
}