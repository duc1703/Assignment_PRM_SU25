package com.example.assignment_prm_su25.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;
import com.google.android.material.button.MaterialButton;
// import com.google.android.material.textfield.TextInputEditText;

public class UserEditActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etRole;
    private MaterialButton btnSave;
    private UserDatabaseHelper dbHelper;
    private int userId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        etName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etUserEmail);
        etPassword = (EditText) findViewById(R.id.etUserPassword);
        etRole = (EditText) findViewById(R.id.etUserRole);
        btnSave = findViewById(R.id.btnSaveUser);
        dbHelper = UserDatabaseHelper.getInstance(this);

        if (getIntent().hasExtra("user_id")) {
            userId = getIntent().getIntExtra("user_id", -1);
            User user = dbHelper.getUserById(userId);
            if (user != null) {
                etName.setText(user.getName());
                etEmail.setText(user.getEmail());
                etPassword.setText(user.getPassword());
                etRole.setText(user.getRole());
            }
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = etRole.getText().toString().trim();
            String phone = "";
            String address = "";
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(role)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userId == -1) {
                // Thêm mới
                User user = new User(name, email, password, role, phone, address);
                boolean success = dbHelper.addUser(user);
                if (success) {
                    Toast.makeText(this, "Đã thêm tài khoản", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Sửa
                User user = new User(userId, name, email, password, role, phone, address);
                boolean success = dbHelper.updateUser(user);
                if (success) {
                    Toast.makeText(this, "Đã cập nhật tài khoản", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
