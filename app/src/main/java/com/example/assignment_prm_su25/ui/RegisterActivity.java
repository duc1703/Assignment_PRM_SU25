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

public class RegisterActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new UserDatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                String role = "user";
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toasty.warning(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toasty.LENGTH_SHORT, true).show();
                } else if (!password.equals(confirmPassword)) {
                    Toasty.error(RegisterActivity.this, "Mật khẩu xác nhận không khớp", Toasty.LENGTH_SHORT, true).show();
                } else if (dbHelper.getUserByEmail(email) != null) {
                    Toasty.error(RegisterActivity.this, "Email đã tồn tại!", Toasty.LENGTH_SHORT, true).show();
                } else {
                    User user = new User(name, email, password, role);
                    boolean success = dbHelper.addUser(user);
                    if (success) {
                        Toasty.success(RegisterActivity.this, "Đăng ký thành công!", Toasty.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toasty.error(RegisterActivity.this, "Đăng ký thất bại!", Toasty.LENGTH_SHORT, true).show();
                    }
                }
            }
        });
    }
} 