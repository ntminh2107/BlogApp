package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.adapter.PostAdapter;
import com.mad.g1.nguyentuanminh.blogapp.modelview.PostViewModel;
import com.mad.g1.nguyentuanminh.blogapp.modelview.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailProfileView extends AppCompatActivity {

    private CircleImageView userPics;
    private TextView usernameView, desTextViewTest;
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private UserViewModel userViewModel;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile_view);

        // Initialize UI components
        userPics = findViewById(R.id.userPics);
        usernameView = findViewById(R.id.usernameView);
        desTextViewTest = findViewById(R.id.DesTextViewTest);
        postRecyclerView = findViewById(R.id.PostRV);

        // Initialize ViewModels
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Get user ID from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            String userId = intent.getStringExtra("userId");

            // Use the UserViewModel to get user details
            userViewModel.getUserDetails(userId).observe(this, user -> {
                if (user != null) {
                    // Update UI with user details
                    usernameView.setText(user.getUsername());
                    desTextViewTest.setText(user.getDes());

                    // Load user profile picture using Glide
                    Glide.with(this)
                            .load(user.getImage())
                            .into(userPics);
                }
            });

            // Use the PostViewModel to get posts by the user ID
            postViewModel.getPostsByUserId(userId).observe(this, posts -> {
                // Update UI with user's posts
                postAdapter = new PostAdapter(posts);
                postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                postRecyclerView.setAdapter(postAdapter);
            });
        }
    }
}


