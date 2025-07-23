package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.MainActivity;
import com.example.assignment_prm_su25.model.Cart;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartUpdateListener {
    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private UserDatabaseHelper dbHelper;
    private int userId;
    private TextView tvEmptyCart;
    private TextView tvTotalAmount;
    private View cartItemsContainer;
    private View emptyCartView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        rvCartItems = findViewById(R.id.rvCartItems);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        cartItemsContainer = findViewById(R.id.cartItemsContainer);
        emptyCartView = findViewById(R.id.emptyCartView);
        
        // Setup toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Setup start shopping button
        findViewById(R.id.btnStartShopping).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        
        dbHelper = UserDatabaseHelper.getInstance(this);

        // Get user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        loadCartItems();
    }

    private void loadCartItems() {
        List<Cart> cartItems = dbHelper.getCartItems(userId);
        cartAdapter = new CartAdapter(this, cartItems, this);
        rvCartItems.setAdapter(cartAdapter);
        
        if (cartItems.isEmpty()) {
            showEmptyCartView();
        } else {
            showCartItemsView();
            updateTotalAmount(cartItems);
        }
    }
    
    private void showEmptyCartView() {
        cartItemsContainer.setVisibility(View.GONE);
        emptyCartView.setVisibility(View.VISIBLE);
    }
    
    private void showCartItemsView() {
        cartItemsContainer.setVisibility(View.VISIBLE);
        emptyCartView.setVisibility(View.GONE);
    }
    
    private void updateTotalAmount(List<Cart> cartItems) {
        double total = 0;
        for (Cart cart : cartItems) {
            if (cart.getProduct() != null) {
                total += cart.getProduct().getPrice() * cart.getQuantity();
            }
        }
        tvTotalAmount.setText(String.format("$%.2f", total));
    }
    
    @Override
    public void onCartUpdated() {
        List<Cart> updatedCart = dbHelper.getCartItems(userId);
        if (updatedCart.isEmpty()) {
            showEmptyCartView();
        } else {
            showCartItemsView();
            updateTotalAmount(updatedCart);
        }
        cartAdapter.updateCartItems(updatedCart);
    }
}