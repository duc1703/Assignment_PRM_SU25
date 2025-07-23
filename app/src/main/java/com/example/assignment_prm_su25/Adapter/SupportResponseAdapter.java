package com.example.assignment_prm_su25.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.SupportResponse;
import com.example.assignment_prm_su25.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupportResponseAdapter extends RecyclerView.Adapter<SupportResponseAdapter.ResponseViewHolder> {

    private Context context;
    private List<SupportResponse> responseList;
    private UserDatabaseHelper dbHelper;

    public SupportResponseAdapter(Context context, List<SupportResponse> responseList, UserDatabaseHelper dbHelper) {
        this.context = context;
        this.responseList = responseList;
        this.dbHelper = dbHelper;
    }

    public void setResponses(List<SupportResponse> responseList) {
        this.responseList = responseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_support_response, parent, false);
        return new ResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        SupportResponse response = responseList.get(position);

        User admin = dbHelper.getUserById(response.getAdminId());
        String senderName = (admin != null) ? admin.getName() : "Admin ẩn danh";
        holder.tvResponseSender.setText(senderName);

        holder.tvResponseContent.setText(response.getResponseContent());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvResponseTimestamp.setText(sdf.format(new Date(response.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    static class ResponseViewHolder extends RecyclerView.ViewHolder {
        TextView tvResponseSender, tvResponseContent, tvResponseTimestamp;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvResponseSender = itemView.findViewById(R.id.tvResponseSender);
            tvResponseContent = itemView.findViewById(R.id.tvResponseContent);
            tvResponseTimestamp = itemView.findViewById(R.id.tvResponseTimestamp);
        }
    }
}