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
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {
    
    private TextView tvUserName, tvUserEmail;
    private TextInputEditText etUserName, etUserEmail, etUserPhone, etUserAddress;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private MaterialButton btnSaveProfile;
    private LinearLayout layoutMyOrders, layoutLogout;
    private SharedPreferences sharedPreferences;
    private UserDatabaseHelper dbHelper;
    private User currentUser;
    
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
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        layoutMyOrders = findViewById(R.id.layoutMyOrders);
        layoutLogout = findViewById(R.id.layoutLogout);
        
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        dbHelper = UserDatabaseHelper.getInstance(this);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void loadUserData() {
        // Get logged in user email from SharedPreferences
        SharedPreferences loginPrefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        String userEmail = loginPrefs.getString("user_email", "");
        
        if (!userEmail.isEmpty()) {
            // Load user data from database
            currentUser = dbHelper.getUserByEmail(userEmail);
            if (currentUser != null) {
                // Set data to views
                tvUserName.setText(currentUser.getName());
                tvUserEmail.setText(currentUser.getEmail());
                etUserName.setText(currentUser.getName());
                etUserEmail.setText(currentUser.getEmail());
                etUserPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
                etUserAddress.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
            } else {
                // Fallback to SharedPreferences if user not found in database
                loadFromSharedPreferences();
            }
        } else {
            // Fallback to SharedPreferences
            loadFromSharedPreferences();
        }
    }
    
    private void loadFromSharedPreferences() {
        String userName = sharedPreferences.getString("user_name", "Người dùng");
        String userEmail = sharedPreferences.getString("user_email", "user@example.com");
        String userPhone = sharedPreferences.getString("user_phone", "");
        String userAddress = sharedPreferences.getString("user_address", "");
        
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
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
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
        
        // Update user in database if currentUser exists
        if (currentUser != null) {
            // Kiểm tra mật khẩu hiện tại
            if (!currentPassword.isEmpty()) {
                if (!currentUser.getPassword().equals(currentPassword)) {
                    etCurrentPassword.setError("Mật khẩu hiện tại không chính xác!");
                    etCurrentPassword.requestFocus();
                    return;
                }
                
                // Kiểm tra mật khẩu mới
                if (newPassword.isEmpty()) {
                    etNewPassword.setError("Vui lòng nhập mật khẩu mới");
                    etNewPassword.requestFocus();
                    return;
                }
                
                if (newPassword.length() < 8) {
                    etNewPassword.setError("Mật khẩu phải có ít nhất 8 ký tự");
                    etNewPassword.requestFocus();
                    return;
                }
                
                if (!confirmPassword.equals(newPassword)) {
                    etConfirmPassword.setError("Mật khẩu không khớp!");
                    etConfirmPassword.requestFocus();
                    return;
                }
                
                // Cập nhật mật khẩu mới
                currentUser.setPassword(newPassword);
            }
            
            // Cập nhật thông tin cá nhân
            currentUser.setName(name);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            
            boolean success = dbHelper.updateUser(currentUser);
            if (success) {
                // Also save to SharedPreferences for backup
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", name);
                editor.putString("user_email", email);
                editor.putString("user_phone", phone);
                editor.putString("user_address", address);
                editor.apply();
                
                // Update login session email if changed
                SharedPreferences loginPrefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
                SharedPreferences.Editor loginEditor = loginPrefs.edit();
                loginEditor.putString("user_email", email);
                loginEditor.apply();
                
                // Update header views
                tvUserName.setText(name);
                tvUserEmail.setText(email);
                
                if (!currentPassword.isEmpty()) {
                    Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Fallback to SharedPreferences only
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_name", name);
            editor.putString("user_email", email);
            editor.putString("user_phone", phone);
            editor.putString("user_address", address);
            editor.apply();
            
            tvUserName.setText(name);
            tvUserEmail.setText(email);
            
            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
        }
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