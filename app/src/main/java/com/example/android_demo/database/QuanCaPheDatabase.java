package com.example.android_demo.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.android_demo.dao.*;
import com.example.android_demo.entity.*;
import java.util.Date;

@Database(
    entities = {
        NguoiDung.class,
        DanhMucSanPham.class, 
        SanPham.class,
        DonHang.class,
        ChiTietDonHang.class,
        Ban.class,
        PhieuPhucVu.class
    },
    version = 2,
    exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class QuanCaPheDatabase extends RoomDatabase {
    
    private static volatile QuanCaPheDatabase INSTANCE;
    
    public abstract NguoiDungDao nguoiDungDao();
    public abstract DanhMucSanPhamDao danhMucSanPhamDao();
    public abstract SanPhamDao sanPhamDao();
    public abstract DonHangDao donHangDao();
    public abstract ChiTietDonHangDao chiTietDonHangDao();
    public abstract BanDao banDao();
    public abstract PhieuPhucVuDao phieuPhucVuDao();
    
    public static QuanCaPheDatabase layDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (QuanCaPheDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        QuanCaPheDatabase.class,
                        "quan_ca_phe_database"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Chỉ để development, nên xóa trong production
                    .build();
                    
                    // Khởi tạo dữ liệu mẫu
                    khoiTaoDuLieuMau();
                }
            }
        }
        return INSTANCE;
    }
    
    private static void khoiTaoDuLieuMau() {
        new Thread(() -> {
            try {
                // Kiểm tra xem đã có dữ liệu mẫu chưa
                if (INSTANCE.danhMucSanPhamDao().layTatCaDanhMuc().size() > 0 || 
                    INSTANCE.sanPhamDao().layTatCaSanPham().size() > 0 ||
                    INSTANCE.banDao().layTatCaBan().size() > 0) {
                    return; // Đã có dữ liệu, không tạo lại
                }
                
                // Tạo người dùng mẫu
                INSTANCE.nguoiDungDao().themNguoiDung(new NguoiDung("admin", "123456", "QUAN_LY", "Admin", "admin@example.com"));
                INSTANCE.nguoiDungDao().themNguoiDung(new NguoiDung("nhanvien1", "123456", "NHAN_VIEN", "Nhân viên 1", "nhanvien1@example.com"));
                INSTANCE.nguoiDungDao().themNguoiDung(new NguoiDung("nhanvien2", "123456", "NHAN_VIEN", "Nhân viên 2", "nhanvien2@example.com"));
                
                // Tạo danh mục sản phẩm
                long danhMucCaPheId = INSTANCE.danhMucSanPhamDao().themDanhMuc(
                    new DanhMucSanPham("Cà Phê", "Các loại cà phê truyền thống và hiện đại"));
                long danhMucTraId = INSTANCE.danhMucSanPhamDao().themDanhMuc(
                    new DanhMucSanPham("Trà", "Các loại trà và đồ uống từ trá"));
                long danhMucBanhId = INSTANCE.danhMucSanPhamDao().themDanhMuc(
                    new DanhMucSanPham("Bánh Kẹo", "Bánh ngọt, bánh mặn và các loại kẹo"));
                
                // Tạo sản phẩm cà phê
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Cà Phê Đen", 25000, (int)danhMucCaPheId, "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400&h=400&fit=crop", "Cà phê đen truyền thống"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Cà Phê Sữa", 30000, (int)danhMucCaPheId, "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=400&h=400&fit=crop", "Cà phê với sữa đặc"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Cappuccino", 45000, (int)danhMucCaPheId, "https://images.unsplash.com/photo-1572442388796-11668a67e53d?w=400&h=400&fit=crop", "Cà phê Ý với bọt sữa"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Latte", 50000, (int)danhMucCaPheId, "https://images.unsplash.com/photo-1561047029-3000c68339ca?w=400&h=400&fit=crop", "Cà phê với nhiều sữa"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Americano", 35000, (int)danhMucCaPheId, "https://images.unsplash.com/photo-1595981267035-7b04ca84a82d?w=400&h=400&fit=crop", "Cà phê pha loãng"));
                
                // Tạo sản phẩm trà
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Đá", 15000, (int)danhMucTraId, "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400&h=400&fit=crop", "Trà đá truyền thống"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Sữa Trân Châu", 40000, (int)danhMucTraId, "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=400&fit=crop", "Trà sữa với trân châu"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Xanh", 20000, (int)danhMucTraId, "https://images.unsplash.com/photo-1544787219-7f47ccb76574?w=400&h=400&fit=crop", "Trá xanh thơm mát"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Trà Ô Long", 25000, (int)danhMucTraId, "https://images.unsplash.com/photo-1597318881921-2c965600cdc7?w=400&h=400&fit=crop", "Trá ô long cao cấp"));
                
                // Tạo sản phẩm bánh kẹo
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Bánh Croissant", 35000, (int)danhMucBanhId, "https://images.unsplash.com/photo-1555507036-ab794f77665e?w=400&h=400&fit=crop", "Bánh sừng bò Pháp"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Bánh Tiramisu", 55000, (int)danhMucBanhId, "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=400&h=400&fit=crop", "Bánh Tiramisu Ý"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Bánh Mì Sandwich", 40000, (int)danhMucBanhId, "https://images.unsplash.com/photo-1539252554453-80ab65ce3586?w=400&h=400&fit=crop", "Bánh mì sandwich thịt nguội"));
                INSTANCE.sanPhamDao().themSanPham(new SanPham("Kẹo Dẻo", 10000, (int)danhMucBanhId, "https://images.unsplash.com/photo-1582058091505-f87a2e55a40f?w=400&h=400&fit=crop", "Kẹo dẻo nhiều vị"));
                
                // Đánh dấu một số sản phẩm hết hàng để test
                INSTANCE.sanPhamDao().capNhatTinhTrangSanPham(4, false); // Latte hết hàng
                INSTANCE.sanPhamDao().capNhatTinhTrangSanPham(12, false); // Bánh Mì hết hàng
                
                // Tạo dữ liệu bàn
                INSTANCE.banDao().themBan(new Ban(1, "Khu A", 2, "TRONG", "Bàn đôi cạnh cửa sổ"));
                INSTANCE.banDao().themBan(new Ban(2, "Khu A", 2, "TRONG", "Bàn đôi góc"));
                INSTANCE.banDao().themBan(new Ban(3, "Khu A", 4, "TRONG", "Bàn 4 người trung tâm"));
                INSTANCE.banDao().themBan(new Ban(4, "Khu B", 2, "TRONG", "Bàn đôi yên tĩnh"));
                INSTANCE.banDao().themBan(new Ban(5, "Khu B", 4, "TRONG", "Bàn 4 người"));
                INSTANCE.banDao().themBan(new Ban(6, "Khu B", 6, "TRONG", "Bàn nhóm lớn"));
                INSTANCE.banDao().themBan(new Ban(7, "Khu C", 2, "TRONG", "Bàn bar"));
                INSTANCE.banDao().themBan(new Ban(8, "Khu C", 2, "TRONG", "Bàn bar"));
                INSTANCE.banDao().themBan(new Ban(9, "Sân thượng", 4, "TRONG", "Bàn ngoài trời"));
                INSTANCE.banDao().themBan(new Ban(10, "Sân thượng", 4, "TRONG", "Bàn ngoài trời"));
                INSTANCE.banDao().themBan(new Ban(11, "VIP", 2, "TRONG", "Phòng riêng đôi"));
                INSTANCE.banDao().themBan(new Ban(12, "VIP", 6, "TRONG", "Phòng riêng nhóm"));
                
                // Tạo một số phiếu phục vụ mẫu (bàn đang được sử dụng)
                Date thoiGianHienTai = new Date();
                long phieuPhucVu1 = INSTANCE.phieuPhucVuDao().themPhieuPhucVu(
                    new PhieuPhucVu(1, 2, new Date(thoiGianHienTai.getTime() - 1800000), "DANG_PHUC_VU", 2)); // 30 phút trước
                long phieuPhucVu2 = INSTANCE.phieuPhucVuDao().themPhieuPhucVu(
                    new PhieuPhucVu(3, 2, new Date(thoiGianHienTai.getTime() - 3600000), "DANG_PHUC_VU", 4)); // 1 giờ trước
                
                // Cập nhật trạng thái bàn đang được sử dụng
                INSTANCE.banDao().capNhatTrangThaiBan(1, "DANG_PHUC_VU");
                INSTANCE.banDao().capNhatTrangThaiBan(3, "DANG_PHUC_VU");
                
                // Tạo một số đơn hàng mẫu cho các bàn đang phục vụ
                INSTANCE.donHangDao().themDonHang(new DonHang(2, 1, (int)phieuPhucVu1, 
                    new Date(thoiGianHienTai.getTime() - 1500000), 55000, "DANG_CHO"));
                INSTANCE.donHangDao().themDonHang(new DonHang(2, 3, (int)phieuPhucVu2, 
                    new Date(thoiGianHienTai.getTime() - 3300000), 120000, "DANG_CHO"));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static void dongDatabase() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}

