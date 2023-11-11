package com.mad.g1.nguyentuanminh.blogapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameView, timestampView, titleView, contentView;
        ImageView imageView, userIMGview;

        public PostViewHolder(View itemView) {
            super(itemView);
            usernameView = itemView.findViewById(R.id.usernameView);
            timestampView = itemView.findViewById(R.id.timestampview);
            titleView = itemView.findViewById(R.id.TitleView);
            contentView = itemView.findViewById(R.id.ContentView);
            imageView = itemView.findViewById(R.id.imagePost);
            userIMGview = itemView.findViewById(R.id.userPics);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.usernameView.setText(post.getUsername());
        holder.timestampView.setText(post.getTimestamp().toString());
        holder.titleView.setText(post.getTitle());
        holder.contentView.setText(post.getContent());

        // Load user profile image
        if (post.getUserProfileImg() != null && !post.getUserProfileImg().isEmpty()) {
            Picasso.get().load(post.getUserProfileImg()).into(holder.userIMGview);
        } else {
            // If user profile image is not available, you can set a default placeholder
            holder.userIMGview.setImageResource(R.drawable.male_ic);
        }

        // Load post image
        if (post.getImg() != null && !post.getImg().isEmpty()) {
            Picasso.get().load(post.getImg()).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // Update postList and notify the adapter of the changes
    public void setPosts(List<Post> posts) {
        this.postList = posts;
        notifyDataSetChanged();
    }

}
