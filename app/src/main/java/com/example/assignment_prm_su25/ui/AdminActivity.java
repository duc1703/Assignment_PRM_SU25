package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView rvUserList;
    private UserAdapter userAdapter;
    private UserDatabaseHelper dbHelper;
    private MaterialButton btnAddUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rvUserList = findViewById(R.id.rvUserList);
        btnAddUser = findViewById(R.id.btnAddUser);
        dbHelper = UserDatabaseHelper.getInstance(this);

        rvUserList.setLayoutManager(new LinearLayoutManager(this));
        loadUsers();

        btnAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, UserEditActivity.class);
            startActivity(intent);
        });
    }

    private void loadUsers() {
        List<User> userList = dbHelper.getAllUsers();
        userAdapter = new UserAdapter(this, userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(User user) {
                Intent intent = new Intent(AdminActivity.this, UserEditActivity.class);
                intent.putExtra("user_id", user.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(User user) {
                dbHelper.deleteUser(user.getId());
                Toast.makeText(AdminActivity.this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                loadUsers();
            }
        });
        rvUserList.setAdapter(userAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }
}
