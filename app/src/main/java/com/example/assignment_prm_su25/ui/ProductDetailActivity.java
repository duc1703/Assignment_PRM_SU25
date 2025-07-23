// ProductDetailActivity.java (Cập nhật)
package com.example.assignment_prm_su25.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment_prm_su25.Adapter.RatingAdapter;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.model.Rating;
import com.example.assignment_prm_su25.model.User; // Import User for username

import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDesc, tvProductRatingValue, tvNoReviews;
    private RatingBar ratingBarProductDetail, ratingBarUserReview;
    private EditText edtReviewComment;
    private Button btnSubmitReview;
    private RecyclerView rvReviews;

    private UserDatabaseHelper dbHelper;
    private Product currentProduct;
    private int userId; // Currently logged-in user ID
    private RatingAdapter ratingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }

        imgProduct = findViewById(R.id.imgProductDetail);
        tvName = findViewById(R.id.tvProductNameDetail);
        tvPrice = findViewById(R.id.tvProductPriceDetail);
        tvDesc = findViewById(R.id.tvProductDescDetail);
        tvProductRatingValue = findViewById(R.id.tvProductRatingValue);
        ratingBarProductDetail = findViewById(R.id.ratingBarProductDetail);
        ratingBarUserReview = findViewById(R.id.ratingBarUserReview);
        edtReviewComment = findViewById(R.id.edtReviewComment);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        rvReviews = findViewById(R.id.rvReviews);
        tvNoReviews = findViewById(R.id.tvNoReviews);

        dbHelper = UserDatabaseHelper.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentProduct = dbHelper.getProductById(productId);
        if (currentProduct == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayProductDetails();
        setupReviewSection();
        loadProductReviews();
    }

    private void displayProductDetails() {
        Glide.with(this).load(currentProduct.getImageUrl()).into(imgProduct);
        tvName.setText(currentProduct.getName());
        tvPrice.setText(String.format(Locale.getDefault(), "%.2f₫", currentProduct.getPrice()));
        tvDesc.setText(currentProduct.getDescription());

        // Display product's average rating
        ratingBarProductDetail.setRating(currentProduct.getRating());
        tvProductRatingValue.setText(String.format(Locale.getDefault(), "%.1f", currentProduct.getRating()));
    }

    private void setupReviewSection() {
        if (userId == -1) {
            // User not logged in, hide review submission
            findViewById(R.id.layoutSubmitReview).setVisibility(View.GONE);
            return;
        }

        // Check if user has already reviewed this product
        Rating userExistingRating = dbHelper.getUserRatingForProduct(userId, currentProduct.getId());
        if (userExistingRating != null) {
            ratingBarUserReview.setRating(userExistingRating.getRatingValue());
            edtReviewComment.setText(userExistingRating.getReviewComment());
            btnSubmitReview.setText("Cập nhật đánh giá của bạn");
        } else {
            btnSubmitReview.setText("Gửi đánh giá");
        }

        btnSubmitReview.setOnClickListener(v -> {
            float ratingValue = ratingBarUserReview.getRating();
            String reviewComment = edtReviewComment.getText().toString().trim();

            if (ratingValue == 0) {
                Toast.makeText(ProductDetailActivity.this, "Vui lòng chọn số sao để đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userExistingRating != null) {
                // Update existing review
                userExistingRating.setRatingValue(ratingValue);
                userExistingRating.setReviewComment(reviewComment);
                boolean success = dbHelper.updateRating(userExistingRating);
                if (success) {
                    Toast.makeText(ProductDetailActivity.this, "Đánh giá đã được cập nhật!", Toast.LENGTH_SHORT).show();
                    loadProductReviews();
                    dbHelper.updateProductAverageRating(currentProduct.getId()); // Recalculate average
                    displayProductDetails(); // Update UI
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Cập nhật đánh giá thất bại.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add new review
                Rating newRating = new Rating(0, currentProduct.getId(), userId, ratingValue, reviewComment, System.currentTimeMillis());
                boolean success = dbHelper.addRating(newRating);
                if (success) {
                    Toast.makeText(ProductDetailActivity.this, "Đánh giá của bạn đã được gửi!", Toast.LENGTH_SHORT).show();
                    edtReviewComment.setText(""); // Clear comment
                    ratingBarUserReview.setRating(0); // Reset rating bar
                    loadProductReviews();
                    dbHelper.updateProductAverageRating(currentProduct.getId()); // Recalculate average
                    displayProductDetails(); // Update UI
                    // Disable submission after first review to prevent multiple reviews from same user
                    findViewById(R.id.layoutSubmitReview).setVisibility(View.GONE); // Or change text to update
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Gửi đánh giá thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProductReviews() {
        List<Rating> reviews = dbHelper.getRatingsByProductId(currentProduct.getId());
        if (reviews.isEmpty()) {
            tvNoReviews.setVisibility(View.VISIBLE);
            rvReviews.setVisibility(View.GONE);
        } else {
            tvNoReviews.setVisibility(View.GONE);
            rvReviews.setVisibility(View.VISIBLE);
            ratingAdapter = new RatingAdapter(this, reviews, dbHelper);
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.setAdapter(ratingAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}