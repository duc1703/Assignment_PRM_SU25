package com.example.assignment_prm_su25.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Category;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private EditText edtCategoryName, edtCategoryDescription;
    private Button btnAddCategory, btnUpdateCategory, btnClearCategory;
    private RecyclerView rvCategoryList;
    private UserDatabaseHelper dbHelper;
    private CategoryAdapter adapter;
    private Category selectedCategory = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDescription = findViewById(R.id.edtCategoryDescription);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnUpdateCategory = findViewById(R.id.btnUpdateCategory);
        btnClearCategory = findViewById(R.id.btnClearCategory);
        rvCategoryList = findViewById(R.id.rvCategoryList);
        dbHelper = UserDatabaseHelper.getInstance(this);

        rvCategoryList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter();
        rvCategoryList.setAdapter(adapter);

        loadCategories();

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtCategoryName.getText().toString().trim();
                String desc = edtCategoryDescription.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }
                Category category = new Category(name, desc);
                boolean success = dbHelper.addCategory(category);
                if (success) {
                    Toast.makeText(CategoryActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    clearInput();
                    loadCategories();
                } else {
                    Toast.makeText(CategoryActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCategory == null) {
                    Toast.makeText(CategoryActivity.this, "Chọn danh mục để cập nhật", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = edtCategoryName.getText().toString().trim();
                String desc = edtCategoryDescription.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedCategory.setName(name);
                selectedCategory.setDescription(desc);
                boolean success = dbHelper.updateCategory(selectedCategory);
                if (success) {
                    Toast.makeText(CategoryActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    clearInput();
                    loadCategories();
                } else {
                    Toast.makeText(CategoryActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClearCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });
    }

    private void loadCategories() {
        List<Category> categories = dbHelper.getAllCategories();
        adapter.setCategories(categories);
        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                selectedCategory = category;
                edtCategoryName.setText(category.getName());
                edtCategoryDescription.setText(category.getDescription());
            }

            @Override
            public void onDeleteClick(Category category) {
                dbHelper.deleteCategory(category.getId());
                loadCategories();
                if (selectedCategory != null && selectedCategory.getId() == category.getId()) {
                    clearInput();
                }
            }
        });
    }

    private void clearInput() {
        edtCategoryName.setText("");
        edtCategoryDescription.setText("");
        selectedCategory = null;
    }
} 