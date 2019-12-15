package com.example.theangkringan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.theangkringan.R;
import com.example.theangkringan.models.ReviewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.CommentViewHolder> {

    private ArrayList<ReviewModel> listReview = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_recipe, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(listReview.get(position));
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fullname;
        TextView tv_review;
        RatingBar rb_rating;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fullname = itemView.findViewById(R.id.tv_full_name);
            tv_review = itemView.findViewById(R.id.tv_comment);
            rb_rating = itemView.findViewById(R.id.item_rating_detail);
        }
        public void bind(ReviewModel data){
            tv_fullname.setText(data.getFullname());
            tv_review.setText(data.getComment());
            rb_rating.setRating(data.getRating());
        }
    }

    public void setData(ArrayList<ReviewModel> items) {
        listReview.clear();
        listReview.addAll(items);
        notifyDataSetChanged();
    }

}
