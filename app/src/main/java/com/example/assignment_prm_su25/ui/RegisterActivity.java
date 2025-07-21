package com.example.assignment_prm_su25.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;
import com.example.assignment_prm_su25.view.LoadingButton;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout tilName, tilEmail, tilPhone, tilAddress, tilPassword, tilConfirmPassword;
    private com.google.android.material.textfield.TextInputEditText edtName, edtEmail, edtPhone, edtAddress, edtPassword, edtConfirmPassword;
    private LoadingButton btnRegister;
    private TextView tvLogin;
    private CheckBox cbRememberAccount;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilAddress = findViewById(R.id.tilAddress);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        cbRememberAccount = findViewById(R.id.cbRememberAccount);
        
        dbHelper = UserDatabaseHelper.getInstance(this);

        // Set up click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Handle register button click
        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        // Handle login text click
        tvLogin.setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.toolbar).setOnClickListener(v -> onBackPressed());
    }

    private boolean validateInputs() {
        boolean isValid = true;
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        // Validate name
        if (TextUtils.isEmpty(name)) {
            tilName.setError(getString(R.string.error_name_required));
            isValid = false;
        } else {
            tilName.setError(null);
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_email_required));
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        } else if (dbHelper.getUserByEmail(email) != null) {
            tilEmail.setError(getString(R.string.error_email_exists));
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        // Validate phone (optional but if provided should be valid)
        if (!TextUtils.isEmpty(phone) && phone.length() < 10) {
            tilPhone.setError("Số điện thoại phải có ít nhất 10 số");
            isValid = false;
        } else {
            tilPhone.setError(null);
        }

        // Address is optional, no validation needed
        tilAddress.setError(null);

        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_password_required));
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError(getString(R.string.error_password_too_short));
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_confirm_password_required));
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_passwords_not_match));
            isValid = false;
        } else {
            tilConfirmPassword.setError(null);
        }

        return isValid;
    }

    private void registerUser() {
        // Show loading state
        setLoading(true);

        // Get input values
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String role = "user";

        // Simulate network call
        new Handler().postDelayed(() -> {
            try {
                // Check if email already exists (double check)
                if (dbHelper.getUserByEmail(email) != null) {
                    runOnUiThread(() -> {
                        setLoading(false);
                        tilEmail.setError(getString(R.string.error_email_exists));
                        showToast(getString(R.string.error_email_exists));
                    });
                    return;
                }

                // Create new user
                User user = new User(name, email, password, role);
                user.setPhone(phone);
                user.setAddress(address);
                boolean success = dbHelper.addUser(user);

                runOnUiThread(() -> {
                    setLoading(false);
                    
                    if (success) {
                        showToast(getString(R.string.registration_successful));
                        
                        // If remember account is checked, save login credentials
                        if (cbRememberAccount.isChecked()) {
                            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("saved_email", email);
                            editor.putString("saved_password", password);
                            editor.putBoolean("remember_account", true);
                            editor.apply();
                        }
                        
                        // Navigate to login screen after short delay
                        new Handler().postDelayed(() -> {
                            finish();
                        }, 1500);
                    } else {
                        showToast(getString(R.string.registration_failed));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    setLoading(false);
                    showToast(getString(R.string.error_occurred));
                });
            }
        }, 2000); // Simulate network delay
    }

    private void setLoading(boolean isLoading) {
        // Update button state
        btnRegister.setButtonState(
            isLoading ? LoadingButton.ButtonState.Loading : LoadingButton.ButtonState.Normal
        );
        
        // Disable/enable inputs during loading
        edtName.setEnabled(!isLoading);
        edtEmail.setEnabled(!isLoading);
        edtPhone.setEnabled(!isLoading);
        edtAddress.setEnabled(!isLoading);
        edtPassword.setEnabled(!isLoading);
        edtConfirmPassword.setEnabled(!isLoading);
        tvLogin.setEnabled(!isLoading);
        
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