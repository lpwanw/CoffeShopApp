package com.example.android_demo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_demo.database.QuanCaPheDatabase;

public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DURATION = 3000; // 3 seconds
    
    private ImageView ivLogo;
    private TextView tvAppName;
    private TextView tvSubtitle;
    private TextView tvLoading;
    private ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        khoiTaoView();
        batDauAnimation();
        khoiTaoDatabase();
        chuyenDenManHinhChinh();
    }
    
    private void khoiTaoView() {
        ivLogo = findViewById(R.id.ivLogo);
        tvAppName = findViewById(R.id.tvAppName);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvLoading = findViewById(R.id.tvLoading);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void batDauAnimation() {
        // Logo fade in animation
        ivLogo.setAlpha(0f);
        ObjectAnimator logoAnimator = ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f);
        logoAnimator.setDuration(1000);
        logoAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        logoAnimator.start();
        
        // Logo scale animation
        ivLogo.setScaleX(0.3f);
        ivLogo.setScaleY(0.3f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.3f, 1f);
        scaleXAnimator.setDuration(1000);
        scaleYAnimator.setDuration(1000);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimator.start();
        scaleYAnimator.start();
        
        // App name animation with delay
        tvAppName.setAlpha(0f);
        tvAppName.setTranslationY(50f);
        ObjectAnimator appNameAlpha = ObjectAnimator.ofFloat(tvAppName, "alpha", 0f, 1f);
        ObjectAnimator appNameTranslation = ObjectAnimator.ofFloat(tvAppName, "translationY", 50f, 0f);
        appNameAlpha.setStartDelay(500);
        appNameTranslation.setStartDelay(500);
        appNameAlpha.setDuration(800);
        appNameTranslation.setDuration(800);
        appNameAlpha.start();
        appNameTranslation.start();
        
        // Subtitle animation with more delay
        tvSubtitle.setAlpha(0f);
        tvSubtitle.setTranslationY(30f);
        ObjectAnimator subtitleAlpha = ObjectAnimator.ofFloat(tvSubtitle, "alpha", 0f, 1f);
        ObjectAnimator subtitleTranslation = ObjectAnimator.ofFloat(tvSubtitle, "translationY", 30f, 0f);
        subtitleAlpha.setStartDelay(800);
        subtitleTranslation.setStartDelay(800);
        subtitleAlpha.setDuration(600);
        subtitleTranslation.setDuration(600);
        subtitleAlpha.start();
        subtitleTranslation.start();
        
        // Loading elements animation
        progressBar.setAlpha(0f);
        tvLoading.setAlpha(0f);
        ObjectAnimator progressAlpha = ObjectAnimator.ofFloat(progressBar, "alpha", 0f, 1f);
        ObjectAnimator loadingAlpha = ObjectAnimator.ofFloat(tvLoading, "alpha", 0f, 1f);
        progressAlpha.setStartDelay(1200);
        loadingAlpha.setStartDelay(1200);
        progressAlpha.setDuration(500);
        loadingAlpha.setDuration(500);
        progressAlpha.start();
        loadingAlpha.start();
    }
    
    private void khoiTaoDatabase() {
        // Initialize database in background thread
        new Thread(() -> {
            try {
                // Simulate some initialization work
                Thread.sleep(1000);
                
                // Initialize database - seed data will be created automatically
                QuanCaPheDatabase database = QuanCaPheDatabase.layDatabase(this);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void chuyenDenManHinhChinh() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
            // Add transition animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, SPLASH_DURATION);
    }
}