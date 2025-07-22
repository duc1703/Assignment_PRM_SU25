package com.example.assignment_prm_su25.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.Adapter.SupportResponseAdapter; // Import adapter
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.SupportMessage;
import com.example.assignment_prm_su25.model.SupportResponse;
import com.example.assignment_prm_su25.model.User; // Để lấy tên người dùng

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupportMessageDetailActivity extends AppCompatActivity {

    private TextView tvDetailSubject, tvDetailSender, tvDetailTimestamp, tvDetailMessage;
    private EditText edtAdminReply;
    private Button btnSendReply;
    private RecyclerView rvResponses; // Để hiển thị các câu trả lời
    private SupportResponseAdapter responseAdapter;

    private UserDatabaseHelper dbHelper;
    private SupportMessage currentMessage;
    private int currentMessageId;
    private int adminId; // ID của admin đang đăng nhập

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_message_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết tin nhắn");
        }

        tvDetailSubject = findViewById(R.id.tvDetailSubject);
        tvDetailSender = findViewById(R.id.tvDetailSender);
        tvDetailTimestamp = findViewById(R.id.tvDetailTimestamp);
        tvDetailMessage = findViewById(R.id.tvDetailMessage);
        edtAdminReply = findViewById(R.id.edtAdminReply);
        btnSendReply = findViewById(R.id.btnSendReply);
        rvResponses = findViewById(R.id.rvResponses);

        dbHelper = UserDatabaseHelper.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        adminId = prefs.getInt("user_id", -1); // Lấy ID của admin đang đăng nhập

        currentMessageId = getIntent().getIntExtra("message_id", -1);
        if (currentMessageId == -1) {
            Toast.makeText(this, "Không tìm thấy tin nhắn.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rvResponses.setLayoutManager(new LinearLayoutManager(this));

        loadMessageDetails();
        loadResponses();

        btnSendReply.setOnClickListener(v -> sendAdminReply());
    }

    private void loadMessageDetails() {
        currentMessage = dbHelper.getSupportMessageById(currentMessageId);
        if (currentMessage != null) {
            tvDetailSubject.setText("Chủ đề: " + currentMessage.getSubject());
            tvDetailMessage.setText(currentMessage.getMessage());

            User sender = dbHelper.getUserById(currentMessage.getUserId());
            String senderInfo = (sender != null ? sender.getName() : "Người dùng ẩn danh");
            if (currentMessage.getOrderId() != -1) {
                senderInfo += " (Đơn hàng #" + currentMessage.getOrderId() + ")";
            }
            tvDetailSender.setText("Từ: " + senderInfo);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvDetailTimestamp.setText("Ngày: " + sdf.format(new Date(currentMessage.getTimestamp())));

            // Có thể cập nhật trạng thái tin nhắn thành "Read" khi admin mở xem
            if ("New".equals(currentMessage.getStatus())) {
                dbHelper.updateSupportMessageStatus(currentMessage.getId(), "Read");
                // Cần thông báo cho SupportMessageListActivity để cập nhật list
                // Ví dụ: Gửi Broadcast hoặc dùng Result API
            }

        } else {
            Toast.makeText(this, "Không tìm thấy chi tiết tin nhắn.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadResponses() {
        List<SupportResponse> responses = dbHelper.getSupportResponsesByMessageId(currentMessageId);
        responseAdapter = new SupportResponseAdapter(this, responses, dbHelper);
        rvResponses.setAdapter(responseAdapter);
        // Cuộn xuống cuối danh sách nếu có nhiều phản hồi
        if (responses != null && !responses.isEmpty()) {
            rvResponses.scrollToPosition(responses.size() - 1);
        }
    }

    private void sendAdminReply() {
        if (adminId == -1) {
            Toast.makeText(this, "Bạn cần đăng nhập với tài khoản Admin để trả lời.", Toast.LENGTH_SHORT).show();
            return;
        }

        String replyContent = edtAdminReply.getText().toString().trim();
        if (TextUtils.isEmpty(replyContent)) {
            edtAdminReply.setError("Vui lòng nhập nội dung trả lời.");
            return;
        }

        SupportResponse response = new SupportResponse(
                0, // ID sẽ tự động tăng
                currentMessageId,
                adminId,
                replyContent,
                System.currentTimeMillis()
        );

        boolean success = dbHelper.addSupportResponse(response);
        if (success) {
            Toast.makeText(this, "Câu trả lời đã được gửi!", Toast.LENGTH_SHORT).show();
            edtAdminReply.setText(""); // Xóa nội dung đã nhập
            loadResponses(); // Tải lại danh sách câu trả lời
            // currentMessage.setStatus("Replied"); // Trạng thái đã được cập nhật trong addSupportResponse
        } else {
            Toast.makeText(this, "Gửi trả lời thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
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
}