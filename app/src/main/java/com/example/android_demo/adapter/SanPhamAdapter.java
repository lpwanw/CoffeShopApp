package com.example.android_demo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.entity.SanPham;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
    
    private List<SanPham> danhSachSanPham;
    private OnSanPhamClickListener listener;
    
    public interface OnSanPhamClickListener {
        void onSuaClick(SanPham sanPham);
        void onXoaClick(SanPham sanPham);
        void onItemClick(SanPham sanPham);
    }
    
    public SanPhamAdapter(List<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }
    
    public void setOnSanPhamClickListener(OnSanPhamClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_san_pham, parent, false);
        return new SanPhamViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sanPham = danhSachSanPham.get(position);
        holder.bind(sanPham);
    }
    
    @Override
    public int getItemCount() {
        return danhSachSanPham != null ? danhSachSanPham.size() : 0;
    }
    
    public void capNhatDanhSach(List<SanPham> danhSachMoi) {
        this.danhSachSanPham = danhSachMoi;
        notifyDataSetChanged();
    }
    
    class SanPhamViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivHinhAnh;
        private TextView tvTenSanPham;
        private TextView tvMoTa;
        private TextView tvGia;
        private TextView tvTinhTrang;
        private ImageButton btnSua;
        private ImageButton btnXoa;
        
        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinhAnh = itemView.findViewById(R.id.ivHinhAnh);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvTinhTrang = itemView.findViewById(R.id.tvTinhTrang);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
        
        public void bind(SanPham sanPham) {
            tvTenSanPham.setText(sanPham.getTenSanPham());
            tvMoTa.setText(sanPham.getMoTa());
            
            // Định dạng giá tiền
            DecimalFormat df = new DecimalFormat("#,### VNĐ");
            tvGia.setText(df.format(sanPham.getGia()));
            
            // Hiển thị tình trạng
            tvTinhTrang.setText(sanPham.isConHang() ? "Còn hàng" : "Hết hàng");
            tvTinhTrang.setTextColor(sanPham.isConHang() ? 
                itemView.getContext().getResources().getColor(android.R.color.holo_green_dark) :
                itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            
            // Hiển thị hình ảnh
            loadImageIntoView(sanPham.getHinhAnh(), ivHinhAnh);
            
            // Sự kiện click
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(sanPham);
                }
            });
            
            btnSua.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSuaClick(sanPham);
                }
            });
            
            btnXoa.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXoaClick(sanPham);
                }
            });
        }
    }
    
    private void loadImageIntoView(String imagePath, ImageView imageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                // URL image - would need additional library like Glide or Picasso
                // For now, show default image
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            } else {
                // Local file
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
}
