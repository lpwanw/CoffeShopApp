package com.example.android_demo.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.entity.Ban;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class BanQuanLyAdapter extends RecyclerView.Adapter<BanQuanLyAdapter.BanQuanLyViewHolder> {
    
    private List<Ban> danhSachBan;
    private OnBanQuanLyClickListener listener;
    
    public interface OnBanQuanLyClickListener {
        void onSuaBan(Ban ban);
        void onXoaBan(Ban ban);
        void onCapNhatTrangThai(Ban ban);
    }
    
    public BanQuanLyAdapter(List<Ban> danhSachBan) {
        this.danhSachBan = danhSachBan;
    }
    
    public void setOnBanQuanLyClickListener(OnBanQuanLyClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public BanQuanLyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ban_quan_ly, parent, false);
        return new BanQuanLyViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BanQuanLyViewHolder holder, int position) {
        Ban ban = danhSachBan.get(position);
        holder.bind(ban);
    }
    
    @Override
    public int getItemCount() {
        return danhSachBan != null ? danhSachBan.size() : 0;
    }
    
    public void capNhatDanhSach(List<Ban> danhSachMoi) {
        this.danhSachBan = danhSachMoi;
        notifyDataSetChanged();
    }
    
    class BanQuanLyViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardBan;
        private TextView tvSoBan;
        private TextView tvViTri;
        private TextView tvSoChoNgoi;
        private TextView tvTrangThaiBan;
        private TextView tvMoTa;
        private ImageButton btnSua;
        private ImageButton btnXoa;
        private ImageButton btnCapNhatTrangThai;
        
        public BanQuanLyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBan = itemView.findViewById(R.id.cardBan);
            tvSoBan = itemView.findViewById(R.id.tvSoBan);
            tvViTri = itemView.findViewById(R.id.tvViTri);
            tvSoChoNgoi = itemView.findViewById(R.id.tvSoChoNgoi);
            tvTrangThaiBan = itemView.findViewById(R.id.tvTrangThaiBan);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            btnCapNhatTrangThai = itemView.findViewById(R.id.btnCapNhatTrangThai);
        }
        
        public void bind(Ban ban) {
            tvSoBan.setText("Bàn " + String.format("%02d", ban.getSoBan()));
            tvViTri.setText("Vị trí: " + ban.getViTri());
            tvSoChoNgoi.setText("Số chỗ: " + ban.getSoChoNgoi());
            
            // Hiển thị mô tả
            if (ban.getMoTa() != null && !ban.getMoTa().trim().isEmpty()) {
                tvMoTa.setText(ban.getMoTa());
                tvMoTa.setVisibility(View.VISIBLE);
            } else {
                tvMoTa.setVisibility(View.GONE);
            }
            
            // Cập nhật trạng thái và màu sắc
            capNhatTrangThaiVaMau(ban);
            
            // Sự kiện click
            btnSua.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSuaBan(ban);
                }
            });
            
            btnXoa.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXoaBan(ban);
                }
            });
            
            btnCapNhatTrangThai.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCapNhatTrangThai(ban);
                }
            });
        }
        
        private void capNhatTrangThaiVaMau(Ban ban) {
            int mauNen;
            String trangThai;
            
            switch (ban.getTrangThai()) {
                case "TRONG":
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_green_light);
                    trangThai = "Trống";
                    break;
                    
                case "DANG_PHUC_VU":
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_orange_light);
                    trangThai = "Đang phục vụ";
                    break;
                    
                case "CAN_DON_DEP":
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_light);
                    trangThai = "Cần dọn dẹp";
                    break;
                    
                default:
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray);
                    trangThai = ban.getTrangThai();
                    break;
            }
            
            cardBan.setCardBackgroundColor(ColorStateList.valueOf(mauNen));
            tvTrangThaiBan.setText(trangThai);
        }
    }
}