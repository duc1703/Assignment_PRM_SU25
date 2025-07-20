package com.example.assignment_prm_su25;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private LoadingButton btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private TextInputLayout tilEmail, tilPassword;

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
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

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
        // Show loading state
        setLoading(true);

        // Simulate network call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This is a mock login. In a real app, you would make an API call here.
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                
                // Mock successful login
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    showToast(getString(R.string.success));
                    // Navigate to MainActivity on successful login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(getString(R.string.error));
                }
                
                // Hide loading state
                setLoading(false);
            }
        }, 2000); // 2 seconds delay to simulate network call
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