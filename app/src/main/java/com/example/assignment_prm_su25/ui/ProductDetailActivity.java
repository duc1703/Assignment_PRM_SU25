package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            finish();
            return;
        }
        UserDatabaseHelper dbHelper = UserDatabaseHelper.getInstance(this);
        Product product = dbHelper.getProductById(productId);
        if (product == null) {
            finish();
            return;
        }
        ImageView imgProduct = findViewById(R.id.imgProductDetail);
        TextView tvName = findViewById(R.id.tvProductNameDetail);
        TextView tvPrice = findViewById(R.id.tvProductPriceDetail);
        TextView tvDesc = findViewById(R.id.tvProductDescDetail);
        Glide.with(this).load(product.getImageUrl()).into(imgProduct);
        tvName.setText(product.getName());
        tvPrice.setText(String.format("$%.2f", product.getPrice()));
        tvDesc.setText(product.getDescription());
    }
}
