package com.example.assignment_prm_su25.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Category;
import com.example.assignment_prm_su25.model.Product;

import java.text.DecimalFormat;
import java.util.List;

import com.example.assignment_prm_su25.MainActivity;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onAddToCartClick(Product product);
        void onDeleteClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void updateProducts(List<Product> productList) {
        setProducts(productList);
    }

    public void setProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        // Set basic product info
        holder.tvProductName.setText(product.getName());
        holder.tvProductDescription.setText(product.getDescription());
        holder.ratingBar.setRating(product.getRating());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvProductPrice.setText(formatter.format(product.getPrice()) + "₫");
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.shimmer_effect)
                .into(holder.imgProduct);
        // Đặt lại sự kiện click cho itemView (mở chi tiết)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
                if (context instanceof MainActivity) {
                    ((MainActivity)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        UserDatabaseHelper dbHelper = UserDatabaseHelper.getInstance(context);
        Category category = dbHelper.getCategoryById(product.getCategoryId());
        if (category != null) {
            holder.tvBrand.setText(category.getName());
            holder.tvBrand.setVisibility(View.VISIBLE);
        } else {
            holder.tvBrand.setVisibility(View.GONE);
        }
        holder.tvStock.setVisibility(View.GONE);  // Hide stock for now
        holder.tvSizeColor.setVisibility(View.GONE);  // Hide size/color for now
        holder.tvOriginalPrice.setVisibility(View.GONE);  // Hide original price for now
        holder.tvDiscount.setVisibility(View.GONE);
        // Đặt lại sự kiện click cho nút thêm vào giỏ
        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductDescription, tvProductPrice, tvOriginalPrice;
        TextView tvBrand, tvSizeColor, tvDiscount, tvStock;
        RatingBar ratingBar;
        View btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvSizeColor = itemView.findViewById(R.id.tvSizeColor);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvStock = itemView.findViewById(R.id.tvStock);
            ratingBar = itemView.findViewById(R.id.ratingBarItem);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);

        }
    }

}