package com.example.assignment_prm_su25.Adapter;

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
import com.example.assignment_prm_su25.ui.OrderItem; // Make sure to import OrderItem

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<OrderItem> orderItemList;

    public OrderItemAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItemList.get(position);
        holder.tvOrderItemName.setText(item.getProductName());
        holder.tvOrderItemQuantity.setText(String.format("Số lượng: %d", item.getQuantity()));
        holder.tvOrderItemPrice.setText(String.format("Giá: %.2f₫", item.getPrice()));
        holder.tvOrderItemSubtotal.setText(String.format("Thành tiền: %.2f₫", item.getQuantity() * item.getPrice()));

        Glide.with(context)
                .load(item.getProductImage())
                .placeholder(R.drawable.shimmer_effect) // Make sure you have this drawable
                .into(holder.imgOrderItem);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrderItem;
        TextView tvOrderItemName, tvOrderItemQuantity, tvOrderItemPrice, tvOrderItemSubtotal;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrderItem = itemView.findViewById(R.id.imgOrderItem);
            tvOrderItemName = itemView.findViewById(R.id.tvOrderItemName);
            tvOrderItemQuantity = itemView.findViewById(R.id.tvOrderItemQuantity);
            tvOrderItemPrice = itemView.findViewById(R.id.tvOrderItemPrice);
            tvOrderItemSubtotal = itemView.findViewById(R.id.tvOrderItemSubtotal);
        }
    }
}