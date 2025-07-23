package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;
import es.dmoral.toasty.Toasty;
import java.security.SecureRandom;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnSend;
    private TextView tvStatus;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtEmail);
        btnSend = findViewById(R.id.btnSend);
        tvStatus = findViewById(R.id.tvStatus);
        dbHelper = UserDatabaseHelper.getInstance(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    showStatus("⚠️ Vui lòng nhập email của bạn");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showStatus("⚠️ Email không hợp lệ. Vui lòng kiểm tra lại!");
                } else {
                    User user = dbHelper.getUserByEmail(email);
                    if (user == null) {
                        showStatus("⚠️ Email không tồn tại trong hệ thống!");
                    } else {
                        // Hiển thị trạng thái đang xử lý
                        showStatus("⏳ Đang xử lý yêu cầu...");
                        btnSend.setEnabled(false);
                        
                        String newPassword = generateRandomPassword();
                        boolean updated = dbHelper.updatePassword(email, newPassword);
                        
                        if (updated) {
                            showStatus("✅ Đã tạo mật khẩu mới. Hãy kiểm tra email của bạn!");
                            Toasty.success(ForgotPasswordActivity.this, "Đã tạo mật khẩu mới. Hãy kiểm tra email của bạn!", Toasty.LENGTH_LONG, true).show();
                            
                            // Tự động chuyển về màn hình đăng nhập sau 2 giây
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        } else {
                            showStatus("❌ Lỗi cập nhật mật khẩu. Vui lòng thử lại!");
                            Toasty.error(ForgotPasswordActivity.this, "Lỗi cập nhật mật khẩu!", Toasty.LENGTH_SHORT, true).show();
                        }
                        
                        // Kích hoạt lại nút sau khi xử lý xong
                        btnSend.setEnabled(true);
                    }
                }
                        } else {
                            Toasty.error(ForgotPasswordActivity.this, "Lỗi cập nhật mật khẩu!", Toasty.LENGTH_SHORT, true).show();
                        }
                    }
                }
            }
        });
    }

    // Hàm sinh mật khẩu ngẫu nhiên mạnh
    // Hiển thị trạng thái xử lý
    private void showStatus(String message) {
        tvStatus.setText(message);
        tvStatus.setVisibility(View.VISIBLE);
    }

    // Xóa trạng thái hiện tại
    private void clearStatus() {
        tvStatus.setText("");
        tvStatus.setVisibility(View.GONE);
    }

    private String generateRandomPassword() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}