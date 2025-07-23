// OrderListActivity.java
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

import com.example.assignment_prm_su25.Adapter.OrderAdapter;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Order;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private UserDatabaseHelper dbHelper;
    private int userId;
    private TextView tvNoOrders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Orders");
        }

        rvOrders = findViewById(R.id.rvOrders);
        tvNoOrders = findViewById(R.id.tvNoOrders);
        dbHelper = UserDatabaseHelper.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        loadOrders();
    }

    private void loadOrders() {
        if (userId != -1) {
            List<Order> orders = dbHelper.getOrdersByUserId(userId);
            if (orders.isEmpty()) {
                tvNoOrders.setVisibility(TextView.VISIBLE);
                rvOrders.setVisibility(RecyclerView.GONE);
            } else {
                tvNoOrders.setVisibility(TextView.GONE);
                rvOrders.setVisibility(RecyclerView.VISIBLE);
                orderAdapter = new OrderAdapter(this, orders);
                rvOrders.setAdapter(orderAdapter);

                orderAdapter.setOnItemClickListener(order -> {
                    // Navigate to OrderDetailActivity
                    Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                    intent.putExtra("order_id", order.getId());
                    startActivity(intent);
                });
            }
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập để xem đơn hàng", Toast.LENGTH_SHORT).show();
            tvNoOrders.setVisibility(TextView.VISIBLE);
            rvOrders.setVisibility(RecyclerView.GONE);
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
        loadOrders(); // Refresh orders in case of changes
    }
}