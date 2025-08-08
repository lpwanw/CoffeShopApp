package com.example.android_demo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.adapter.BanQuanLyAdapter;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.Ban;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class QuanLyBanFragment extends Fragment implements BanQuanLyAdapter.OnBanQuanLyClickListener {
    
    private RecyclerView rvDanhSachBan;
    private TextView tvTongSoBan;
    private TextView tvSoBanTrong;
    private TextView tvSoBanDangPhucVu;
    private TextView tvSoBanCanDonDep;
    private FloatingActionButton fabThemBan;
    private MaterialButton btnCapNhatTrangThai;
    
    private BanQuanLyAdapter adapter;
    private QuanCaPheDatabase database;
    private List<Ban> danhSachBan;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_ban, container, false);
        
        khoiTaoView(view);
        khoiTaoDatabase();
        khoiTaoRecyclerView();
        suKienClick();
        taiDanhSachBan();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        taiDanhSachBan();
    }
    
    private void khoiTaoView(View view) {
        rvDanhSachBan = view.findViewById(R.id.rvDanhSachBan);
        tvTongSoBan = view.findViewById(R.id.tvTongSoBan);
        tvSoBanTrong = view.findViewById(R.id.tvSoBanTrong);
        tvSoBanDangPhucVu = view.findViewById(R.id.tvSoBanDangPhucVu);
        tvSoBanCanDonDep = view.findViewById(R.id.tvSoBanCanDonDep);
        fabThemBan = view.findViewById(R.id.fabThemBan);
        btnCapNhatTrangThai = view.findViewById(R.id.btnCapNhatTrangThai);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(getContext());
    }
    
    private void khoiTaoRecyclerView() {
        danhSachBan = new ArrayList<>();
        adapter = new BanQuanLyAdapter(danhSachBan);
        adapter.setOnBanQuanLyClickListener(this);
        
        rvDanhSachBan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDanhSachBan.setAdapter(adapter);
    }
    
    private void suKienClick() {
        fabThemBan.setOnClickListener(v -> hienThiDialogThemBan());
        btnCapNhatTrangThai.setOnClickListener(v -> hienThiDialogCapNhatTrangThaiTatCa());
    }
    
    private void taiDanhSachBan() {
        danhSachBan = database.banDao().layTatCaBan();
        adapter.capNhatDanhSach(danhSachBan);
        capNhatThongKe();
    }
    
    private void capNhatThongKe() {
        int tongSoBan = danhSachBan.size();
        int soBanTrong = database.banDao().demBanTheoTrangThai("TRONG");
        int soBanDangPhucVu = database.banDao().demBanTheoTrangThai("DANG_PHUC_VU");
        int soBanCanDonDep = database.banDao().demBanTheoTrangThai("CAN_DON_DEP");
        
        tvTongSoBan.setText(String.valueOf(tongSoBan));
        tvSoBanTrong.setText(String.valueOf(soBanTrong));
        tvSoBanDangPhucVu.setText(String.valueOf(soBanDangPhucVu));
        tvSoBanCanDonDep.setText(String.valueOf(soBanCanDonDep));
    }
    
    @Override
    public void onSuaBan(Ban ban) {
        hienThiDialogSuaBan(ban);
    }
    
    @Override
    public void onXoaBan(Ban ban) {
        hienThiDialogXoaBan(ban);
    }
    
    @Override
    public void onCapNhatTrangThai(Ban ban) {
        hienThiDialogCapNhatTrangThai(ban);
    }
    
    private void hienThiDialogThemBan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm bàn mới");
        
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        
        EditText etSoBan = new EditText(getContext());
        etSoBan.setHint("Số bàn");
        layout.addView(etSoBan);
        
        EditText etViTri = new EditText(getContext());
        etViTri.setHint("Vị trí (VD: Khu A, Sân thượng)");
        layout.addView(etViTri);
        
        EditText etSoChoNgoi = new EditText(getContext());
        etSoChoNgoi.setHint("Số chỗ ngồi");
        layout.addView(etSoChoNgoi);
        
        EditText etMoTa = new EditText(getContext());
        etMoTa.setHint("Mô tả (tuỳ chọn)");
        layout.addView(etMoTa);
        
        builder.setView(layout);
        
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String soBanStr = etSoBan.getText().toString().trim();
            String viTri = etViTri.getText().toString().trim();
            String soChoNgoiStr = etSoChoNgoi.getText().toString().trim();
            String moTa = etMoTa.getText().toString().trim();
            
            if (soBanStr.isEmpty() || viTri.isEmpty() || soChoNgoiStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                int soBan = Integer.parseInt(soBanStr);
                int soChoNgoi = Integer.parseInt(soChoNgoiStr);
                
                // Kiểm tra số bàn đã tồn tại chưa
                if (database.banDao().kiemTraBanTonTai(soBan) > 0) {
                    Toast.makeText(getContext(), "Số bàn " + soBan + " đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Ban banMoi = new Ban(soBan, viTri, soChoNgoi, "TRONG", moTa);
                database.banDao().themBan(banMoi);
                
                taiDanhSachBan();
                Toast.makeText(getContext(), "Đã thêm bàn " + soBan, Toast.LENGTH_SHORT).show();
                
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Số bàn và số chỗ ngồi phải là số", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void hienThiDialogSuaBan(Ban ban) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sửa thông tin bàn " + ban.getSoBan());
        
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        
        EditText etSoBan = new EditText(getContext());
        etSoBan.setHint("Số bàn");
        etSoBan.setText(String.valueOf(ban.getSoBan()));
        layout.addView(etSoBan);
        
        EditText etViTri = new EditText(getContext());
        etViTri.setHint("Vị trí");
        etViTri.setText(ban.getViTri());
        layout.addView(etViTri);
        
        EditText etSoChoNgoi = new EditText(getContext());
        etSoChoNgoi.setHint("Số chỗ ngồi");
        etSoChoNgoi.setText(String.valueOf(ban.getSoChoNgoi()));
        layout.addView(etSoChoNgoi);
        
        EditText etMoTa = new EditText(getContext());
        etMoTa.setHint("Mô tả");
        etMoTa.setText(ban.getMoTa() != null ? ban.getMoTa() : "");
        layout.addView(etMoTa);
        
        builder.setView(layout);
        
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String soBanStr = etSoBan.getText().toString().trim();
            String viTri = etViTri.getText().toString().trim();
            String soChoNgoiStr = etSoChoNgoi.getText().toString().trim();
            String moTa = etMoTa.getText().toString().trim();
            
            if (soBanStr.isEmpty() || viTri.isEmpty() || soChoNgoiStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                int soBanMoi = Integer.parseInt(soBanStr);
                int soChoNgoi = Integer.parseInt(soChoNgoiStr);
                
                // Kiểm tra số bàn mới có trùng với bàn khác không
                if (soBanMoi != ban.getSoBan() && database.banDao().kiemTraBanTonTai(soBanMoi) > 0) {
                    Toast.makeText(getContext(), "Số bàn " + soBanMoi + " đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                database.banDao().capNhatBan(ban.getId(), soBanMoi, viTri, soChoNgoi, moTa);
                
                taiDanhSachBan();
                Toast.makeText(getContext(), "Đã cập nhật bàn " + soBanMoi, Toast.LENGTH_SHORT).show();
                
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Số bàn và số chỗ ngồi phải là số", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void hienThiDialogXoaBan(Ban ban) {
        // Kiểm tra bàn có đang được sử dụng không
        if ("DANG_PHUC_VU".equals(ban.getTrangThai())) {
            Toast.makeText(getContext(), "Không thể xóa bàn đang phục vụ", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa bàn " + ban.getSoBan());
        builder.setMessage("Bạn có chắc chắn muốn xóa bàn này?\nHành động này không thể hoàn tác.");
        
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            try {
                database.banDao().xoaBan(ban.getId());
                taiDanhSachBan();
                Toast.makeText(getContext(), "Đã xóa bàn " + ban.getSoBan(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Lỗi khi xóa bàn: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void hienThiDialogCapNhatTrangThai(Ban ban) {
        String[] trangThaiOptions = {"Trống", "Cần dọn dẹp"};
        String[] trangThaiValues = {"TRONG", "CAN_DON_DEP"};
        
        // Nếu bàn đang phục vụ thì không cho phép chuyển trạng thái
        if ("DANG_PHUC_VU".equals(ban.getTrangThai())) {
            Toast.makeText(getContext(), "Bàn đang phục vụ, không thể thay đổi trạng thái", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cập nhật trạng thái bàn " + ban.getSoBan());
        builder.setItems(trangThaiOptions, (dialog, which) -> {
            String trangThaiMoi = trangThaiValues[which];
            database.banDao().capNhatTrangThaiBan(ban.getId(), trangThaiMoi);
            taiDanhSachBan();
            Toast.makeText(getContext(), "Đã cập nhật trạng thái bàn " + ban.getSoBan(), Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void hienThiDialogCapNhatTrangThaiTatCa() {
        String[] options = {"Đánh dấu tất cả bàn đã dọn dẹp", "Reset tất cả bàn về trống"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cập nhật trạng thái tất cả bàn");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Đánh dấu tất cả bàn cần dọn dẹp thành trống
                    database.banDao().capNhatTatCaBanCanDonDepThanhTrong();
                    taiDanhSachBan();
                    Toast.makeText(getContext(), "Đã đánh dấu tất cả bàn đã dọn dẹp", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    hienThiDialogXacNhanResetTatCa();
                    break;
            }
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void hienThiDialogXacNhanResetTatCa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reset tất cả bàn");
        builder.setMessage("Đặt lại tất cả bàn về trạng thái trống?\n\nCảnh báo: Bàn đang phục vụ sẽ bị đóng bàn tự động.");
        
        builder.setPositiveButton("Reset", (dialog, which) -> {
            try {
                // Đóng tất cả phiếu phục vụ đang mở
                database.phieuPhucVuDao().dongTatCaPhieuPhucVuDangMo();
                
                // Đặt tất cả bàn về trạng thái trống
                database.banDao().capNhatTatCaBanVeTrong();
                
                taiDanhSachBan();
                Toast.makeText(getContext(), "Đã reset tất cả bàn về trạng thái trống", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Lỗi khi reset: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}