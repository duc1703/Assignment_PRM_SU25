package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edtEmail, edtCurrentPassword, edtNewPassword, edtConfirmNewPassword;
    private Button btnChangePassword;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtEmail = new EditText(this);
        edtEmail.setHint("Email");
        edtEmail.setId(View.generateViewId());
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        dbHelper = new UserDatabaseHelper(this);

        // Thêm EditText email vào layout nếu chưa có
        // (Nếu bạn đã có trường email trong layout thì bỏ đoạn này và dùng findViewById)

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String current = edtCurrentPassword.getText().toString();
                String newPass = edtNewPassword.getText().toString();
                String confirm = edtConfirmNewPassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(current) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirm)) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(confirm)) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    User user = dbHelper.checkUserLogin(email, current);
                    if (user == null) {
                        Toast.makeText(ChangePasswordActivity.this, "Email hoặc mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean success = dbHelper.updatePassword(email, newPass);
                        if (success) {
                            Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
} 