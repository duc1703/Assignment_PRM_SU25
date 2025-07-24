// OrderDetailActivity.java
package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout; // Đảm bảo đã có
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.Adapter.OrderItemAdapter;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderIdDetail, tvOrderDateDetail, tvOrderStatusDetail, tvOrderTotalDetail, tvShippingAddress, tvPhoneNumber;
    private RecyclerView rvOrderItems;
    private OrderItemAdapter orderItemAdapter;
    private UserDatabaseHelper dbHelper;
    private int orderId;
    private LinearLayout layoutContactSupport; // Khai báo LinearLayout

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Order Details");
        }

        tvOrderIdDetail = findViewById(R.id.tvOrderIdDetail);
        tvOrderDateDetail = findViewById(R.id.tvOrderDateDetail);
        tvOrderStatusDetail = findViewById(R.id.tvOrderStatusDetail);
        tvOrderTotalDetail = findViewById(R.id.tvOrderTotalDetail);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        dbHelper = UserDatabaseHelper.getInstance(this);
        layoutContactSupport = findViewById(R.id.layoutContactSupport); // Tìm kiếm LinearLayout từ XML

        orderId = getIntent().getIntExtra("order_id", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        loadOrderDetail();

        // Thiết lập OnClickListener để mở ContactSupportActivity
        layoutContactSupport.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, ContactSupportActivity.class);
            intent.putExtra("order_id_for_support", orderId); // Truyền ID đơn hàng
            startActivity(intent);
        });
    }

    private void loadOrderDetail() {
        Order order = dbHelper.getOrderById(orderId);
        if (order != null) {
            tvOrderIdDetail.setText(String.format("Mã đơn hàng: #%d", order.getId()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvOrderDateDetail.setText(String.format("Ngày đặt: %s", sdf.format(new Date(order.getOrderDate()))));
            tvOrderStatusDetail.setText(String.format("Trạng thái: %s", order.getStatus()));
            tvOrderTotalDetail.setText(String.format("Tổng cộng: %.2f₫", order.getTotalAmount()));
            tvShippingAddress.setText(String.format("Địa chỉ giao hàng: %s", order.getShippingAddress()));
            tvPhoneNumber.setText(String.format("Số điện thoại: %s", order.getPhoneNumber()));

            List<OrderItem> orderItems = dbHelper.getOrderItemsByOrderId(orderId); // Đảm bảo OrderItem là từ gói model
            orderItemAdapter = new OrderItemAdapter(this, orderItems);
            rvOrderItems.setAdapter(orderItemAdapter);
        } else {
            Toast.makeText(this, "Không tìm thấy chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
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