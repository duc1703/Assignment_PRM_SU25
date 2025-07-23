package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.model.ProductVariant;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private UserDatabaseHelper dbHelper;
    private Product product;
    private int userId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            finish();
            return;
        }

        dbHelper = UserDatabaseHelper.getInstance(this);
        product = dbHelper.getProductById(productId);
        if (product == null) {
            finish();
            return;
        }

        // Initialize views
        ImageView imgProduct = findViewById(R.id.imgProductDetail);
        TextView tvName = findViewById(R.id.tvProductNameDetail);
        TextView tvPrice = findViewById(R.id.tvProductPriceDetail);
        TextView tvDesc = findViewById(R.id.tvProductDescDetail);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        RecyclerView rvVariants = findViewById(R.id.rvVariants);

        // Load product data
        Glide.with(this)
            .load(product.getImageUrl())
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(imgProduct);
        
        tvName.setText(product.getName());
        tvPrice.setText(String.format("$%.2f", product.getPrice()));
        tvDesc.setText(product.getDescription());
        ratingBar.setRating(product.getRating());

        // Load variants
        List<ProductVariant> variants = dbHelper.getProductVariantsByProductId(productId);
        if (!variants.isEmpty()) {
            rvVariants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            ProductVariantAdapter variantAdapter = new ProductVariantAdapter(variants);
            rvVariants.setAdapter(variantAdapter);
        }

        // Add to cart button click
        btnAddToCart.setOnClickListener(v -> {
            if (userId != -1) {
                boolean success = dbHelper.addToCart(userId, productId, 1);
                if (success) {
                    Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Không thể thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Animation khi quay về
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Animation khi nhấn nút back
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
