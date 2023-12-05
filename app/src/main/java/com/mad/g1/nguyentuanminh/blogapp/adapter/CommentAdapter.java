package com.mad.g1.nguyentuanminh.blogapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.Comments;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comments> commentList;

    // Constructor to initialize the adapter with a list of comments
    public CommentAdapter(List<Comments> commentList) {
        this.commentList = commentList;
    }

    // ViewHolder class to hold the views
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userPics;
        TextView usernameView, commentView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userPics = itemView.findViewById(R.id.userPics);
            usernameView = itemView.findViewById(R.id.usernameView);
            commentView = itemView.findViewById(R.id.commentView);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a comment item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Bind data to views for each comment
        Comments comment = commentList.get(position);

        // Set user profile picture, username, and comment content
        // You may need to load the user profile picture using a library like Glide or Picasso
        Glide.with(holder.itemView.getContext())
                .load(comment.getUserPic())
                .into(holder.userPics);

        holder.usernameView.setText(comment.getUsername());
        holder.commentView.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    // Method to update the dataset and refresh the RecyclerView
    public void setCommentList(List<Comments> comments) {
        this.commentList = comments;
        notifyDataSetChanged();
    }
}
