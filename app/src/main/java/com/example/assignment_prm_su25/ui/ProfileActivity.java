// ProfileActivity.java
package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View; // Import View
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
    private MaterialButton btnSaveProfile;
    private LinearLayout layoutMyOrders, layoutLogout;
    private MaterialButton btnViewSupportMessages; // Khai báo MaterialButton mới
    private SharedPreferences sharedPreferences;
    private UserDatabaseHelper dbHelper;
    private User currentUser; // Biến để lưu thông tin người dùng hiện tại

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupToolbar();
        loadUserData(); // loadUserData sẽ lấy thông tin người dùng bao gồm cả role
        setupClickListeners();
        checkAdminAccess(); // Gọi phương thức này để kiểm tra và hiển thị/ẩn nút
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
        btnViewSupportMessages = findViewById(R.id.btnViewSupportMessages); // Tìm kiếm nút

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        dbHelper = UserDatabaseHelper.getInstance(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.profile);
        }
    }

    private void loadUserData() {
        SharedPreferences loginPrefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        String userEmail = loginPrefs.getString("user_email", "");
        int userId = loginPrefs.getInt("user_id", -1); // Lấy userId từ session

        if (userId != -1) { // Nếu có userId, ưu tiên load từ DB
            currentUser = dbHelper.getUserById(userId); // Lấy toàn bộ đối tượng User
            if (currentUser != null) {
                tvUserName.setText(currentUser.getName());
                tvUserEmail.setText(currentUser.getEmail());
                etUserName.setText(currentUser.getName());
                etUserEmail.setText(currentUser.getEmail());
                etUserPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
                etUserAddress.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
            } else {
                // Fallback to SharedPreferences if user not found in database (shouldn't happen if login is successful)
                loadFromSharedPreferences();
            }
        } else {
            // Fallback to SharedPreferences if no user ID in session
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
            Intent intent = new Intent(ProfileActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        layoutLogout.setOnClickListener(v -> logout());
    }

    // Phương thức mới để kiểm tra quyền admin
    private void checkAdminAccess() {
        if (btnViewSupportMessages != null) {
            if (currentUser != null && "admin".equals(currentUser.getRole())) {
                btnViewSupportMessages.setVisibility(View.VISIBLE); // Hiển thị nút nếu là admin
                btnViewSupportMessages.setOnClickListener(v -> {
                    Intent intent = new Intent(ProfileActivity.this, SupportMessageListActivity.class);
                    startActivity(intent);
                });
            } else {
                btnViewSupportMessages.setVisibility(View.GONE); // Ẩn nút nếu không phải admin
            }
        }
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

        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);

            boolean success = dbHelper.updateUser(currentUser);
            if (success) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", name);
                editor.putString("user_email", email);
                editor.putString("user_phone", phone);
                editor.putString("user_address", address);
                editor.apply();

                SharedPreferences loginPrefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
                SharedPreferences.Editor loginEditor = loginPrefs.edit();
                loginEditor.putString("user_email", email);
                loginEditor.apply();

                tvUserName.setText(name);
                tvUserEmail.setText(email);

                Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        } else {
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
        SharedPreferences loginPrefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = loginPrefs.edit();
        editor.clear();
        editor.apply();

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