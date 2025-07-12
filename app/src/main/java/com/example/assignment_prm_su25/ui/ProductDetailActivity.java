package com.example.assignment_prm_su25.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvName, tvDescription, tvPrice, tvBrand, tvSize, tvColor, tvStock, tvImageUrl;
    private UserDatabaseHelper databaseHelper;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        databaseHelper = new UserDatabaseHelper(this);
        
        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            finish();
            return;
        }

        product = databaseHelper.getProductById(productId);
        if (product == null) {
            finish();
            return;
        }

        initViews();
        loadProductData();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvProductName);
        tvDescription = findViewById(R.id.tvProductDescription);
        tvPrice = findViewById(R.id.tvProductPrice);
        tvBrand = findViewById(R.id.tvProductBrand);
        tvSize = findViewById(R.id.tvProductSize);
        tvColor = findViewById(R.id.tvProductColor);
        tvStock = findViewById(R.id.tvProductStock);
        tvImageUrl = findViewById(R.id.tvProductImageUrl);
    }

    private void loadProductData() {
        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        tvPrice.setText(String.format("$%.2f", product.getPrice()));
        tvBrand.setText(product.getBrand());
        tvSize.setText(product.getSize());
        tvColor.setText(product.getColor());
        tvStock.setText(String.valueOf(product.getStock()));
        tvImageUrl.setText(product.getImageUrl());
    }
} 