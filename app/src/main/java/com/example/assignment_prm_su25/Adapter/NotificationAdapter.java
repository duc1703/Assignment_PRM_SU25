// app/src/main/java/com/example/assignment_prm_su25/Adapter/NotificationAdapter.java
package com.example.assignment_prm_su25.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.model.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Notification notification);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public void setNotifications(List<Notification> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.tvNotificationTitle.setText(notification.getTitle());
        holder.tvNotificationMessage.setText(notification.getMessage());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvNotificationTimestamp.setText(sdf.format(new Date(notification.getTimestamp())));

        // Thay đổi giao diện dựa trên trạng thái đã đọc/chưa đọc
        if (notification.isRead()) {
            holder.notificationItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white)); // Màu nền đã đọc
            holder.tvNotificationTitle.setTypeface(null, Typeface.NORMAL);
            holder.tvNotificationMessage.setTypeface(null, Typeface.NORMAL);
            holder.tvNotificationTitle.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
            holder.tvNotificationMessage.setTextColor(ContextCompat.getColor(context, R.color.textSecondary));
        } else {
            holder.notificationItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_light)); // Màu nền chưa đọc
            holder.tvNotificationTitle.setTypeface(null, Typeface.BOLD);
            holder.tvNotificationMessage.setTypeface(null, Typeface.BOLD);
            holder.tvNotificationTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary)); // Màu nổi bật hơn
            holder.tvNotificationMessage.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
        }


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        LinearLayout notificationItemLayout;
        TextView tvNotificationTitle, tvNotificationMessage, tvNotificationTimestamp;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationItemLayout = itemView.findViewById(R.id.notification_item_layout);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationTimestamp = itemView.findViewById(R.id.tvNotificationTimestamp);
        }
    }
}