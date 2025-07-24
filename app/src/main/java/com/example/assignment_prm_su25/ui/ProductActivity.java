package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.Adapter.ProductAdapter;
import com.example.assignment_prm_su25.Adapter.ProductManageAdapter;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Category;
import com.example.assignment_prm_su25.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private UserDatabaseHelper dbHelper;
    private ProductManageAdapter adapter;
    private FloatingActionButton fabAddProduct;
    private AlertDialog dialog;
    private List<Category> categoryList;
    private ArrayAdapter<String> categoryAdapter;
    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Quản lý sản phẩm");

        RecyclerView rvProductList = findViewById(R.id.rvProductList);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        dbHelper = UserDatabaseHelper.getInstance(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProductList.setLayoutManager(layoutManager);
        adapter = new ProductManageAdapter(this, new ArrayList<>());
        rvProductList.setAdapter(adapter);

        loadProducts();

        fabAddProduct.setOnClickListener(v -> showProductDialog(null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_categories) {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_support_messages) {
            Intent intent = new Intent(this, SupportMessageListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_product, null);

        EditText edtName = view.findViewById(R.id.edtProductName);
        EditText edtDesc = view.findViewById(R.id.edtProductDescription);
        EditText edtPrice = view.findViewById(R.id.edtProductPrice);
        EditText edtImage = view.findViewById(R.id.edtProductImage);
        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);

        loadCategories(spinnerCategory);

        if(product != null) {
            edtName.setText(product.getName());
            edtDesc.setText(product.getDescription());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtImage.setText(product.getImageUrl());
            // Set spinner selection
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == product.getCategoryId()) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        }


        builder.setView(view)
                .setTitle(product == null ? "Thêm sản phẩm" : "Sửa sản phẩm")
                .setPositiveButton(product == null ? "Thêm" : "Cập nhật", (dialog, which) -> {
                    saveProduct(product, edtName, edtDesc, edtPrice, edtImage, spinnerCategory);
                })
                .setNegativeButton("Hủy", null);

        dialog = builder.create();
        dialog.show();
    }

    private void loadCategories(Spinner spinnerCategory) {
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
        adapter.setOnItemClickListener(new ProductManageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                showProductDialog(product);
            }

            @Override
            public void onEditClick(Product product) {
                showProductDialog(product);
            }

            @Override
            public void onDeleteClick(Product product) {
                new AlertDialog.Builder(ProductActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deleteProduct(product.getId());
                            loadProducts();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
    }

    private void saveProduct(Product product, EditText edtName, EditText edtDesc,
                             EditText edtPrice, EditText edtImage,
                             Spinner spinnerCategory) {
        String name = edtName.getText().toString().trim();
        String desc = edtDesc.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String image = edtImage.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || selectedCategoryId == -1) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);

            if(product == null) {
                // Thêm mới sản phẩm với rating = 0
                Product newProduct = new Product(0, name, desc, price, image, 0f, selectedCategoryId);
                boolean success = dbHelper.addProduct(newProduct);
                if(success) {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    loadProducts();
                }
            } else {
                // Cập nhật sản phẩm, giữ nguyên rating cũ
                product.setName(name);
                product.setDescription(desc);
                product.setPrice(price);
                product.setImageUrl(image);
                product.setCategoryId(selectedCategoryId);
                // Không thay đổi rating

                boolean success = dbHelper.updateProduct(product);
                if(success) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadProducts();
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}