package com.example.assignment_prm_su25.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_prm_su25.R
import com.example.assignment_prm_su25.databinding.ActivityLoginBinding
import com.example.assignment_prm_su25.view.LoadingButton
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Handle login button click
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                performLogin()
            }
        }
        
        // Handle forgot password click
        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
        
        // Handle register click
        binding.tvRegister.setOnClickListener {
            // TODO: Navigate to RegisterActivity
            showToast(getString(R.string.register_clicked))
        }
        
        // Handle Google login
        binding.btnGoogle.setOnClickListener {
            // TODO: Implement Google Sign In
            showToast(getString(R.string.google_sign_in_clicked))
        }
        
        // Handle Facebook login
        binding.btnFacebook.setOnClickListener {
            // TODO: Implement Facebook Login
            showToast(getString(R.string.facebook_login_clicked))
        }
    }
    
    private fun validateInputs(): Boolean {
        var isValid = true
        
        // Validate email
        if (binding.etEmail.text.isNullOrBlank()) {
            binding.tilEmail.error = getString(R.string.error_email_required)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            isValid = false
        } else {
            binding.tilEmail.error = null
        }
        
        // Validate password
        if (binding.etPassword.text.isNullOrBlank()) {
            binding.tilPassword.error = getString(R.string.error_password_required)
            isValid = false
        } else if (binding.etPassword.text.toString().length < 6) {
            binding.tilPassword.error = getString(R.string.error_password_too_short)
            isValid = false
        } else {
            binding.tilPassword.error = null
        }
        
        return isValid
    }
    
    private fun performLogin() {
        // Show loading state
        setLoading(true)
        
        // Simulate network call
        binding.root.postDelayed({
            // This is a mock login. In a real app, you would make an API call here.
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            
            // Mock successful login
            if (email.isNotBlank() && password.isNotBlank()) {
                showToast(getString(R.string.login_successful))
                // TODO: Navigate to MainActivity
            } else {
                showToast(getString(R.string.login_failed))
            }
            
            // Hide loading state
            setLoading(false)
        }, 2000) // 2 seconds delay to simulate network call
    }
    
    private fun setLoading(isLoading: Boolean) {
        // Update button state
        binding.btnLogin.setButtonState(
            if (isLoading) LoadingButton.ButtonState.Loading 
            else LoadingButton.ButtonState.Normal
        )
        
        // Disable/enable inputs during loading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.tvForgotPassword.isEnabled = !isLoading
        binding.tvRegister.isEnabled = !isLoading
        binding.btnGoogle.isEnabled = !isLoading
        binding.btnFacebook.isEnabled = !isLoading
    }
    
    private fun showForgotPasswordDialog() {
        // TODO: Implement forgot password dialog
        showToast(getString(R.string.forgot_password_clicked))
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    companion object {
        private const val TAG = "LoginActivity"
    }
}
