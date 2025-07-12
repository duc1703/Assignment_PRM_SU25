package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private UserDatabaseHelper databaseHelper;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Set title
        setTitle("Quản lý Sản phẩm");

        databaseHelper = new UserDatabaseHelper(this);
        
        recyclerView = findViewById(R.id.recyclerViewProducts);
        fabAdd = findViewById(R.id.fabAddProduct);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
        
        loadProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Đăng xuất và quay về màn hình đăng nhập
            Intent intent = new Intent(ProductListActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = databaseHelper.getAllProducts();
        adapter = new ProductAdapter(products, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // Mở màn hình chi tiết sản phẩm
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }

            @Override
            public void onEditClick(Product product) {
                // Mở màn hình chỉnh sửa sản phẩm
                Intent intent = new Intent(ProductListActivity.this, EditProductActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Product product) {
                // Xóa sản phẩm
                if (databaseHelper.deleteProduct(product.getId())) {
                    Toast.makeText(ProductListActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ProductListActivity.this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
} 