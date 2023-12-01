package com.mad.g1.nguyentuanminh.blogapp.modelview;

import android.net.Uri;

import androidx.annotation.NonNull;
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

    public MutableLiveData<Boolean> getAddPostResult() {
        return addPostResult;
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
                        String userProfileImg = snapshot.child("image").getValue(String.class);

                        if (username != null) {
                            Post post = new Post(username, user.getUid(), title, content, userProfileImg);
                            post.setTimestamp(ServerValue.TIMESTAMP);

                            if (imguri != null) {
                                // Upload the image to Firebase Storage
                                StorageReference imgRef = storage.child("images/" + user.getUid() + "/"
                                        + System.currentTimeMillis() + ".jpg");
                                imgRef.putFile(imguri)
                                        .addOnSuccessListener(taskSnapshot -> {
                                            // Get the download URL for the image
                                            imgRef.getDownloadUrl()
                                                    .addOnSuccessListener(uri -> {
                                                        // Set the image URL in the Post object
                                                        post.setImg(uri.toString());

                                                        // Add the post to the database
                                                        addPostToDB(post);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Handle failure to get image URL
                                                        addPostResult.setValue(false);
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure to upload image
                                            addPostResult.setValue(false);
                                        });
                            } else {
                                // If no image is selected, add the post to the database without an image
                                addPostToDB(post);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                    addPostResult.setValue(false);
                }
            });
        }
    }

    private void addPostToDB(Post post) {
        String postID = postRef.push().getKey();
        post.setPostID(postID);

        postRef.child(postID).setValue(post)
                .addOnSuccessListener(task -> addPostResult.setValue(true))
                .addOnFailureListener(e -> addPostResult.setValue(false));
    }
    //xin chao

}
