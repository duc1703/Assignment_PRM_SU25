package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements UserAdapter.OnUserListener {

    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private UserDatabaseHelper dbHelper;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = UserDatabaseHelper.getInstance(this);

        // Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Setup RecyclerView
        usersRecyclerView = findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup FAB
        FloatingActionButton fab = findViewById(R.id.add_user_fab);
        fab.setOnClickListener(view -> {
            // Intent to open UserEditActivity for adding a new user
            Intent intent = new Intent(AdminActivity.this, UserEditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers() {
        userList = dbHelper.getAllUsers(); // Assuming getAllUsers() exists in your DB Helper
        if (userList == null) {
            userList = new ArrayList<>();
        }
        userAdapter = new UserAdapter(userList, this);
        usersRecyclerView.setAdapter(userAdapter);
    }

    @Override
    public void onEditClick(int position) {
        User userToEdit = userList.get(position);
        Intent intent = new Intent(AdminActivity.this, UserEditActivity.class);
        intent.putExtra("USER_ID", userToEdit.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        User userToDelete = userList.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + userToDelete.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteUser(userToDelete.getId()); // Assuming deleteUser() exists
                    loadUsers(); // Refresh the list
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
