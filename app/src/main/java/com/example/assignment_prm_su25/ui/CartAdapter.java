package com.example.assignment_prm_su25.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.model.Cart;
import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Cart> cartList;
    private UserDatabaseHelper dbHelper;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        this.dbHelper = UserDatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        Product product = dbHelper.getProductById(cart.getProductId());

        if (product != null) {
            holder.tvProductName.setText(product.getName());
            holder.tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
            holder.tvQuantity.setText(String.valueOf(cart.getQuantity()));

            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.shimmer_effect)
                    .into(holder.imgProduct);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}