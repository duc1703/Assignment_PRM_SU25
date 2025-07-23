package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Category;
import com.example.assignment_prm_su25.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private EditText edtProductName, edtProductDescription, edtProductPrice, edtProductImage;
    private Spinner spinnerCategory;
    private RatingBar ratingBar;
    private Button btnAddProduct, btnUpdateProduct, btnClearProduct;
    private RecyclerView rvProductList;
    private UserDatabaseHelper dbHelper;
    private ProductAdapter adapter;
    private Product selectedProduct = null;
    private List<Category> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductDescription = findViewById(R.id.edtProductDescription);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        edtProductImage = findViewById(R.id.edtProductImage);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ratingBar = findViewById(R.id.ratingBar);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnClearProduct = findViewById(R.id.btnClearProduct);
        rvProductList = findViewById(R.id.rvProductList);
        dbHelper = UserDatabaseHelper.getInstance(this);

        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, new ArrayList<>());
        rvProductList.setAdapter(adapter);

        loadCategories();
        loadProducts();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtProductName.getText().toString().trim();
                String desc = edtProductDescription.getText().toString().trim();
                String priceStr = edtProductPrice.getText().toString().trim();
                String image = edtProductImage.getText().toString().trim();
                float rate = ratingBar.getRating();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || selectedCategoryId == -1) {
                    Toast.makeText(ProductActivity.this, "Vui lòng nhập đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                    return;
                }
                double price = Double.parseDouble(priceStr);
                Product product = new Product(0, name, desc, price, image, rate, selectedCategoryId);
                boolean success = dbHelper.addProduct(product);
                if (success) {
                    Toast.makeText(ProductActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    clearInput();
                    loadProducts();
                } else {
                    Toast.makeText(ProductActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedProduct == null) {
                    Toast.makeText(ProductActivity.this, "Chọn sản phẩm để cập nhật", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = edtProductName.getText().toString().trim();
                String desc = edtProductDescription.getText().toString().trim();
                String priceStr = edtProductPrice.getText().toString().trim();
                String image = edtProductImage.getText().toString().trim();
                float rate = ratingBar.getRating();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || selectedCategoryId == -1) {
                    Toast.makeText(ProductActivity.this, "Vui lòng nhập đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                    return;
                }
                double price = Double.parseDouble(priceStr);
                selectedProduct.setName(name);
                selectedProduct.setDescription(desc);
                selectedProduct.setPrice(price);
                selectedProduct.setImageUrl(image);
                selectedProduct.setCategoryId(selectedCategoryId);
                selectedProduct.setRating(rate);
                boolean success = dbHelper.updateProduct(selectedProduct);
                if (success) {
                    Toast.makeText(ProductActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    clearInput();
                    loadProducts();
                } else {
                    Toast.makeText(ProductActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClearProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });
    }

    private void loadCategories() {
        categoryList = dbHelper.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category c : categoryList) {
            categoryNames.add(c.getName());
        }
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < categoryList.size()) {
                    selectedCategoryId = categoryList.get(position).getId();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1;
            }
        });
    }

    private void loadProducts() {
        List<Product> products = dbHelper.getAllProducts();
        adapter.setProducts(products);
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                selectedProduct = product;
                edtProductName.setText(product.getName());
                edtProductDescription.setText(product.getDescription());
                edtProductPrice.setText(String.valueOf(product.getPrice()));
                edtProductImage.setText(product.getImageUrl());
                ratingBar.setRating(product.getRating());
                // set spinner category
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryList.get(i).getId() == product.getCategoryId()) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }
            @Override
            public void onDeleteClick(Product product) {
                dbHelper.deleteProduct(product.getId());
                loadProducts();
                if (selectedProduct != null && selectedProduct.getId() == product.getId()) {
                    clearInput();
                }
            }

            @Override
            public void onAddToCartClick(Product product) {
                // Handle add to cart
            }

            @Override
            public void onViewDetailsClick(Product product) {
                // Open product details screen
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void clearInput() {
        edtProductName.setText("");
        edtProductDescription.setText("");
        edtProductPrice.setText("");
        edtProductImage.setText("");
        ratingBar.setRating(5);
        spinnerCategory.setSelection(0);
        selectedProduct = null;
    }
}