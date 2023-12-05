package com.mad.g1.nguyentuanminh.blogapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.HomeFragment;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.modelview.PostViewModel;
import com.mad.g1.nguyentuanminh.blogapp.view.DetailPostView;
import com.mad.g1.nguyentuanminh.blogapp.view.EditPostView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameView, timestampView, titleView, contentView,likeView;
        ImageView imageView, userIMGview, choiceButton;
        ImageButton likeBTN, cmtBTN;

        public PostViewHolder(View itemView) {
            super(itemView);
            usernameView = itemView.findViewById(R.id.usernameView);
            timestampView = itemView.findViewById(R.id.timestampview);
            titleView = itemView.findViewById(R.id.TitleView);
            contentView = itemView.findViewById(R.id.ContentView);
            imageView = itemView.findViewById(R.id.imagePost);
            userIMGview = itemView.findViewById(R.id.userPics);
            likeBTN = itemView.findViewById(R.id.likeButton);
            likeView = itemView.findViewById(R.id.likecountTV);
            cmtBTN = itemView.findViewById(R.id.cmtBTN);
            choiceButton = itemView.findViewById(R.id.choicepost);

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();
        holder.usernameView.setText(post.getUsername());
        holder.timestampView.setText(post.getTimestamp().toString());
        holder.titleView.setText(post.getTitle());
        holder.contentView.setText(post.getContent());
        holder.likeView.setText(String.valueOf(post.getLikecount()+1));
        if (post.getUserid().equals(currentUserId)) {
            // Nếu là bài post của current user, hiển thị nút "edit"
            holder.choiceButton.setVisibility(View.VISIBLE);

            // Bắt sự kiện khi người dùng click vào nút "edit"
            holder.choiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Xử lý logic khi người dùng click vào nút "edit"
                    // Ví dụ: mở màn hình chỉnh sửa bài post
                    Intent intent = new Intent(view.getContext(), EditPostView.class);
                    intent.putExtra("postId",post.getPostID());
                    view.getContext().startActivity(intent);
                }
            });
        } else {
            // Nếu không phải là bài post của current user, ẩn nút "edit"
            holder.choiceButton.setVisibility(View.GONE);
        }

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


        holder.likeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION)
                {
                    Post post = postList.get(position);
                        post.setLikecount(post.getLikecount()+1);
                        holder.likeBTN.setImageResource(R.drawable.thumbupblue);

                    notifyItemChanged(position);
                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("post").child(post.getPostID());
                    postRef.child("likecount").setValue(post.getLikecount());
                }
            }
        });

        holder.cmtBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Post post1 = postList.get(position);
                Intent intent = new Intent(v.getContext(), DetailPostView.class);
                intent.putExtra("postId", post1.getPostID());

                v.getContext().startActivity(intent);
            }
        });
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
