package com.example.assignment_prm_su25.ui;
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

import com.example.assignment_prm_su25.Adapter.SupportMessageAdapter;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.SupportMessage;

import java.util.List;

public class SupportMessageListActivity extends AppCompatActivity {

    private RecyclerView rvSupportMessages;
    private SupportMessageAdapter adapter;
    private UserDatabaseHelper dbHelper;
    private TextView tvNoSupportMessages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_message_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tin nhắn hỗ trợ (Admin)");
        }

        rvSupportMessages = findViewById(R.id.rvSupportMessages);
        tvNoSupportMessages = findViewById(R.id.tvNoSupportMessages);
        dbHelper = UserDatabaseHelper.getInstance(this);

        rvSupportMessages.setLayoutManager(new LinearLayoutManager(this));
        loadMessages();
    }

    private void loadMessages() {
        List<SupportMessage> messages = dbHelper.getAllSupportMessages();
        if (messages.isEmpty()) {
            tvNoSupportMessages.setVisibility(TextView.VISIBLE);
            rvSupportMessages.setVisibility(RecyclerView.GONE);
        } else {
            tvNoSupportMessages.setVisibility(TextView.GONE);
            rvSupportMessages.setVisibility(RecyclerView.VISIBLE);
            adapter = new SupportMessageAdapter(this, messages, dbHelper);
            rvSupportMessages.setAdapter(adapter);

            adapter.setOnItemClickListener(message -> {
                // TODO: Implement logic to view full message details or change status
                Toast.makeText(SupportMessageListActivity.this, "Xem chi tiết tin nhắn: " + message.getSubject(), Toast.LENGTH_SHORT).show();
            });
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
        loadMessages(); // Tải lại tin nhắn khi quay lại màn hình
    }
}