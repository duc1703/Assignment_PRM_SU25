package com.example.assignment_prm_su25;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.ui.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvProducts = findViewById(R.id.rvProducts);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize product list and adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);

        // Set up RecyclerView
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);

        // Populate product list with sample data
        populateProducts();

        // Set up BottomNavigationView listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    showToast("Home selected");
                    return true;
                } else if (itemId == R.id.nav_search) {
                    showToast("Search selected");
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    showToast("Notifications selected");
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    showToast("Profile selected");
                    return true;
                }
                return false;
            }
        });
    }

    private void populateProducts() {
        // Add sample products
        productList.add(new Product(1, "Apple iPhone 14 Pro", "The ultimate iPhone.", 999.99, "https://www.apple.com/v/iphone-14-pro/c/images/overview/hero/hero_iphone_14_pro__e0act2165xqq_large.jpg", 4.8f, 1));
        productList.add(new Product(2, "Samsung Galaxy S23 Ultra", "The best of Samsung.", 1199.99, "https://images.samsung.com/us/smartphones/galaxy-s23-ultra/images/galaxy-s23-ultra-highlights-kv.jpg", 4.7f, 1));
        productList.add(new Product(3, "Google Pixel 7 Pro", "The all-pro Google phone.", 899.99, "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/Google_Pixel_7_Pro.width-1200.height-630.mode-crop.jpg", 4.6f, 1));
        productList.add(new Product(4, "OnePlus 11", "The shape of power.", 699.99, "https://oasis.op-mobile.opera.com/op-website-wordpress/wp-content/uploads/2023/01/11.png", 4.5f, 1));
        productList.add(new Product(5, "Sony WH-1000XM5", "Industry-leading noise cancellation.", 399.99, "https://www.sony.co.uk/image/5d09395f6e9b3c2a3e6f6c31d0b74c5a?fmt=pjpeg&wid=1200&hei=470&bgcolor=F1F5F9&bgc=F1F5F9", 4.9f, 2));
        productList.add(new Product(6, "Bose QuietComfort 45", "The comfort you love.", 329.99, "https://assets.bose.com/content/dam/Bose_DAM/Web/consumer_electronics/global/products/headphones/qc45/product_silo_images/qc45-le-white-smoke-1-1.psd/jcr:content/renditions/cq5dam.web.1280.1280.jpeg", 4.8f, 2));
        productAdapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}