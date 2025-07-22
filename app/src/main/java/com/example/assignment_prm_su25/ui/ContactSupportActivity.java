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

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.SupportMessage;
import com.example.assignment_prm_su25.model.User;

public class ContactSupportActivity extends AppCompatActivity {

    private EditText edtSubject, edtMessage;
    private Button btnSendMessage;
    private TextView tvOrderReference;
    private UserDatabaseHelper dbHelper;
    private int userId;
    private int orderIdReference = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_support);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Liên hệ hỗ trợ");
        }

        edtSubject = findViewById(R.id.edtSubject);
        edtMessage = findViewById(R.id.edtMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        tvOrderReference = findViewById(R.id.tvOrderReference);

        dbHelper = UserDatabaseHelper.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1); // Lấy ID người dùng đã đăng nhập

        // Lấy orderId từ Intent nếu có (khi liên hệ từ chi tiết đơn hàng)
        orderIdReference = getIntent().getIntExtra("order_id_for_support", -1);
        if (orderIdReference != -1) {
            tvOrderReference.setVisibility(View.VISIBLE);
            tvOrderReference.setText("Đơn hàng tham chiếu: #" + orderIdReference);
            edtSubject.setText("Hỗ trợ về đơn hàng #" + orderIdReference); // Gợi ý chủ đề
        } else {
            tvOrderReference.setVisibility(View.GONE);
        }

        btnSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để gửi tin nhắn hỗ trợ.", Toast.LENGTH_SHORT).show();
            return;
        }

        String subject = edtSubject.getText().toString().trim();
        String message = edtMessage.getText().toString().trim();

        if (TextUtils.isEmpty(subject)) {
            edtSubject.setError("Vui lòng nhập chủ đề");
            return;
        }
        if (TextUtils.isEmpty(message)) {
            edtMessage.setError("Vui lòng nhập nội dung tin nhắn");
            return;
        }

        SupportMessage supportMessage = new SupportMessage(
                0, // ID sẽ tự động tăng trong DB
                userId,
                orderIdReference, // orderId có thể là -1 nếu không liên quan đến đơn hàng cụ thể
                subject,
                message,
                System.currentTimeMillis(), // Thời gian gửi tin nhắn
                "New" // Trạng thái ban đầu
        );

        boolean success = dbHelper.addSupportMessage(supportMessage); // Gọi phương thức thêm vào DB

        if (success) {
            Toast.makeText(this, "Tin nhắn của bạn đã được gửi. Chúng tôi sẽ liên hệ lại sớm!", Toast.LENGTH_LONG).show();
            edtSubject.setText(""); // Xóa nội dung đã nhập
            edtMessage.setText("");
            if (orderIdReference == -1) {
                edtSubject.setText(""); // Chỉ xóa chủ đề nếu không phải từ chi tiết đơn hàng
            }
            finish(); // Đóng màn hình sau khi gửi thành công
        } else {
            Toast.makeText(this, "Gửi tin nhắn thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
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