package com.example.assignment_prm_su25.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Cart;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private CartAdapter adapter;
    private UserDatabaseHelper dbHelper;
    private int userId = 1; // Replace with actual user ID

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCartItems = findViewById(R.id.rvCartItems);
        dbHelper = UserDatabaseHelper.getInstance(this);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        loadCartItems();
    }

    private void loadCartItems() {
        List<Cart> cartList = dbHelper.getCartItems(userId);
        adapter = new CartAdapter(this, cartList);
        rvCartItems.setAdapter(adapter);
    }
}