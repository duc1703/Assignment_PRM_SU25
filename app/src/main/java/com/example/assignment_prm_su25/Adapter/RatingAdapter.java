package com.example.assignment_prm_su25.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Rating;
import com.example.assignment_prm_su25.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private Context context;
    private List<Rating> ratingList;
    private UserDatabaseHelper dbHelper;

    public RatingAdapter(Context context, List<Rating> ratingList, UserDatabaseHelper dbHelper) {
        this.context = context;
        this.ratingList = ratingList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating rating = ratingList.get(position);

        User user = dbHelper.getUserById(rating.getUserId()); // Assuming you have getUserById in UserDatabaseHelper
        String userName = (user != null) ? user.getName() : "Anonymous User";

        holder.tvReviewerName.setText(userName);
        holder.ratingBarReview.setRating(rating.getRatingValue());
        holder.tvReviewComment.setText(rating.getReviewComment());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvReviewDate.setText(sdf.format(new Date(rating.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewerName, tvReviewComment, tvReviewDate;
        RatingBar ratingBarReview;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvReviewComment = itemView.findViewById(R.id.tvReviewComment);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            ratingBarReview = itemView.findViewById(R.id.ratingBarReview);
        }
    }
}