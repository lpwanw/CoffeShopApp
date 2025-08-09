package com.example.android_demo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.android_demo.R;
import com.example.android_demo.entity.SanPham;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDatHangAdapter extends RecyclerView.Adapter<SanPhamDatHangAdapter.ViewHolder> {
    
    private List<SanPham> danhSachSanPham;
    private List<SanPham> danhSachSanPhamGoc;
    private OnThemVaoGioClickListener listener;
    
    public interface OnThemVaoGioClickListener {
        void onThemVaoGio(SanPham sanPham);
    }
    
    public SanPhamDatHangAdapter(List<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }
    
    public void setOnThemVaoGioClickListener(OnThemVaoGioClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_san_pham_dat_hang, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = danhSachSanPham.get(position);
        holder.bind(sanPham);
    }
    
    @Override
    public int getItemCount() {
        return danhSachSanPham != null ? danhSachSanPham.size() : 0;
    }
    
    public void capNhatDanhSach(List<SanPham> danhSachMoi) {
        this.danhSachSanPham = danhSachMoi;
        this.danhSachSanPhamGoc = new ArrayList<>(danhSachMoi);
        notifyDataSetChanged();
    }
    
    public void timKiem(String query) {
        if (danhSachSanPhamGoc == null) return;
        
        if (query.isEmpty()) {
            danhSachSanPham = new ArrayList<>(danhSachSanPhamGoc);
        } else {
            List<SanPham> ketQuaTimKiem = new ArrayList<>();
            for (SanPham sanPham : danhSachSanPhamGoc) {
                if (sanPham.getTenSanPham().toLowerCase().contains(query.toLowerCase()) ||
                    sanPham.getMoTa().toLowerCase().contains(query.toLowerCase())) {
                    ketQuaTimKiem.add(sanPham);
                }
            }
            danhSachSanPham = ketQuaTimKiem;
        }
        notifyDataSetChanged();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivHinhAnhDatHang;
        private TextView tvTenSanPhamDatHang;
        private TextView tvGiaDatHang;
        private MaterialButton btnThemVaoGio;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinhAnhDatHang = itemView.findViewById(R.id.ivHinhAnhDatHang);
            tvTenSanPhamDatHang = itemView.findViewById(R.id.tvTenSanPhamDatHang);
            tvGiaDatHang = itemView.findViewById(R.id.tvGiaDatHang);
            btnThemVaoGio = itemView.findViewById(R.id.btnThemVaoGio);
        }
        
        public void bind(SanPham sanPham) {
            tvTenSanPhamDatHang.setText(sanPham.getTenSanPham());
            
            DecimalFormat df = new DecimalFormat("#,### VNĐ");
            tvGiaDatHang.setText(df.format(sanPham.getGia()));
            
            // Hiển thị hình ảnh sản phẩm
            loadImageIntoView(sanPham.getHinhAnh(), ivHinhAnhDatHang);
            
            // Disable nút nếu hết hàng
            btnThemVaoGio.setEnabled(sanPham.isConHang());
            btnThemVaoGio.setText(sanPham.isConHang() ? "Thêm" : "Hết hàng");
            
            btnThemVaoGio.setOnClickListener(v -> {
                if (listener != null && sanPham.isConHang()) {
                    listener.onThemVaoGio(sanPham);
                }
            });
        }
    }
    
    private void loadImageIntoView(String imagePath, ImageView imageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                // Load URL image using Glide
                Glide.with(imageView.getContext())
                    .load(imagePath)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageView);
            } else {
                // Local file
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Glide.with(imageView.getContext())
                        .load(imgFile)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
}
