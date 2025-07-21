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
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtEmail);
        btnSend = findViewById(R.id.btnSend);
        dbHelper = UserDatabaseHelper.getInstance(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toasty.warning(ForgotPasswordActivity.this, "Vui lòng nhập email", Toasty.LENGTH_SHORT, true).show();
                } else {
                    User user = dbHelper.getUserByEmail(email);
                    if (user == null) {
                        Toasty.error(ForgotPasswordActivity.this, "Email không tồn tại!", Toasty.LENGTH_SHORT, true).show();
                    } else {
                        String newPassword = generateRandomPassword(10);
                        boolean updated = dbHelper.updatePassword(email, newPassword);
                        if (updated) {
                            // Gửi email qua Intent
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("message/rfc822");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mật khẩu mới cho tài khoản Sneaker Shop");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Mật khẩu mới của bạn là: " + newPassword + "\nVui lòng đăng nhập và đổi lại mật khẩu!");
                            try {
                                startActivity(Intent.createChooser(emailIntent, "Gửi email bằng..."));
                                Toasty.success(ForgotPasswordActivity.this, "Đã tạo mật khẩu mới và mở email gửi cho bạn!", Toasty.LENGTH_LONG, true).show();
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toasty.error(ForgotPasswordActivity.this, "Không tìm thấy ứng dụng gửi email!", Toasty.LENGTH_SHORT, true).show();
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
    private String generateRandomPassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
} 