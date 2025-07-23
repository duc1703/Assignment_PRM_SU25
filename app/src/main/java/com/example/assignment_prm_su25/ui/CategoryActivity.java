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

import com.example.assignment_prm_su25.Adapter.CategoryAdapter;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Category;
import java.util.List;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryActivity extends AppCompatActivity {
    private EditText edtCategoryName, edtCategoryDescription;
    private Button btnAddCategory, btnUpdateCategory, btnClearCategory;
    private RecyclerView rvCategoryList;
    private UserDatabaseHelper dbHelper;
    private CategoryAdapter adapter;
    private Category selectedCategory = null;
    private FloatingActionButton fabAddCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        rvCategoryList = findViewById(R.id.rvCategoryList);
        fabAddCategory = findViewById(R.id.fabAddCategory);
        dbHelper = UserDatabaseHelper.getInstance(this);

        rvCategoryList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter();
        rvCategoryList.setAdapter(adapter);

        loadCategories();

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog(null);
            }
        });
    }

    private void loadCategories() {
        List<Category> categories = dbHelper.getAllCategories();
        adapter.setCategories(categories);
        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                // Không làm gì khi click item
            }
            @Override
            public void onDeleteClick(Category category) {
                dbHelper.deleteCategory(category.getId());
                loadCategories();
            }
            @Override
            public void onEditClick(Category category) {
                showCategoryDialog(category);
            }
        });
    }

    private void showCategoryDialog(Category categoryToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(categoryToEdit == null ? "Thêm danh mục" : "Sửa danh mục");
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_category, null);
        EditText edtName = dialogView.findViewById(R.id.edtDialogCategoryName);
        EditText edtDesc = dialogView.findViewById(R.id.edtDialogCategoryDescription);
        if (categoryToEdit != null) {
            edtName.setText(categoryToEdit.getName());
            edtDesc.setText(categoryToEdit.getDescription());
        }
        builder.setView(dialogView);
        builder.setPositiveButton(categoryToEdit == null ? "Thêm" : "Cập nhật", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }
            if (categoryToEdit == null) {
                // Thêm mới
                Category category = new Category(name, desc);
                boolean success = dbHelper.addCategory(category);
                if (success) {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Sửa
                categoryToEdit.setName(name);
                categoryToEdit.setDescription(desc);
                boolean success = dbHelper.updateCategory(categoryToEdit);
                if (success) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            loadCategories();
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
} 