package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.adapter.CommentAdapter;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;
import com.mad.g1.nguyentuanminh.blogapp.modelview.CommentViewModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPostView extends AppCompatActivity {

    private CircleImageView userPics;
    private ImageButton likeButton;
    private RecyclerView cmtRV;
    private EditText cmted;

    private Post post;
    private Button cmtbtn;
    private TextView usernameView, timestampView, titleView, contentView, likecount;
    private ImageView imagePost;

    private DatabaseReference postRef;
    private CommentViewModel commentViewModel;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post_view);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        // Initialize UI components
        userPics = findViewById(R.id.userPics);
        usernameView = findViewById(R.id.usernameView);
        timestampView = findViewById(R.id.timestampview);
        titleView = findViewById(R.id.TitleView);
        contentView = findViewById(R.id.ContentView);
        imagePost = findViewById(R.id.imagePost);
        likeButton = findViewById(R.id.likeButton);
        likecount = findViewById(R.id.likecountTV);

        // Get post details from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("postId")) {
            String postID = intent.getStringExtra("postId");
            if (postID != null) {
                // Initialize the DatabaseReference for the specific post
                postRef = FirebaseDatabase.getInstance().getReference().child("post").child(postID);

                // Add ValueEventListener to get post details
                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Create a Post object using the DataSnapshot
                            Post post = dataSnapshot.getValue(Post.class);

                            // Set post details to UI components
                            if (post != null) {
                                usernameView.setText(post.getUsername());
                                timestampView.setText(post.getTimestamp().toString());
                                titleView.setText(post.getTitle());
                                contentView.setText(post.getContent());

                                // Load user profile pic using Glide (placeholder included)
                                Glide.with(DetailPostView.this)
                                        .load(post.getUserProfileImg())
                                        .into(userPics);

                                // Load post image using Glide (placeholder included)
                                if (post.getImg() != null && !post.getImg().isEmpty()) {
                                    Glide.with(DetailPostView.this)
                                            .load(post.getImg())
                                            .into(imagePost);
                                } else {
                                    // If there's no post image, you can hide or set a placeholder for the ImageView
                                    imagePost.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });
            }
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setLikecount(post.getLikecount() + 1);
                likeButton.setImageResource(R.drawable.thumbupblue);
                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("post").child(post.getPostID());
                postRef.child("likecount").setValue(post.getLikecount());
            }
        });

        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        cmted = findViewById(R.id.cmtET);
        cmtbtn = findViewById(R.id.submitCmt);
        cmtRV = findViewById(R.id.cmtRV);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        cmtRV.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(new ArrayList<>()); // Create an empty adapter
        cmtRV.setAdapter(commentAdapter);

        if (intent != null && intent.hasExtra("postId")) {
            String postId = intent.getStringExtra("postId");
            System.out.println(postId);

            // Observe changes in the comment list
            commentViewModel.getCommentList(postId).observe(this, comments -> {
                // Update the RecyclerView adapter with the new list of comments
                commentAdapter.setCommentList(comments);
                commentAdapter.notifyDataSetChanged();
            });
        }
        cmtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postId = intent.getStringExtra("postId");
                String comment = cmted.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)) {
                    // Call the addComment method in CommentViewModel
                    commentViewModel.addComment(postId, comment);

                    // Optionally, clear the EditText after submitting the comment
                    cmted.setText("");
                } else {
                    // Handle the case where the comment content is empty
                    Toast.makeText(DetailPostView.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
