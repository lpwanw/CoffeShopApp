package com.example.android_demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android_demo.R;
import com.example.android_demo.entity.NguoiDung;
import java.util.List;

public class NguoiDungAdapter extends RecyclerView.Adapter<NguoiDungAdapter.NguoiDungViewHolder> {
    
    private List<NguoiDung> danhSachNguoiDung;
    private OnNguoiDungClickListener listener;
    
    public interface OnNguoiDungClickListener {
        void onSuaClick(NguoiDung nguoiDung);
        void onXoaClick(NguoiDung nguoiDung);
        void onItemClick(NguoiDung nguoiDung);
    }
    
    public NguoiDungAdapter(List<NguoiDung> danhSachNguoiDung) {
        this.danhSachNguoiDung = danhSachNguoiDung;
    }
    
    public void setOnNguoiDungClickListener(OnNguoiDungClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public NguoiDungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nguoi_dung, parent, false);
        return new NguoiDungViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NguoiDungViewHolder holder, int position) {
        NguoiDung nguoiDung = danhSachNguoiDung.get(position);
        holder.bind(nguoiDung);
    }
    
    @Override
    public int getItemCount() {
        return danhSachNguoiDung != null ? danhSachNguoiDung.size() : 0;
    }
    
    public void capNhatDanhSach(List<NguoiDung> danhSachMoi) {
        this.danhSachNguoiDung = danhSachMoi;
        notifyDataSetChanged();
    }
    
    class NguoiDungViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHoTenNguoiDung;
        private TextView tvTenDangNhapNguoiDung;
        private TextView tvEmailNguoiDung;
        private TextView tvVaiTroNguoiDung;
        private ImageButton btnSuaNguoiDung;
        private ImageButton btnXoaNguoiDung;
        
        public NguoiDungViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTenNguoiDung = itemView.findViewById(R.id.tvHoTenNguoiDung);
            tvTenDangNhapNguoiDung = itemView.findViewById(R.id.tvTenDangNhapNguoiDung);
            tvEmailNguoiDung = itemView.findViewById(R.id.tvEmailNguoiDung);
            tvVaiTroNguoiDung = itemView.findViewById(R.id.tvVaiTroNguoiDung);
            btnSuaNguoiDung = itemView.findViewById(R.id.btnSuaNguoiDung);
            btnXoaNguoiDung = itemView.findViewById(R.id.btnXoaNguoiDung);
        }
        
        public void bind(NguoiDung nguoiDung) {
            tvHoTenNguoiDung.setText(nguoiDung.getHoTen());
            tvTenDangNhapNguoiDung.setText("@" + nguoiDung.getTenDangNhap());
            tvEmailNguoiDung.setText(nguoiDung.getEmail());
            
            // Hiển thị vai trò
            String vaiTro = "ADMIN".equals(nguoiDung.getVaiTro()) ? "Quản trị viên" : "Nhân viên";
            tvVaiTroNguoiDung.setText(vaiTro);
            
            // Đổi màu theo vai trò
            if ("ADMIN".equals(nguoiDung.getVaiTro())) {
                tvVaiTroNguoiDung.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            } else {
                tvVaiTroNguoiDung.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark));
            }
            
            // Sự kiện click
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(nguoiDung);
                }
            });
            
            btnSuaNguoiDung.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSuaClick(nguoiDung);
                }
            });
            
            btnXoaNguoiDung.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXoaClick(nguoiDung);
                }
            });
        }
    }
}