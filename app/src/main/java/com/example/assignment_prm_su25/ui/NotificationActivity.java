// app/src/main/java/com/example/assignment_prm_su25/ui/NotificationActivity.java
package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.Adapter.NotificationAdapter; // Import adapter
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Notification;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private UserDatabaseHelper dbHelper;
    private int userId;
    private TextView tvNoNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notifications");
        }

        rvNotifications = findViewById(R.id.rvNotifications);
        tvNoNotifications = findViewById(R.id.tvNoNotifications);
        dbHelper = UserDatabaseHelper.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        loadNotifications();
    }

    private void loadNotifications() {
        if (userId != -1) {
            List<Notification> notifications = dbHelper.getNotificationsByUserId(userId);
            if (notifications.isEmpty()) {
                tvNoNotifications.setVisibility(TextView.VISIBLE);
                rvNotifications.setVisibility(RecyclerView.GONE);
            } else {
                tvNoNotifications.setVisibility(TextView.GONE);
                rvNotifications.setVisibility(RecyclerView.VISIBLE);
                adapter = new NotificationAdapter(this, notifications);
                rvNotifications.setAdapter(adapter);

                adapter.setOnItemClickListener(notification -> {
                    // Mark notification as read when clicked
                    dbHelper.markNotificationAsRead(notification.getId());
                    notification.setRead(true); // Cập nhật trạng thái trong đối tượng
                    adapter.notifyItemChanged(notifications.indexOf(notification)); // Cập nhật UI cho mục đó

                    // TODO: Có thể mở màn hình chi tiết tin nhắn hỗ trợ nếu type là "admin_reply"
                    if ("admin_reply".equals(notification.getType())) {
                        // Mở SupportMessageDetailActivity
                        Intent intent = new Intent(NotificationActivity.this, SupportMessageDetailActivity.class);
                        intent.putExtra("message_id", notification.getRelatedId());
                        startActivity(intent);
                    } else {
                        Toast.makeText(NotificationActivity.this, "Thông báo: " + notification.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập để xem thông báo", Toast.LENGTH_SHORT).show();
            tvNoNotifications.setVisibility(TextView.VISIBLE);
            rvNotifications.setVisibility(RecyclerView.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications(); // Tải lại thông báo khi quay lại màn hình
    }
}