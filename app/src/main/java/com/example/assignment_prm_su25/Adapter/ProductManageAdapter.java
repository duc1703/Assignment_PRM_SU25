package com.example.assignment_prm_su25.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.model.Product;

import java.text.DecimalFormat;
import java.util.List;

import com.example.assignment_prm_su25.MainActivity;

public class ProductManageAdapter extends RecyclerView.Adapter<ProductManageAdapter.ProductManageViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductManageAdapter(Context context, List<Product> productList) {
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
    public ProductManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_manage, parent, false);
        return new ProductManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManageViewHolder holder, int position) {
        Product product = productList.get(position);
        // Set basic product info
        holder.tvProductName.setText(product.getName());
        holder.tvProductDescription.setText(product.getDescription());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvProductPrice.setText(formatter.format(product.getPrice()) + "₫");
        holder.ratingBar.setRating(product.getRating());
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

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(product);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductManageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductDescription, tvProductPrice, tvOriginalPrice;
        TextView tvBrand, tvSizeColor, tvDiscount, tvStock;
        RatingBar ratingBar;
        View btnAddToCart;
        ImageButton btnEdit, btnDelete;

        public ProductManageViewHolder(@NonNull View itemView) {
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
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }

}
