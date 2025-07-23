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

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    private Context context;
    private List<Cart> cartList;
    private UserDatabaseHelper dbHelper;
    private CartUpdateListener cartUpdateListener;

    public CartAdapter(Context context, List<Cart> cartList, CartUpdateListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.dbHelper = UserDatabaseHelper.getInstance(context);
        this.cartUpdateListener = listener;
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
        Product product = cart.getProduct();

        if (product != null) {
            // Set product details
            holder.tvProductName.setText(product.getName());
            holder.tvProductPrice.setText(String.format("$%.2f", product.getPrice() * cart.getQuantity()));
            holder.tvQuantity.setText(String.valueOf(cart.getQuantity()));

            // Load product image with Glide
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.shimmer_effect)
                    .error(R.drawable.ic_error)
                    .into(holder.imgProduct);
                    
            // Update quantity buttons
            holder.btnIncrease.setOnClickListener(v -> {
                int newQuantity = cart.getQuantity() + 1;
                cart.setQuantity(newQuantity);
                dbHelper.updateCartItem(cart);
                holder.tvQuantity.setText(String.valueOf(newQuantity));
                holder.tvProductPrice.setText(String.format("$%.2f", product.getPrice() * newQuantity));
                if (cartUpdateListener != null) {
                    cartUpdateListener.onCartUpdated();
                }
            });
            
            holder.btnDecrease.setOnClickListener(v -> {
                if (cart.getQuantity() > 1) {
                    int newQuantity = cart.getQuantity() - 1;
                    cart.setQuantity(newQuantity);
                    dbHelper.updateCartItem(cart);
                    holder.tvQuantity.setText(String.valueOf(newQuantity));
                    holder.tvProductPrice.setText(String.format("$%.2f", product.getPrice() * newQuantity));
                    if (cartUpdateListener != null) {
                        cartUpdateListener.onCartUpdated();
                    }
                }
            });
        }

        // Remove item from cart
        holder.btnDeleteCartItem.setOnClickListener(v -> {
            dbHelper.deleteCartItem(cart.getId());
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            
            // Update total amount and check if cart is empty
            if (cartUpdateListener != null) {
                cartUpdateListener.onCartUpdated();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
    
    public void updateCartItems(List<Cart> newCartItems) {
        this.cartList = newCartItems;
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnDeleteCartItem, btnIncrease, btnDecrease;
        TextView tvProductName, tvProductPrice, tvQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnDeleteCartItem = itemView.findViewById(R.id.btnDeleteCartItem);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}