package com.example.android_demo.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.android_demo.R;
import com.example.android_demo.adapter.SanPhamAdapter;
import com.example.android_demo.database.QuanCaPheDatabase;
import com.example.android_demo.entity.DanhMucSanPham;
import com.example.android_demo.entity.SanPham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuanLySanPhamFragment extends Fragment implements SanPhamAdapter.OnSanPhamClickListener {
    
    private RecyclerView rvSanPham;
    private TextView tvKhongCoDuLieu;
    private FloatingActionButton fabThemSanPham;
    private SanPhamAdapter adapter;
    private QuanCaPheDatabase database;
    private List<SanPham> danhSachSanPham;
    
    // Image handling variables
    private String currentPhotoPath;
    private ImageView currentPreviewImageView;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<String> storagePermissionLauncher;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeActivityResultLaunchers();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_san_pham, container, false);
        
        khoiTaoView(view);
        khoiTaoDatabase();
        khoiTaoRecyclerView();
        suKienClick();
        taiDanhSachSanPham();
        
        return view;
    }
    
    private void khoiTaoView(View view) {
        rvSanPham = view.findViewById(R.id.rvSanPham);
        tvKhongCoDuLieu = view.findViewById(R.id.tvKhongCoDuLieu);
        fabThemSanPham = view.findViewById(R.id.fabThemSanPham);
    }
    
    private void khoiTaoDatabase() {
        database = QuanCaPheDatabase.layDatabase(getContext());
    }
    
    private void khoiTaoRecyclerView() {
        danhSachSanPham = new ArrayList<>();
        adapter = new SanPhamAdapter(danhSachSanPham);
        adapter.setOnSanPhamClickListener(this);
        
        rvSanPham.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSanPham.setAdapter(adapter);
    }
    
    private void suKienClick() {
        fabThemSanPham.setOnClickListener(v -> hienThiDialogThemSanPham());
    }
    
    private void taiDanhSachSanPham() {
        danhSachSanPham = database.sanPhamDao().layTatCaSanPham();
        adapter.capNhatDanhSach(danhSachSanPham);
        
        // Hiển thị thông báo nếu không có dữ liệu
        if (danhSachSanPham.isEmpty()) {
            tvKhongCoDuLieu.setVisibility(View.VISIBLE);
            rvSanPham.setVisibility(View.GONE);
        } else {
            tvKhongCoDuLieu.setVisibility(View.GONE);
            rvSanPham.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onSuaClick(SanPham sanPham) {
        hienThiDialogSuaSanPham(sanPham);
    }
    
    @Override
    public void onXoaClick(SanPham sanPham) {
        hienThiDialogXacNhanXoa(sanPham);
    }
    
    @Override
    public void onItemClick(SanPham sanPham) {
        // Có thể hiển thị chi tiết sản phẩm
        Toast.makeText(getContext(), "Chi tiết: " + sanPham.getTenSanPham(), Toast.LENGTH_SHORT).show();
    }
    
    private void hienThiDialogThemSanPham() {
        hienThiDialogSanPham(null, false);
    }
    
    private void hienThiDialogSuaSanPham(SanPham sanPham) {
        hienThiDialogSanPham(sanPham, true);
    }
    
    private void hienThiDialogSanPham(SanPham sanPham, boolean laSua) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_them_sua_san_pham, null);
        
        TextView tvTieuDe = dialogView.findViewById(R.id.tvTieuDe);
        TextInputEditText etTenSanPham = dialogView.findViewById(R.id.etTenSanPham);
        TextInputEditText etMoTa = dialogView.findViewById(R.id.etMoTa);
        TextInputEditText etGia = dialogView.findViewById(R.id.etGia);
        TextInputEditText etHinhAnh = dialogView.findViewById(R.id.etHinhAnh);
        Spinner spDanhMuc = dialogView.findViewById(R.id.spDanhMuc);
        CheckBox cbConHang = dialogView.findViewById(R.id.cbConHang);
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        Button btnLuu = dialogView.findViewById(R.id.btnLuu);
        Button btnChupAnh = dialogView.findViewById(R.id.btnChupAnh);
        Button btnChonAnh = dialogView.findViewById(R.id.btnChonAnh);
        ImageView ivPreviewHinhAnh = dialogView.findViewById(R.id.ivPreviewHinhAnh);
        
        currentPreviewImageView = ivPreviewHinhAnh;
        
        // Thiết lập tiêu đề
        tvTieuDe.setText(laSua ? "Sửa sản phẩm" : "Thêm sản phẩm");
        
        // Lấy danh sách danh mục và thiết lập spinner
        List<DanhMucSanPham> danhSachDanhMuc = database.danhMucSanPhamDao().layTatCaDanhMuc();
        List<String> tenDanhMuc = new ArrayList<>();
        for (DanhMucSanPham dm : danhSachDanhMuc) {
            tenDanhMuc.add(dm.getTenDanhMuc());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tenDanhMuc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDanhMuc.setAdapter(adapter);
        
        // Thiết lập sự kiện cho nút chụp ảnh và chọn ảnh
        btnChupAnh.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });
        
        btnChonAnh.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openGallery();
            } else {
                requestStoragePermission();
            }
        });
        
        // Nếu là sửa, điền thông tin hiện tại
        if (laSua && sanPham != null) {
            etTenSanPham.setText(sanPham.getTenSanPham());
            etMoTa.setText(sanPham.getMoTa());
            etGia.setText(String.valueOf(sanPham.getGia()));
            etHinhAnh.setText(sanPham.getHinhAnh());
            cbConHang.setChecked(sanPham.isConHang());
            
            // Hiển thị hình ảnh nếu có
            if (!TextUtils.isEmpty(sanPham.getHinhAnh())) {
                loadImageIntoPreview(sanPham.getHinhAnh(), ivPreviewHinhAnh);
            }
            
            // Chọn danh mục tương ứng
            for (int i = 0; i < danhSachDanhMuc.size(); i++) {
                if (danhSachDanhMuc.get(i).getId() == sanPham.getDanhMucId()) {
                    spDanhMuc.setSelection(i);
                    break;
                }
            }
        }
        
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();
        
        // Set dialog window parameters to ensure proper sizing
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        btnHuy.setOnClickListener(v -> dialog.dismiss());
        
        btnLuu.setOnClickListener(v -> {
            String tenSanPham = etTenSanPham.getText().toString().trim();
            String moTa = etMoTa.getText().toString().trim();
            String giaStr = etGia.getText().toString().trim();
            String hinhAnh = etHinhAnh.getText().toString().trim();
            boolean conHang = cbConHang.isChecked();
            
            if (TextUtils.isEmpty(tenSanPham)) {
                etTenSanPham.setError("Vui lòng nhập tên sản phẩm");
                return;
            }
            
            if (TextUtils.isEmpty(giaStr)) {
                etGia.setError("Vui lòng nhập giá");
                return;
            }
            
            double gia;
            try {
                gia = Double.parseDouble(giaStr);
                if (gia <= 0) {
                    etGia.setError("Giá phải lớn hơn 0");
                    return;
                }
            } catch (NumberFormatException e) {
                etGia.setError("Giá không hợp lệ");
                return;
            }
            
            int viTriDanhMuc = spDanhMuc.getSelectedItemPosition();
            if (viTriDanhMuc < 0 || viTriDanhMuc >= danhSachDanhMuc.size()) {
                Toast.makeText(getContext(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int danhMucId = danhSachDanhMuc.get(viTriDanhMuc).getId();
            
            // Sử dụng đường dẫn ảnh từ camera/gallery nếu có, hoặc URL nếu người dùng nhập
            String finalImagePath = currentPhotoPath != null ? currentPhotoPath : hinhAnh;
            
            if (laSua && sanPham != null) {
                // Cập nhật sản phẩm
                sanPham.setTenSanPham(tenSanPham);
                sanPham.setMoTa(moTa);
                sanPham.setGia(gia);
                sanPham.setHinhAnh(finalImagePath);
                sanPham.setDanhMucId(danhMucId);
                sanPham.setConHang(conHang);
                
                database.sanPhamDao().capNhatSanPham(sanPham);
                Toast.makeText(getContext(), "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                // Thêm sản phẩm mới
                SanPham sanPhamMoi = new SanPham(tenSanPham, gia, danhMucId, finalImagePath, moTa);
                sanPhamMoi.setConHang(conHang);
                
                database.sanPhamDao().themSanPham(sanPhamMoi);
                Toast.makeText(getContext(), "Đã thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
            }
            
            taiDanhSachSanPham();
            dialog.dismiss();
        });
        
        dialog.show();
    }
    
    private void hienThiDialogXacNhanXoa(SanPham sanPham) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm \"" + sanPham.getTenSanPham() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    database.sanPhamDao().xoaSanPham(sanPham);
                    taiDanhSachSanPham();
                    Toast.makeText(getContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void initializeActivityResultLaunchers() {
        // Camera launcher
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    if (currentPhotoPath != null) {
                        loadImageIntoPreview(currentPhotoPath, currentPreviewImageView);
                        currentPreviewImageView.setVisibility(View.VISIBLE);
                    }
                }
            }
        );
        
        // Gallery launcher
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        try {
                            String imagePath = saveImageFromUri(imageUri);
                            if (imagePath != null) {
                                currentPhotoPath = imagePath;
                                loadImageIntoPreview(imagePath, currentPreviewImageView);
                                currentPreviewImageView.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Không thể lưu ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        );
        
        // Permission launchers
        cameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Cần cấp quyền camera để chụp ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        );
        
        storagePermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(getContext(), "Cần cấp quyền truy cập file để chọn ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }
    
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED;
    }
    
    private boolean checkStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) 
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) 
                    == PackageManager.PERMISSION_GRANTED;
        }
    }
    
    private void requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
    
    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Không thể tạo file ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        getContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraLauncher.launch(takePictureIntent);
            }
        }
    }
    
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }
    
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "SanPham");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    
    private String saveImageFromUri(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        
        File storageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "SanPham");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        
        File imageFile = new File(storageDir, imageFileName);
        FileOutputStream out = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
        out.flush();
        out.close();
        
        return imageFile.getAbsolutePath();
    }
    
    private void loadImageIntoPreview(String imagePath, ImageView imageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                // Load URL image using Glide
                Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else {
                // Local file
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Glide.with(this)
                        .load(imgFile)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
