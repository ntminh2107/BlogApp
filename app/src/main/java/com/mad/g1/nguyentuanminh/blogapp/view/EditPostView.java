package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.modelview.PostViewModel;

public class EditPostView extends AppCompatActivity {

    private PostViewModel editPostViewModel;

    private EditText titleEditText;
    private EditText contentEditText;
    private ImageView imageView;
    private Button editButton;
    private Button changeImageButton;
    private Uri selectedimageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_view);

        // Initialize ViewModel
        editPostViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Initialize UI components
        titleEditText = findViewById(R.id.ETtitle);
        contentEditText = findViewById(R.id.contentET);
        imageView = findViewById(R.id.imgView);
        editButton = findViewById(R.id.editBTN);
        changeImageButton = findViewById(R.id.changeImgBTN);

        // Get postId from Intent
        String postId = getIntent().getStringExtra("postId");

        // Observe changes in the detailed post
        editPostViewModel.getDetailedPost(postId).observe(this, post -> {
            if (post != null) {
                // Populate UI with post details
                titleEditText.setText(post.getTitle());
                contentEditText.setText(post.getContent());

                if (post != null) {
                    // Populate UI with post details
                    titleEditText.setText(post.getTitle());
                    contentEditText.setText(post.getContent());

                    // Check if post has an image
                    if (post.getImg() != null && !post.getImg().isEmpty()) {
                        // Load and display image using your preferred image loading library (Glide, Picasso, etc.)
                        // Example with Glide:
                        Glide.with(this).load(post.getImg()).into(imageView);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                }
            }
        });

        // Set click listener for the change image button
        changeImageButton.setOnClickListener(view -> openImagePicker());

        // Set click listener for the edit button
        editButton.setOnClickListener(view -> updatePost(postId));
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedimageUri = data.getData();

            // Load and display the selected image using your preferred image loading library
            // Example with Glide:
            Glide.with(this).load(selectedimageUri).into(imageView);

            // You can also save the selectedImageUri for later use when updating the post
            // updatePostViewModel.setNewImageUri(selectedImageUri);
        }
    }

    private void updatePost(String postId) {
        String newTitle = titleEditText.getText().toString().trim();
        String newContent = contentEditText.getText().toString().trim();

        // Perform the update using the ViewModel
        editPostViewModel.updatePost(postId, newTitle, newContent, selectedimageUri);

        // Add an observer for the update result if needed
        editPostViewModel.getUpdatePostResult().observe(this, success -> {
            if (success != null && success) {
                // Update successful, handle accordingly
                finish(); // Finish the activity or navigate to another screen
            } else {
                // Update failed, handle accordingly
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

