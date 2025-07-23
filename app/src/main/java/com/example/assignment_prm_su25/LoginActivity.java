package com.example.assignment_prm_su25;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment_prm_su25.ui.ForgotPasswordActivity;
import com.example.assignment_prm_su25.ui.RegisterActivity;
import com.example.assignment_prm_su25.view.LoadingButton;
import com.google.android.material.textfield.TextInputLayout;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private LoadingButton btnLogin;
    private com.google.android.material.button.MaterialButton tvRegister;
    private TextView tvForgotPassword;
    private TextInputLayout tilEmail, tilPassword;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tilEmail = findViewById(R.id.emailLayout);
        tilPassword = findViewById(R.id.passwordLayout);
        dbHelper = UserDatabaseHelper.getInstance(this);

        // Đảm bảo tài khoản abc@gmail.com luôn là admin

        // Load saved login credentials if available
        loadSavedCredentials();

        // Set click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Handle login button click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    performLogin();
                }
            }
        });

        // Handle register click
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Handle forgot password click
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_field_required, "Email"));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_field_required, "Password"));
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        return isValid;
    }

    private void performLogin() {
        setLoading(true);
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Use a Handler to simulate network delay and avoid blocking the UI thread
        new Handler().postDelayed(() -> {
            User user = dbHelper.checkUserLogin(email, password);
            // Switch back to the main thread to update the UI
            runOnUiThread(() -> {
                setLoading(false);
                if (user != null) {
                    // Save user session to SharedPreferences
                    saveUserSession(user);
                    showToast("Đăng nhập thành công!");
                    Intent intent;
                    if (user.getRole() != null && user.getRole().equalsIgnoreCase("admin")) {
                        intent = new Intent(LoginActivity.this, com.example.assignment_prm_su25.ui.AdminActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    showToast("Sai email hoặc mật khẩu!");
                    tilPassword.setError("Sai email hoặc mật khẩu!");
                }
            });
        }, 1000); // 1-second delay
    }

    private void loadSavedCredentials() {
        SharedPreferences prefs = getSharedPreferences("RememberAccount", MODE_PRIVATE);
        boolean rememberAccount = prefs.getBoolean("remember_account", false);
        
        if (rememberAccount) {
            String savedEmail = prefs.getString("saved_email", "");
            String savedPassword = prefs.getString("saved_password", "");
            
            if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
                edtEmail.setText(savedEmail);
                edtPassword.setText(savedPassword);
            }
        }
    }

    private void saveUserSession(User user) {
        // Save to LoginSession SharedPreferences
        SharedPreferences loginPrefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = loginPrefs.edit();
        loginEditor.putInt("user_id", user.getId());
        loginEditor.putString("user_email", user.getEmail());
        loginEditor.putBoolean("is_logged_in", true);
        loginEditor.apply();
        
        // Also save to UserProfile SharedPreferences for ProfileActivity
        SharedPreferences profilePrefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor profileEditor = profilePrefs.edit();
        profileEditor.putString("user_name", user.getName());
        profileEditor.putString("user_email", user.getEmail());
        profileEditor.putString("user_phone", user.getPhone() != null ? user.getPhone() : "");
        profileEditor.putString("user_address", user.getAddress() != null ? user.getAddress() : "");
        profileEditor.apply();
    }

    private void setLoading(boolean isLoading) {
        // Update button state
        btnLogin.setButtonState(
            isLoading ? LoadingButton.ButtonState.Loading : LoadingButton.ButtonState.Normal
        );
        
        // Disable/enable inputs during loading
        edtEmail.setEnabled(!isLoading);
        edtPassword.setEnabled(!isLoading);
        tvForgotPassword.setEnabled(!isLoading);
        tvRegister.setEnabled(!isLoading);
        
        // Disable social login buttons if they exist
        View btnGoogle = findViewById(R.id.btnGoogle);
        View btnFacebook = findViewById(R.id.btnFacebook);
        if (btnGoogle != null) btnGoogle.setEnabled(!isLoading);
        if (btnFacebook != null) btnFacebook.setEnabled(!isLoading);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}