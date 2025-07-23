package com.example.assignment_prm_su25.ui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.Adapter.CartAdapter;
import com.google.android.material.button.MaterialButton;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Cart;
import com.example.assignment_prm_su25.model.Product;

import java.util.List;
import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private CartAdapter adapter;
    private UserDatabaseHelper dbHelper;
    private int userId;
    private TextView tvTotalPrice;
    private MaterialButton btnCheckout;
    private List<Cart> currentCartList; // To store the current cart items

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Shopping Cart");
        }

        rvCartItems = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        dbHelper = UserDatabaseHelper.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            if (currentCartList != null && !currentCartList.isEmpty()) {
                showCheckoutDialog();
            } else {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartItems() {
        if (userId != -1) {
            currentCartList = dbHelper.getCartItems(userId);
            adapter = new CartAdapter(this, currentCartList);
            rvCartItems.setAdapter(adapter);
            calculateTotalPrice();
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
            tvTotalPrice.setText("Tổng tiền: 0.00₫");
            btnCheckout.setEnabled(false);
        }
    }

    private void calculateTotalPrice() {
        double total = 0;
        if (currentCartList != null) {
            for (Cart cart : currentCartList) {
                Product product = dbHelper.getProductById(cart.getProductId());
                if (product != null) {
                    total += product.getPrice() * cart.getQuantity();
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        tvTotalPrice.setText(String.format("Tổng tiền: %s₫", formatter.format(total)));
    }

    private void showCheckoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_checkout, null);
        builder.setView(dialogView);

        final EditText edtShippingAddress = dialogView.findViewById(R.id.edtShippingAddress);
        final EditText edtPhoneNumber = dialogView.findViewById(R.id.edtPhoneNumber);
        TextView tvCheckoutTotal = dialogView.findViewById(R.id.tvCheckoutTotal);

        // Pre-fill address and phone from user profile if available
        if (dbHelper.getUserById(userId) != null) { // You need to add getUserById method
            edtShippingAddress.setText(dbHelper.getUserById(userId).getAddress());
            edtPhoneNumber.setText(dbHelper.getUserById(userId).getPhone());
        }


        // Calculate total price again for the dialog
        double total = 0;
        if (currentCartList != null) {
            for (Cart cart : currentCartList) {
                Product product = dbHelper.getProductById(cart.getProductId());
                if (product != null) {
                    total += product.getPrice() * cart.getQuantity();
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        tvCheckoutTotal.setText(String.format("Tổng tiền: %s₫", formatter.format(total)));

        builder.setTitle("Xác nhận thanh toán");
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String shippingAddress = edtShippingAddress.getText().toString().trim();
            String phoneNumber = edtPhoneNumber.getText().toString().trim();

            if (shippingAddress.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui lòng nhập địa chỉ và số điện thoại giao hàng.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Place the order
            boolean success = dbHelper.placeOrder(userId, shippingAddress, phoneNumber);
            if (success) {
                Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                // Clear cart and refresh UI
                loadCartItems();
                // Optionally navigate to order list
                // Intent intent = new Intent(CartActivity.this, OrderListActivity.class);
                // startActivity(intent);
            } else {
                Toast.makeText(CartActivity.this, "Đặt hàng thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
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
        loadCartItems(); // Refresh cart items on resume
    }
}