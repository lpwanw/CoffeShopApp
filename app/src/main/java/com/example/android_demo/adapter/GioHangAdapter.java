package com.example.android_demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.model.ItemGioHang;
import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {
    
    private List<ItemGioHang> danhSachGioHang;
    private OnGioHangChangeListener listener;
    
    public interface OnGioHangChangeListener {
        void onTangSoLuong(ItemGioHang item);
        void onGiamSoLuong(ItemGioHang item);
        void onXoaKhoiGio(ItemGioHang item);
    }
    
    public GioHangAdapter(List<ItemGioHang> danhSachGioHang) {
        this.danhSachGioHang = danhSachGioHang;
    }
    
    public void setOnGioHangChangeListener(OnGioHangChangeListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gio_hang, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemGioHang item = danhSachGioHang.get(position);
        holder.bind(item);
    }
    
    @Override
    public int getItemCount() {
        return danhSachGioHang != null ? danhSachGioHang.size() : 0;
    }
    
    public void capNhatDanhSach(List<ItemGioHang> danhSachMoi) {
        this.danhSachGioHang = danhSachMoi;
        notifyDataSetChanged();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTenSanPhamGio;
        private TextView tvGiaGio;
        private TextView tvSoLuong;
        private ImageButton btnTangSoLuong;
        private ImageButton btnGiamSoLuong;
        private ImageButton btnXoaKhoiGio;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSanPhamGio = itemView.findViewById(R.id.tvTenSanPhamGio);
            tvGiaGio = itemView.findViewById(R.id.tvGiaGio);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            btnTangSoLuong = itemView.findViewById(R.id.btnTangSoLuong);
            btnGiamSoLuong = itemView.findViewById(R.id.btnGiamSoLuong);
            btnXoaKhoiGio = itemView.findViewById(R.id.btnXoaKhoiGio);
        }
        
        public void bind(ItemGioHang item) {
            tvTenSanPhamGio.setText(item.getSanPham().getTenSanPham());
            
            DecimalFormat df = new DecimalFormat("#,### VNĐ");
            tvGiaGio.setText(df.format(item.tinhThanhTien()));
            tvSoLuong.setText(String.valueOf(item.getSoLuong()));
            
            // Disable nút giảm khi quantity = 1, remove button sẽ xử lý việc xóa
            btnGiamSoLuong.setEnabled(item.getSoLuong() > 1);
            btnGiamSoLuong.setAlpha(item.getSoLuong() > 1 ? 1.0f : 0.3f);
            
            btnTangSoLuong.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTangSoLuong(item);
                }
            });
            
            btnGiamSoLuong.setOnClickListener(v -> {
                if (listener != null && item.getSoLuong() > 1) {
                    listener.onGiamSoLuong(item);
                }
            });
            
            btnXoaKhoiGio.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXoaKhoiGio(item);
                }
            });
        }
    }
}