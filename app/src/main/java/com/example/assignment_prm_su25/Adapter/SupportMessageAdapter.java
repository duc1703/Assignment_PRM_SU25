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
import com.example.assignment_prm_su25.model.SupportMessage;
import com.example.assignment_prm_su25.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupportMessageAdapter extends RecyclerView.Adapter<SupportMessageAdapter.MessageViewHolder> {

    private Context context;
    private List<SupportMessage> messageList;
    private UserDatabaseHelper dbHelper; // Cần thiết để lấy tên người gửi
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SupportMessage message);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SupportMessageAdapter(Context context, List<SupportMessage> messageList, UserDatabaseHelper dbHelper) {
        this.context = context;
        this.messageList = messageList;
        this.dbHelper = dbHelper;
    }

    public void setMessages(List<SupportMessage> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_support_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        SupportMessage message = messageList.get(position);

        holder.tvMessageSubject.setText(message.getSubject());
        holder.tvMessagePreview.setText(message.getMessage());
        holder.tvMessageStatus.setText(message.getStatus());

        // Đặt màu trạng thái
        if ("New".equals(message.getStatus())) {
            holder.tvMessageStatus.setBackgroundResource(R.drawable.bg_status_new_rounded);
        } else {
            // Có thể thêm các trạng thái khác và màu khác ở đây, ví dụ cho trạng thái "Read"
            holder.tvMessageStatus.setBackgroundResource(R.drawable.bg_rounded_border);
            // Hoặc set màu nền khác: holder.tvMessageStatus.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.tvMessageStatus.setTextColor(context.getResources().getColor(android.R.color.black)); // Đảm bảo màu chữ dễ đọc
        }

        // Lấy thông tin người gửi
        User sender = dbHelper.getUserById(message.getUserId());
        String senderInfo = "Từ: " + (sender != null ? sender.getName() : "Người dùng ẩn danh");
        if (message.getOrderId() != -1) {
            senderInfo += " (Đơn hàng #" + message.getOrderId() + ")";
        }
        holder.tvSenderInfo.setText(senderInfo);

        // Định dạng ngày
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvMessageDate.setText(sdf.format(new Date(message.getTimestamp())));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageSubject, tvMessagePreview, tvSenderInfo, tvMessageDate, tvMessageStatus;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageSubject = itemView.findViewById(R.id.tvMessageSubject);
            tvMessagePreview = itemView.findViewById(R.id.tvMessagePreview);
            tvSenderInfo = itemView.findViewById(R.id.tvSenderInfo);
            tvMessageDate = itemView.findViewById(R.id.tvMessageDate);
            tvMessageStatus = itemView.findViewById(R.id.tvMessageStatus);
        }
    }
}