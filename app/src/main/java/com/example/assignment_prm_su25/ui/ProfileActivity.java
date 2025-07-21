package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.assignment_prm_su25.LoginActivity;
import com.example.assignment_prm_su25.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {
    
    private TextView tvUserName, tvUserEmail;
    private TextInputEditText etUserName, etUserEmail, etUserPhone, etUserAddress;
    private MaterialButton btnSaveProfile;
    private LinearLayout layoutMyOrders, layoutLogout;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        initViews();
        setupToolbar();
        loadUserData();
        setupClickListeners();
    }
    
    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPhone = findViewById(R.id.etUserPhone);
        etUserAddress = findViewById(R.id.etUserAddress);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        layoutMyOrders = findViewById(R.id.layoutMyOrders);
        layoutLogout = findViewById(R.id.layoutLogout);
        
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void loadUserData() {
        // Load user data from SharedPreferences
        String userName = sharedPreferences.getString("user_name", "Người dùng");
        String userEmail = sharedPreferences.getString("user_email", "user@example.com");
        String userPhone = sharedPreferences.getString("user_phone", "");
        String userAddress = sharedPreferences.getString("user_address", "");
        
        // Set data to views
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
        etUserName.setText(userName);
        etUserEmail.setText(userEmail);
        etUserPhone.setText(userPhone);
        etUserAddress.setText(userAddress);
    }
    
    private void setupClickListeners() {
        btnSaveProfile.setOnClickListener(v -> saveUserProfile());
        
        layoutMyOrders.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
        
        layoutLogout.setOnClickListener(v -> logout());
    }
    
    private void saveUserProfile() {
        String name = etUserName.getText().toString().trim();
        String email = etUserEmail.getText().toString().trim();
        String phone = etUserPhone.getText().toString().trim();
        String address = etUserAddress.getText().toString().trim();
        
        if (name.isEmpty()) {
            etUserName.setError("Vui lòng nhập tên");
            etUserName.requestFocus();
            return;
        }
        
        if (email.isEmpty()) {
            etUserEmail.setError("Vui lòng nhập email");
            etUserEmail.requestFocus();
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etUserEmail.setError("Email không hợp lệ");
            etUserEmail.requestFocus();
            return;
        }
        
        // Save to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", name);
        editor.putString("user_email", email);
        editor.putString("user_phone", phone);
        editor.putString("user_address", address);
        editor.apply();
        
        // Update header views
        tvUserName.setText(name);
        tvUserEmail.setText(email);
        
        Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
    }
    
    private void logout() {
        // Clear user session
        SharedPreferences loginPrefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = loginPrefs.edit();
        editor.clear();
        editor.apply();
        
        // Navigate to login screen
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}