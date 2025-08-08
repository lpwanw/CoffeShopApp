package com.example.android_demo;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.adapter.SanPhamDatHangAdapter;
import com.example.android_demo.adapter.ItemGioHangAdapter;
import com.example.android_demo.dao.ChiTietDonHangDao;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.*;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderTheoBanActivity extends AppCompatActivity 
    implements SanPhamDatHangAdapter.OnThemVaoGioClickListener {
    
    private Toolbar toolbar;
    private TextView tvTenBan;
    private TextView tvThoiGianBatDau;
    private TextView tvTongTienBan;
    private TextView tvSoMonDaOrder;
    private TextView tvKhongCoDonHang;
    private RecyclerView rvSanPhamOrder;
    private RecyclerView rvDonHangHienTai;
    private MaterialButton btnDongBan;
    private MaterialButton btnThanhToan;
    private TextInputEditText etTimKiemSanPham;
    private Spinner spinnerLocTrangThaiDonHang;
    
    private SanPhamDatHangAdapter sanPhamAdapter;
    private ItemGioHangAdapter itemGioHangAdapter;
    private QuanCaPheDatabase database;
    
    private int banId;
    private int soBan;
    private Ban ban;
    private PhieuPhucVu phieuPhucVuHienTai;
    private List<SanPham> danhSachSanPham;
    private List<ChiTietDonHangDao.GroupedOrderItem> danhSachItemHienTai;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_theo_ban);
        
        layThongTinBan();
        khoiTaoView();
        khoiTaoDatabase();
        khoiTaoToolbar();
        khoiTaoRecyclerView();
        khoiTaoTimKiemVaLoc();
        suKienClick();
        taiDuLieu();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        taiDuLieu();
    }
    
    private void layThongTinBan() {
        banId = getIntent().getIntExtra("ban_id", -1);
        soBan = getIntent().getIntExtra("so_ban", -1);
        
        if (banId == -1 || soBan == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin bàn", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void khoiTaoView() {
        toolbar = findViewById(R.id.toolbar);
        tvTenBan = findViewById(R.id.tvTenBan);
        tvThoiGianBatDau = findViewById(R.id.tvThoiGianBatDau);
        tvTongTienBan = findViewById(R.id.tvTongTienBan);
        tvSoMonDaOrder = findViewById(R.id.tvSoMonDaOrder);
        tvKhongCoDonHang = findViewById(R.id.tvKhongCoDonHang);
        rvSanPhamOrder = findViewById(R.id.rvSanPhamOrder);
        rvDonHangHienTai = findViewById(R.id.rvDonHangHienTai);
        btnDongBan = findViewById(R.id.btnDongBan);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        etTimKiemSanPham = findViewById(R.id.etTimKiemSanPham);
        spinnerLocTrangThaiDonHang = findViewById(R.id.spinnerLocTrangThaiDonHang);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(this);
    }
    
    private void khoiTaoToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Bàn " + String.format("%02d", soBan));
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void khoiTaoRecyclerView() {
        // RecyclerView sản phẩm
        danhSachSanPham = new ArrayList<>();
        sanPhamAdapter = new SanPhamDatHangAdapter(danhSachSanPham);
        sanPhamAdapter.setOnThemVaoGioClickListener(this);
        rvSanPhamOrder.setLayoutManager(new LinearLayoutManager(this));
        rvSanPhamOrder.setAdapter(sanPhamAdapter);
        
        // RecyclerView đơn hàng hiện tại (grouped items)
        danhSachItemHienTai = new ArrayList<>();
        itemGioHangAdapter = new ItemGioHangAdapter(danhSachItemHienTai);
        itemGioHangAdapter.setOnItemClickListener(new ItemGioHangAdapter.OnItemClickListener() {
            @Override
            public void onXemChiTiet(ChiTietDonHangDao.GroupedOrderItem item) {
                // Xem chi tiết item
                Toast.makeText(OrderTheoBanActivity.this, "Chi tiết: " + item.tenSanPham, Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onTangSoLuong(ChiTietDonHangDao.GroupedOrderItem item) {
                tangSoLuongItem(item);
            }
            
            @Override
            public void onGiamSoLuong(ChiTietDonHangDao.GroupedOrderItem item) {
                giamSoLuongItem(item);
            }
            
            @Override
            public void onXoaItem(ChiTietDonHangDao.GroupedOrderItem item) {
                xoaItem(item);
            }
        });
        rvDonHangHienTai.setLayoutManager(new LinearLayoutManager(this));
        rvDonHangHienTai.setAdapter(itemGioHangAdapter);
    }
    
    private void khoiTaoTimKiemVaLoc() {
        // Thiết lập search cho sản phẩm
        etTimKiemSanPham.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sanPhamAdapter.timKiem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Thiết lập spinner filter cho đơn hàng
        String[] trangThaiOptions = {"Tất cả", "Đang chờ", "Đang chế biến", "Hoàn thành", "Hỗn hợp"};
        String[] trangThaiValues = {"TAT_CA", "DANG_CHO", "DANG_CHE_BIEN", "HOAN_THANH", "MIXED"};
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, trangThaiOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocTrangThaiDonHang.setAdapter(spinnerAdapter);
        
        spinnerLocTrangThaiDonHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemGioHangAdapter.locTheoTrangThai(trangThaiValues[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    
    private void suKienClick() {
        btnDongBan.setOnClickListener(v -> hienThiDialogDongBan());
        btnThanhToan.setOnClickListener(v -> hienThiDialogThanhToan());
    }
    
    private void taiDuLieu() {
        // Tải thông tin bàn
        ban = database.banDao().layBanTheoId(banId);
        if (ban != null) {
            tvTenBan.setText("Bàn " + String.format("%02d", ban.getSoBan()));
        }
        
        // Tải phiếu phục vụ hiện tại
        phieuPhucVuHienTai = database.phieuPhucVuDao().layPhieuPhucVuHienTaiCuaBan(banId);
        if (phieuPhucVuHienTai != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            tvThoiGianBatDau.setText("Bắt đầu: " + sdf.format(phieuPhucVuHienTai.getThoiGianBatDau()));
        }
        
        // Tải danh sách sản phẩm
        danhSachSanPham = database.sanPhamDao().laySanPhamConHang();
        sanPhamAdapter.capNhatDanhSach(danhSachSanPham);
        
        // Tải đơn hàng nhóm theo sản phẩm
        if (phieuPhucVuHienTai != null) {
            danhSachItemHienTai = database.chiTietDonHangDao().layDonHangNhomTheoSanPham(phieuPhucVuHienTai.getId());
        } else {
            danhSachItemHienTai = new ArrayList<>();
        }
        itemGioHangAdapter.capNhatDanhSach(danhSachItemHienTai);
        
        // Hiển thị thông báo nếu không có đơn hàng
        if (danhSachItemHienTai.isEmpty()) {
            tvKhongCoDonHang.setVisibility(android.view.View.VISIBLE);
        } else {
            tvKhongCoDonHang.setVisibility(android.view.View.GONE);
        }
        
        capNhatThongKe();
    }
    
    private void capNhatThongKe() {
        double tongTien = 0;
        int soMon = 0;
        
        for (ChiTietDonHangDao.GroupedOrderItem item : danhSachItemHienTai) {
            if (!"HUY".equals(item.trangThai)) {
                tongTien += item.tongTien;
                soMon += item.tongSoLuong;
            }
        }
        
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        tvTongTienBan.setText(df.format(tongTien));
        tvSoMonDaOrder.setText(soMon + " món");
        
        // Cập nhật tổng tiền trong phiếu phục vụ
        if (phieuPhucVuHienTai != null && phieuPhucVuHienTai.getTongTien() != tongTien) {
            database.phieuPhucVuDao().capNhatTongTienPhieuPhucVu(phieuPhucVuHienTai.getId(), tongTien);
        }
    }
    
    @Override
    public void onThemVaoGio(SanPham sanPham) {
        if (phieuPhucVuHienTai == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy phiếu phục vụ", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Kiểm tra xem đã có món này trong đơn hàng chưa
            ChiTietDonHang chiTietTonTai = database.chiTietDonHangDao()
                .layChiTietTheoSanPhamVaPhieuPhucVu(phieuPhucVuHienTai.getId(), sanPham.getId());
            
            if (chiTietTonTai != null) {
                // Nếu đã có món này, tăng số lượng
                database.chiTietDonHangDao().tangSoLuongChiTiet(chiTietTonTai.getId(), 1);
                
                // Tính lại tổng tiền đơn hàng dựa trên chi tiết
                database.donHangDao().tinhLaiTongTienDonHang(chiTietTonTai.getDonHangId());
                
                Toast.makeText(this, "Đã tăng số lượng " + sanPham.getTenSanPham(), Toast.LENGTH_SHORT).show();
            } else {
                // Nếu chưa có, tạo đơn hàng mới
                SharedPreferences sharedPreferences = getSharedPreferences("QuanCaPhe", MODE_PRIVATE);
                int nguoiDungId = sharedPreferences.getInt("nguoi_dung_id", -1);
                
                DonHang donHangMoi = new DonHang(
                    nguoiDungId, 
                    banId, 
                    phieuPhucVuHienTai.getId(),
                    new Date(),
                    sanPham.getGia(),
                    "DANG_CHO"
                );
                
                long donHangId = database.donHangDao().themDonHang(donHangMoi);
                
                // Tạo chi tiết đơn hàng
                ChiTietDonHang chiTiet = new ChiTietDonHang(
                    (int) donHangId,
                    sanPham.getId(),
                    1,
                    sanPham.getGia()
                );
                
                database.chiTietDonHangDao().themChiTietDonHang(chiTiet);
                
                Toast.makeText(this, "Đã thêm " + sanPham.getTenSanPham(), Toast.LENGTH_SHORT).show();
            }
            
            taiDuLieu(); // Refresh dữ liệu
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi thêm món: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void hienThiDialogDongBan() {
        new AlertDialog.Builder(this)
                .setTitle("Đóng bàn " + soBan)
                .setMessage("Kết thúc phục vụ và đóng bàn này?\n\nLưu ý: Các đơn hàng chưa thanh toán sẽ được tự động hủy.")
                .setPositiveButton("Đóng bàn", (dialog, which) -> dongBan())
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void hienThiDialogThanhToan() {
        if (danhSachItemHienTai.isEmpty()) {
            Toast.makeText(this, "Không có đơn hàng nào để thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }
        
        double tongTien = 0;
        for (ChiTietDonHangDao.GroupedOrderItem item : danhSachItemHienTai) {
            if (!"HUY".equals(item.trangThai)) {
                tongTien += item.tongTien;
            }
        }
        
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        String thongBao = "Tổng tiền: " + df.format(tongTien) + "\n\nXác nhận thanh toán?";
        
        new AlertDialog.Builder(this)
                .setTitle("Thanh toán bàn " + soBan)
                .setMessage(thongBao)
                .setPositiveButton("Thanh toán", (dialog, which) -> thanhToan())
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void dongBan() {
        try {
            // Hủy các đơn hàng chưa hoàn thành (lấy từ database)
            if (phieuPhucVuHienTai != null) {
                List<DonHang> danhSachDonHang = database.donHangDao().layDonHangTheoPhieuPhucVu(phieuPhucVuHienTai.getId());
                for (DonHang donHang : danhSachDonHang) {
                    if ("DANG_CHO".equals(donHang.getTrangThai()) || "DANG_CHE_BIEN".equals(donHang.getTrangThai())) {
                        database.donHangDao().capNhatTrangThaiDonHang(donHang.getId(), "HUY");
                    }
                }
            }
            
            // Đóng phiếu phục vụ
            if (phieuPhucVuHienTai != null) {
                database.phieuPhucVuDao().dongPhieuPhucVu(phieuPhucVuHienTai.getId(), new Date());
            }
            
            // Cập nhật trạng thái bàn
            database.banDao().capNhatTrangThaiBan(banId, "CAN_DON_DEP");
            
            Toast.makeText(this, "Đã đóng bàn " + soBan, Toast.LENGTH_SHORT).show();
            finish();
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đóng bàn: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void thanhToan() {
        try {
            // Cập nhật trạng thái tất cả đơn hàng thành hoàn thành (lấy từ database)
            if (phieuPhucVuHienTai != null) {
                List<DonHang> danhSachDonHang = database.donHangDao().layDonHangTheoPhieuPhucVu(phieuPhucVuHienTai.getId());
                for (DonHang donHang : danhSachDonHang) {
                    if (!"HUY".equals(donHang.getTrangThai())) {
                        database.donHangDao().capNhatTrangThaiDonHang(donHang.getId(), "HOAN_THANH");
                    }
                }
            }
            
            // Đóng phiếu phục vụ với trạng thái hoàn thành
            if (phieuPhucVuHienTai != null) {
                database.phieuPhucVuDao().capNhatTrangThaiPhieuPhucVu(phieuPhucVuHienTai.getId(), "HOAN_THANH");
                database.phieuPhucVuDao().dongPhieuPhucVu(phieuPhucVuHienTai.getId(), new Date());
            }
            
            // Cập nhật trạng thái bàn
            database.banDao().capNhatTrangThaiBan(banId, "CAN_DON_DEP");
            
            Toast.makeText(this, "Đã thanh toán bàn " + soBan, Toast.LENGTH_SHORT).show();
            finish();
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi thanh toán: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void tangSoLuongItem(ChiTietDonHangDao.GroupedOrderItem item) {
        if (phieuPhucVuHienTai == null) return;
        
        try {
            // Tìm chi tiết đơn hàng để tăng số lượng
            ChiTietDonHang chiTiet = database.chiTietDonHangDao()
                .layChiTietTheoSanPhamVaPhieuPhucVu(phieuPhucVuHienTai.getId(), item.sanPhamId);
            
            if (chiTiet != null) {
                // Tăng số lượng
                database.chiTietDonHangDao().tangSoLuongChiTiet(chiTiet.getId(), 1);
                
                // Tính lại tổng tiền đơn hàng
                database.donHangDao().tinhLaiTongTienDonHang(chiTiet.getDonHangId());
                
                taiDuLieu(); // Refresh dữ liệu
                Toast.makeText(this, "Đã tăng số lượng " + item.tenSanPham, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tăng số lượng: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void giamSoLuongItem(ChiTietDonHangDao.GroupedOrderItem item) {
        if (phieuPhucVuHienTai == null || item.tongSoLuong <= 1) return;
        
        try {
            // Tìm chi tiết đơn hàng để giảm số lượng
            ChiTietDonHang chiTiet = database.chiTietDonHangDao()
                .layChiTietTheoSanPhamVaPhieuPhucVu(phieuPhucVuHienTai.getId(), item.sanPhamId);
            
            if (chiTiet != null && chiTiet.getSoLuong() > 1) {
                // Giảm số lượng (chỉ khi > 1)
                database.chiTietDonHangDao().tangSoLuongChiTiet(chiTiet.getId(), -1);
                
                // Tính lại tổng tiền đơn hàng
                database.donHangDao().tinhLaiTongTienDonHang(chiTiet.getDonHangId());
                
                taiDuLieu(); // Refresh dữ liệu
                Toast.makeText(this, "Đã giảm số lượng " + item.tenSanPham, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi giảm số lượng: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void xoaItem(ChiTietDonHangDao.GroupedOrderItem item) {
        if (phieuPhucVuHienTai == null) return;
        
        new AlertDialog.Builder(this)
            .setTitle("Xóa món")
            .setMessage("Bạn có chắc chắn muốn xóa toàn bộ " + item.tenSanPham + " (" + item.tongSoLuong + " món) khỏi đơn hàng?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                try {
                    // Tìm và xóa tất cả chi tiết đơn hàng của sản phẩm này
                    ChiTietDonHang chiTiet = database.chiTietDonHangDao()
                        .layChiTietTheoSanPhamVaPhieuPhucVu(phieuPhucVuHienTai.getId(), item.sanPhamId);
                    
                    if (chiTiet != null) {
                        xoaItemChiTiet(chiTiet, item.tenSanPham);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Lỗi khi xóa món: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void xoaItemChiTiet(ChiTietDonHang chiTiet, String tenSanPham) {
        try {
            int donHangId = chiTiet.getDonHangId();
            
            // Xóa chi tiết đơn hàng
            database.chiTietDonHangDao().xoaChiTietDonHang(chiTiet);
            
            // Kiểm tra xem đơn hàng còn chi tiết nào không
            List<ChiTietDonHang> conLai = database.chiTietDonHangDao().layChiTietTheoDonHang(donHangId);
            if (conLai.isEmpty()) {
                // Nếu không còn chi tiết nào, xóa luôn đơn hàng
                database.donHangDao().xoaDonHangTheoId(donHangId);
            } else {
                // Tính lại tổng tiền đơn hàng
                database.donHangDao().tinhLaiTongTienDonHang(donHangId);
            }
            
            taiDuLieu(); // Refresh dữ liệu
            Toast.makeText(this, "Đã xóa " + tenSanPham, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi xóa món: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}