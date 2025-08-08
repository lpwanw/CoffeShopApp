package com.example.android_demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.entity.DonHang;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder> {
    
    private List<DonHang> danhSachDonHang;
    private OnDonHangClickListener listener;
    private boolean laAdmin;
    
    public interface OnDonHangClickListener {
        void onXemChiTiet(DonHang donHang);
        void onCapNhatTrangThai(DonHang donHang);
    }
    
    public DonHangAdapter(List<DonHang> danhSachDonHang, boolean laAdmin) {
        this.danhSachDonHang = danhSachDonHang;
        this.laAdmin = laAdmin;
    }
    
    public void setOnDonHangClickListener(OnDonHangClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_don_hang, parent, false);
        return new DonHangViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DonHang donHang = danhSachDonHang.get(position);
        holder.bind(donHang);
    }
    
    @Override
    public int getItemCount() {
        return danhSachDonHang != null ? danhSachDonHang.size() : 0;
    }
    
    public void capNhatDanhSach(List<DonHang> danhSachMoi) {
        this.danhSachDonHang = danhSachMoi;
        notifyDataSetChanged();
    }
    
    class DonHangViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMaDonHang;
        private TextView tvTrangThaiDonHang;
        private TextView tvThoiGianDat;
        private TextView tvTongTienDonHang;
        private TextView tvGhiChu;
        private MaterialButton btnXemChiTiet;
        private MaterialButton btnCapNhatTrangThai;
        
        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHang = itemView.findViewById(R.id.tvMaDonHang);
            tvTrangThaiDonHang = itemView.findViewById(R.id.tvTrangThaiDonHang);
            tvThoiGianDat = itemView.findViewById(R.id.tvThoiGianDat);
            tvTongTienDonHang = itemView.findViewById(R.id.tvTongTienDonHang);
            tvGhiChu = itemView.findViewById(R.id.tvGhiChu);
            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTiet);
            btnCapNhatTrangThai = itemView.findViewById(R.id.btnCapNhatTrangThai);
        }
        
        public void bind(DonHang donHang) {
            // Mã đơn hàng
            tvMaDonHang.setText("Đơn hàng #" + String.format("%03d", donHang.getId()));
            
            // Trạng thái
            String trangThai = layTenTrangThai(donHang.getTrangThai());
            tvTrangThaiDonHang.setText(trangThai);
            tvTrangThaiDonHang.setTextColor(layMauTrangThai(donHang.getTrangThai()));
            
            // Thời gian
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvThoiGianDat.setText(sdf.format(donHang.getThoiGianDat()));
            
            // Tổng tiền
            DecimalFormat df = new DecimalFormat("#,### VNĐ");
            tvTongTienDonHang.setText("Tổng tiền: " + df.format(donHang.getTongTien()));
            
            // Ghi chú
            if (donHang.getGhiChu() != null && !donHang.getGhiChu().trim().isEmpty()) {
                tvGhiChu.setText("Ghi chú: " + donHang.getGhiChu());
                tvGhiChu.setVisibility(View.VISIBLE);
            } else {
                tvGhiChu.setVisibility(View.GONE);
            }
            
            // Hiển thị nút cập nhật trạng thái cho admin
            if (laAdmin && !"HUY".equals(donHang.getTrangThai()) && !"HOAN_THANH".equals(donHang.getTrangThai())) {
                btnCapNhatTrangThai.setVisibility(View.VISIBLE);
            } else {
                btnCapNhatTrangThai.setVisibility(View.GONE);
            }
            
            // Sự kiện click
            btnXemChiTiet.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXemChiTiet(donHang);
                }
            });
            
            btnCapNhatTrangThai.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCapNhatTrangThai(donHang);
                }
            });
        }
        
        private String layTenTrangThai(String trangThai) {
            switch (trangThai) {
                case "DANG_CHO": return "Đang chờ";
                case "HOAN_THANH": return "Hoàn thành";
                case "HUY": return "Đã hủy";
                default: return trangThai;
            }
        }
        
        private int layMauTrangThai(String trangThai) {
            switch (trangThai) {
                case "DANG_CHO": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                case "HOAN_THANH": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                case "HUY": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
                default: 
                    return itemView.getContext().getResources().getColor(android.R.color.darker_gray);
            }
        }
    }
}