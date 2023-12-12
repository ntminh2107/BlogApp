package com.mad.g1.nguyentuanminh.blogapp.modelview;


import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.g1.nguyentuanminh.blogapp.model.Comments;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import io.grpc.Context;

public class PostViewModel extends ViewModel {

    private MutableLiveData<List<Post>> postList;
    private MutableLiveData<Post> detailedPost;
    private DatabaseReference postsRef;
    private StorageReference storage;
    private MutableLiveData<Boolean> updatePostResult = new MutableLiveData<>();

    public LiveData<Boolean> getUpdatePostResult() {
        return updatePostResult;
    }

    public LiveData<List<Post>> getPosts() {
        if (postList == null) {
            postList = new MutableLiveData<>();
            loadPosts();
        }
        return postList;
    }

    public PostViewModel()
    {
        storage = FirebaseStorage.getInstance().getReference();
        postsRef = FirebaseDatabase.getInstance().getReference().child("post");
    }


    private void loadPosts() {
        postsRef = FirebaseDatabase.getInstance().getReference().child("post");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }

                postList.setValue(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    public LiveData<Post> getDetailedPost(String postId) {
        if (detailedPost == null) {
            detailedPost = new MutableLiveData<>();
            loadDetailedPost(postId);
        }
        return detailedPost;
    }

    private void loadDetailedPost(String postId) {
        DatabaseReference postRef = postsRef.child(postId);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    detailedPost.setValue(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    public LiveData<List<Post>> getPostsByUserId(String userId) {
        MutableLiveData<List<Post>> userPosts = new MutableLiveData<>();
        postsRef = FirebaseDatabase.getInstance().getReference().child("post");

        postsRef.orderByChild("userid").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                userPosts.setValue(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        return userPosts;
    }

    public void updatePost(String postId, String title, String content, Uri newImageUri) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("post").child(postId);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get the current post details
                    Post currentPost = snapshot.getValue(Post.class);

                    // Create an updated Post object with the new title, content, and comments
                    Post updatedPost = new Post(currentPost.getUsername(), currentPost.getUserid(), title, content, currentPost.getImg());
                    updatedPost.setTimestamp(ServerValue.TIMESTAMP);

                    // Set the comments in the updated post

                    if (newImageUri != null) {
                        // If a new image is selected, upload it to Firebase Storage
                        StorageReference imgRef = storage.child("images/" + currentPost.getUserid() + "/"
                                + System.currentTimeMillis() + ".jpg");

                        imgRef.putFile(newImageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // Get the download URL for the new image
                                    imgRef.getDownloadUrl()
                                            .addOnSuccessListener(uri -> {
                                                // Set the new image URL in the updated Post object
                                                updatedPost.setImg(uri.toString());

                                                // Update the post in the database with the new details
                                                postRef.setValue(updatedPost)
                                                        .addOnCompleteListener(task -> {
                                                            // Handle successful post update
                                                            updatePostResult.setValue(true);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Handle failure to update post
                                                            updatePostResult.setValue(false);
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle failure to get image URL
                                                updatePostResult.setValue(false);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to upload new image
                                    updatePostResult.setValue(false);
                                });
                    } else {
                        // If no new image is selected, update the post in the database directly
                        postRef.setValue(updatedPost)
                                .addOnCompleteListener(task -> {
                                    // Handle successful post update
                                    updatePostResult.setValue(true);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to update post
                                    updatePostResult.setValue(false);
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                updatePostResult.setValue(false);
            }
        });
    }





}
