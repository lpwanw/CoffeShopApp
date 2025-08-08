package com.example.android_demo.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.entity.Ban;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class BanAdapter extends RecyclerView.Adapter<BanAdapter.BanViewHolder> {
    
    private List<Ban> danhSachBan;
    private List<Ban> danhSachBanGoc;
    private OnBanClickListener listener;
    
    public interface OnBanClickListener {
        void onBanClick(Ban ban);
        void onBanLongClick(Ban ban);
    }
    
    public BanAdapter(List<Ban> danhSachBan) {
        this.danhSachBan = danhSachBan;
    }
    
    public void setOnBanClickListener(OnBanClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public BanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ban, parent, false);
        return new BanViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BanViewHolder holder, int position) {
        Ban ban = danhSachBan.get(position);
        holder.bind(ban);
    }
    
    @Override
    public int getItemCount() {
        return danhSachBan != null ? danhSachBan.size() : 0;
    }
    
    public void capNhatDanhSach(List<Ban> danhSachMoi) {
        this.danhSachBan = danhSachMoi;
        this.danhSachBanGoc = new ArrayList<>(danhSachMoi);
        notifyDataSetChanged();
    }
    
    public void timKiem(String query) {
        if (danhSachBanGoc == null) return;
        
        if (query.isEmpty()) {
            danhSachBan = new ArrayList<>(danhSachBanGoc);
        } else {
            List<Ban> ketQuaTimKiem = new ArrayList<>();
            for (Ban ban : danhSachBanGoc) {
                if (String.valueOf(ban.getSoBan()).contains(query) ||
                    ban.getViTri().toLowerCase().contains(query.toLowerCase())) {
                    ketQuaTimKiem.add(ban);
                }
            }
            danhSachBan = ketQuaTimKiem;
        }
        notifyDataSetChanged();
    }
    
    public void locTheoTrangThai(String trangThai) {
        if (danhSachBanGoc == null) return;
        
        if (trangThai.equals("TAT_CA")) {
            danhSachBan = new ArrayList<>(danhSachBanGoc);
        } else {
            List<Ban> ketQuaLoc = new ArrayList<>();
            for (Ban ban : danhSachBanGoc) {
                if (ban.getTrangThai().equals(trangThai)) {
                    ketQuaLoc.add(ban);
                }
            }
            danhSachBan = ketQuaLoc;
        }
        notifyDataSetChanged();
    }
    
    class BanViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardBan;
        private TextView tvSoBan;
        private TextView tvViTri;
        private TextView tvSoChoNgoi;
        private TextView tvTrangThaiBan;
        private TextView tvThoiGianPhucVu;
        
        public BanViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBan = (MaterialCardView) itemView;
            tvSoBan = itemView.findViewById(R.id.tvSoBan);
            tvViTri = itemView.findViewById(R.id.tvViTri);
            tvSoChoNgoi = itemView.findViewById(R.id.tvSoChoNgoi);
            tvTrangThaiBan = itemView.findViewById(R.id.tvTrangThaiBan);
            tvThoiGianPhucVu = itemView.findViewById(R.id.tvThoiGianPhucVu);
        }
        
        public void bind(Ban ban) {
            tvSoBan.setText("Bàn " + String.format("%02d", ban.getSoBan()));
            tvViTri.setText(ban.getViTri());
            tvSoChoNgoi.setText(ban.getSoChoNgoi() + " chỗ");
            
            // Cập nhật trạng thái và màu sắc
            capNhatTrangThaiVaMau(ban);
            
            // Sự kiện click
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBanClick(ban);
                }
            });
            
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onBanLongClick(ban);
                }
                return true;
            });
        }
        
        private void capNhatTrangThaiVaMau(Ban ban) {
            int mauNen;
            String trangThai;
            
            switch (ban.getTrangThai()) {
                case "TRONG":
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_green_dark);
                    trangThai = "Trống";
                    tvThoiGianPhucVu.setVisibility(View.GONE);
                    break;
                    
                case "DANG_PHUC_VU":
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_orange_dark);
                    trangThai = "Đang phục vụ";
                    tvThoiGianPhucVu.setVisibility(View.VISIBLE);
                    tvThoiGianPhucVu.setText("Đang sử dụng");
                    break;
                    
                case "CAN_DON_DEP":
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark);
                    trangThai = "Cần dọn dẹp";
                    tvThoiGianPhucVu.setVisibility(View.GONE);
                    break;
                    
                default:
                    mauNen = ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray);
                    trangThai = ban.getTrangThai();
                    tvThoiGianPhucVu.setVisibility(View.GONE);
                    break;
            }
            
            cardBan.setCardBackgroundColor(ColorStateList.valueOf(mauNen));
            tvTrangThaiBan.setText(trangThai);
        }
    }
}