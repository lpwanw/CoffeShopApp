package com.example.android_demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.dao.ChiTietDonHangDao;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemGioHangAdapter extends RecyclerView.Adapter<ItemGioHangAdapter.ItemViewHolder> {
    
    private List<ChiTietDonHangDao.GroupedOrderItem> danhSachItem;
    private List<ChiTietDonHangDao.GroupedOrderItem> danhSachItemGoc;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onXemChiTiet(ChiTietDonHangDao.GroupedOrderItem item);
        void onTangSoLuong(ChiTietDonHangDao.GroupedOrderItem item);
        void onGiamSoLuong(ChiTietDonHangDao.GroupedOrderItem item);
        void onXoaItem(ChiTietDonHangDao.GroupedOrderItem item);
    }
    
    public ItemGioHangAdapter(List<ChiTietDonHangDao.GroupedOrderItem> danhSachItem) {
        this.danhSachItem = danhSachItem;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gio_hang, parent, false);
        return new ItemViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ChiTietDonHangDao.GroupedOrderItem item = danhSachItem.get(position);
        holder.bind(item);
    }
    
    @Override
    public int getItemCount() {
        return danhSachItem != null ? danhSachItem.size() : 0;
    }
    
    public void capNhatDanhSach(List<ChiTietDonHangDao.GroupedOrderItem> danhSachMoi) {
        this.danhSachItem = danhSachMoi;
        this.danhSachItemGoc = new ArrayList<>(danhSachMoi);
        notifyDataSetChanged();
    }
    
    public void locTheoTrangThai(String trangThai) {
        if (danhSachItemGoc == null) return;
        
        if (trangThai.equals("TAT_CA")) {
            danhSachItem = new ArrayList<>(danhSachItemGoc);
        } else {
            List<ChiTietDonHangDao.GroupedOrderItem> ketQuaLoc = new ArrayList<>();
            for (ChiTietDonHangDao.GroupedOrderItem item : danhSachItemGoc) {
                if (item.trangThai.equals(trangThai)) {
                    ketQuaLoc.add(item);
                }
            }
            danhSachItem = ketQuaLoc;
        }
        notifyDataSetChanged();
    }
    
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTenSanPham;
        private TextView tvGiaSanPham;
        private TextView tvSoLuong;
        private ImageButton btnTangSoLuong;
        private ImageButton btnGiamSoLuong;
        private ImageButton btnXoaKhoiGio;
        
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPhamGio);
            tvGiaSanPham = itemView.findViewById(R.id.tvGiaGio);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            btnTangSoLuong = itemView.findViewById(R.id.btnTangSoLuong);
            btnGiamSoLuong = itemView.findViewById(R.id.btnGiamSoLuong);
            btnXoaKhoiGio = itemView.findViewById(R.id.btnXoaKhoiGio);
        }
        
        public void bind(ChiTietDonHangDao.GroupedOrderItem item) {
            tvTenSanPham.setText(item.tenSanPham);
            
            DecimalFormat df = new DecimalFormat("#,### VNĐ");
            tvGiaSanPham.setText(df.format(item.tongTien));
            
            tvSoLuong.setText(String.valueOf(item.tongSoLuong));
            
            // Disable nút giảm khi quantity = 1, remove button sẽ xử lý việc xóa
            btnGiamSoLuong.setEnabled(item.tongSoLuong > 1);
            btnGiamSoLuong.setAlpha(item.tongSoLuong > 1 ? 1.0f : 0.3f);
            
            // Màu số lượng theo trạng thái
            tvSoLuong.setTextColor(layMauTrangThai(item.trangThai));
            
            // Sự kiện click cho các nút
            btnTangSoLuong.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTangSoLuong(item);
                }
            });
            
            btnGiamSoLuong.setOnClickListener(v -> {
                if (listener != null && item.tongSoLuong > 1) {
                    listener.onGiamSoLuong(item);
                }
            });
            
            btnXoaKhoiGio.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXoaItem(item);
                }
            });
            
            // Click trên toàn bộ item để xem chi tiết
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXemChiTiet(item);
                }
            });
        }
        
        private String layTenTrangThai(String trangThai) {
            switch (trangThai) {
                case "DANG_CHO": return "Đang chờ";
                case "DANG_CHE_BIEN": return "Đang chế biến";
                case "HOAN_THANH": return "Hoàn thành";
                case "HUY": return "Đã hủy";
                case "MIXED": return "Hỗn hợp";
                default: return trangThai;
            }
        }
        
        private int layMauTrangThai(String trangThai) {
            switch (trangThai) {
                case "DANG_CHO": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                case "DANG_CHE_BIEN": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark);
                case "HOAN_THANH": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                case "HUY": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
                case "MIXED": 
                    return itemView.getContext().getResources().getColor(android.R.color.holo_purple);
                default: 
                    return itemView.getContext().getResources().getColor(android.R.color.darker_gray);
            }
        }
    }
}