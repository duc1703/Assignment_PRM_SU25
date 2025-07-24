package com.example.assignment_prm_su25.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> userList;
    private final OnUserListener onUserListener;

    public UserAdapter(List<User> userList, OnUserListener onUserListener) {
        this.userList = userList;
        this.onUserListener = onUserListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view, onUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView emailTextView;
        private final TextView roleTextView;
        private final MaterialButton editButton;
        private final MaterialButton deleteButton;
        private final OnUserListener onUserListener;

        public UserViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            emailTextView = itemView.findViewById(R.id.user_email_text_view);
            roleTextView = itemView.findViewById(R.id.user_role_text_view);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            this.onUserListener = onUserListener;
        }

        public void bind(final User user) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            roleTextView.setText(user.getRole());

            editButton.setOnClickListener(v -> {
                if (onUserListener != null) {
                    onUserListener.onEditClick(getAdapterPosition());
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (onUserListener != null) {
                    onUserListener.onDeleteClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnUserListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }
}
