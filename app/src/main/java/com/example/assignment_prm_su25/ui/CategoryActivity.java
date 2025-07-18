package com.example.assignment_prm_su25.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.CategoryDatabaseHelper;
import com.example.assignment_prm_su25.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private CategoryDatabaseHelper dbHelper;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerViewCategories);
        fabAdd = findViewById(R.id.fabAddCategory);
        dbHelper = new CategoryDatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(dbHelper.getAllCategories(), new CategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onEdit(Category category) {
                showEditCategoryDialog(category);
            }
            @Override
            public void onDelete(Category category) {
                showDeleteCategoryDialog(category);
            }
        });
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void showAddCategoryDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        EditText edtName = dialogView.findViewById(R.id.edtCategoryName);
        EditText edtDescription = dialogView.findViewById(R.id.edtCategoryDescription);

        new AlertDialog.Builder(this)
                .setTitle("Thêm danh mục mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String description = edtDescription.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(this, "Tên danh mục không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Category category = new Category(name, description);
                    boolean success = dbHelper.addCategory(category);
                    if (success) {
                        reloadCategories();
                        Toast.makeText(this, "Đã thêm danh mục", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void showCategoryOptionsDialog(Category category) {
        String[] options = {"Sửa", "Xoá"};
        new AlertDialog.Builder(this)
                .setTitle(category.getName())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditCategoryDialog(category);
                    } else if (which == 1) {
                        showDeleteCategoryDialog(category);
                    }
                })
                .show();
    }

    private void showEditCategoryDialog(Category category) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        EditText edtName = dialogView.findViewById(R.id.edtCategoryName);
        EditText edtDescription = dialogView.findViewById(R.id.edtCategoryDescription);
        edtName.setText(category.getName());
        edtDescription.setText(category.getDescription());

        new AlertDialog.Builder(this)
                .setTitle("Sửa danh mục")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String description = edtDescription.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(this, "Tên danh mục không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    category.setName(name);
                    category.setDescription(description);
                    boolean success = dbHelper.updateCategory(category);
                    if (success) {
                        reloadCategories();
                        Toast.makeText(this, "Đã cập nhật danh mục", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void showDeleteCategoryDialog(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xoá danh mục")
                .setMessage("Bạn có chắc muốn xoá danh mục này không?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    boolean success = dbHelper.deleteCategory(category.getId());
                    if (success) {
                        reloadCategories();
                        Toast.makeText(this, "Đã xoá danh mục", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void reloadCategories() {
        List<Category> categories = dbHelper.getAllCategories();
        adapter.setCategoryList(categories);
    }
} 