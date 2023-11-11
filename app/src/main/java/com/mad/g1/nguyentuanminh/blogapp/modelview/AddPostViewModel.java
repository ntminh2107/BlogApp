package com.mad.g1.nguyentuanminh.blogapp.modelview;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;

public class AddPostViewModel extends ViewModel {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference storage;
    private DatabaseReference userRef;
    private DatabaseReference postRef;
    private MutableLiveData<Boolean> addPostResult = new MutableLiveData<>();
    private MutableLiveData<String> userImageUrl = new MutableLiveData<>();

    public LiveData<String> getUserImageUrl() {
        return userImageUrl;
    }

    public AddPostViewModel() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();
        postRef = FirebaseDatabase.getInstance().getReference().child("post");
        userRef = FirebaseDatabase.getInstance().getReference().child("user");
    }

    public void addPost(String title, String content, Uri imguri) {
        if (user != null) {
            userRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        String userProfileImg = snapshot.child("image").getValue(String.class); // Retrieve user profile image URL
                        if (username != null) {
                            Post post = new Post(username, user.getUid(), title, content,null ,userProfileImg); // Include user profile image URL
                            // Set timestamp to ServerValue.TIMESTAMP when creating a new post
                            post.setTimestamp(ServerValue.TIMESTAMP);

                            if (imguri != null) {
                                StorageReference imgRef = storage.child("images/" + user.getUid() + "/"
                                        + System.currentTimeMillis() + ".jpg");
                                imgRef.putFile(imguri).addOnSuccessListener(taskSnapshot -> {
                                    imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        post.setImg(uri.toString());
                                        addPostToDB(post);
                                    });
                                }).addOnFailureListener(e -> {
                                    addPostToDB(post);
                                });
                            } else {
                                addPostToDB(post);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });
        }
    }

    private void addPostToDB(Post post) {
        String postID = postRef.push().getKey();
        post.setPostID(postID);
        postRef.child(postID).setValue(post).addOnSuccessListener(task -> {
            addPostResult.setValue(true);
        }).addOnFailureListener(e -> {
            addPostResult.setValue(false);
        });
    }



}
