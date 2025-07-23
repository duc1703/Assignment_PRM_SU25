package com.example.assignment_prm_su25.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.model.ProductVariant;
import java.util.List;

public class ProductVariantAdapter extends RecyclerView.Adapter<ProductVariantAdapter.VariantViewHolder> {
    private List<ProductVariant> variants;
    private int selectedPosition = -1;

    public ProductVariantAdapter(List<ProductVariant> variants) {
        this.variants = variants;
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_variant, parent, false);
        return new VariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariantViewHolder holder, int position) {
        ProductVariant variant = variants.get(position);
        holder.tvColor.setText(variant.getColor());
        holder.tvSize.setText(variant.getSize());
        holder.tvStock.setText("Còn: " + variant.getStock());
        
        // Highlight selected variant
        holder.itemView.setSelected(selectedPosition == position);
        
        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    public ProductVariant getSelectedVariant() {
        return selectedPosition >= 0 ? variants.get(selectedPosition) : null;
    }

    static class VariantViewHolder extends RecyclerView.ViewHolder {
        TextView tvColor, tvSize, tvStock;

        VariantViewHolder(View itemView) {
            super(itemView);
            tvColor = itemView.findViewById(R.id.tvVariantColor);
            tvSize = itemView.findViewById(R.id.tvVariantSize);
            tvStock = itemView.findViewById(R.id.tvVariantStock);
        }
    }
}
