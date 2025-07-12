package com.example.assignment_prm_su25.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Product;

public class EditProductActivity extends AppCompatActivity {
    private EditText etName, etDescription, etPrice, etBrand, etSize, etColor, etStock, etImageUrl;
    private Button btnUpdate, btnCancel;
    private UserDatabaseHelper databaseHelper;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        databaseHelper = new UserDatabaseHelper(this);
        
        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        product = databaseHelper.getProductById(productId);
        if (product == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadProductData();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etProductDescription);
        etPrice = findViewById(R.id.etProductPrice);
        etBrand = findViewById(R.id.etProductBrand);
        etSize = findViewById(R.id.etProductSize);
        etColor = findViewById(R.id.etProductColor);
        etStock = findViewById(R.id.etProductStock);
        etImageUrl = findViewById(R.id.etProductImageUrl);
        btnUpdate = findViewById(R.id.btnUpdateProduct);
        btnCancel = findViewById(R.id.btnCancelProduct);
    }

    private void loadProductData() {
        etName.setText(product.getName());
        etDescription.setText(product.getDescription());
        etPrice.setText(String.valueOf(product.getPrice()));
        etBrand.setText(product.getBrand());
        etSize.setText(product.getSize());
        etColor.setText(product.getColor());
        etStock.setText(String.valueOf(product.getStock()));
        etImageUrl.setText(product.getImageUrl());
    }

    private void setupListeners() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateProduct() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String size = etSize.getText().toString().trim();
        String color = etColor.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập tên sản phẩm");
            return;
        }
        if (priceStr.isEmpty()) {
            etPrice.setError("Vui lòng nhập giá");
            return;
        }
        if (brand.isEmpty()) {
            etBrand.setError("Vui lòng nhập thương hiệu");
            return;
        }
        if (stockStr.isEmpty()) {
            etStock.setError("Vui lòng nhập số lượng");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setBrand(brand);
            product.setSize(size);
            product.setColor(color);
            product.setStock(stock);
            product.setImageUrl(imageUrl);
            
            if (databaseHelper.updateProduct(product)) {
                Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();
        }
    }
} 