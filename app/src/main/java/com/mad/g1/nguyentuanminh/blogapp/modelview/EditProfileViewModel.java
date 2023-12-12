package com.mad.g1.nguyentuanminh.blogapp.modelview;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.g1.nguyentuanminh.blogapp.adapter.PostAdapter;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;

import java.util.HashMap;
import java.util.Map;

public class EditProfileViewModel extends ViewModel {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private DatabaseReference reference;
    private PostAdapter postAdapter;

    public EditProfileViewModel() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("user");
    }

    public FirebaseUser getCurrentUser() {
        return user;
    }

    public StorageReference getStorageReference(String userId) {
        return storage.getReference().child("user").child(userId);
    }

    public DatabaseReference getUserReference(String userId) {
        return reference.child(userId);
    }

    public void updateProfileData(String userId, String fullname, String dob, String pob,
                                  String username, String des, String gender) {
        DatabaseReference editRef = reference.child(userId);
        editRef.child("fullname").setValue(fullname);
        editRef.child("dob").setValue(dob);
        editRef.child("pob").setValue(pob);
        editRef.child("username").setValue(username);
        editRef.child("des").setValue(des);
        editRef.child("gender").setValue(gender);
    }

    public void uploadImageAndSetProfile(String userId, Uri selectedImage, OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        if (selectedImage != null) {
            StorageReference storageReference = storage.getReference().child("user").child(userId);
            storageReference.putFile(selectedImage).addOnCompleteListener(onCompleteListener);
        }
    }

    public void getImageDownloadUrl(String userId, OnSuccessListener<Uri> onSuccessListener) {
        StorageReference storageReference = storage.getReference().child("user").child(userId);
        storageReference.getDownloadUrl().addOnSuccessListener(onSuccessListener);
    }


    public void updateProfileImage(String userId, String imageUrl) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("post");

        // Create a map to update both the user's profile image and posts
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("user/" + userId + "/image", imageUrl);

        // Query posts by user ID and update the userProfileImg field
        postsRef.orderByChild("userid").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postKey = postSnapshot.getKey();
                    updateMap.put("post/" + postKey + "/userProfileImg", imageUrl);
                }

                // Perform the multi-location update
                FirebaseDatabase.getInstance().getReference().updateChildren(updateMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

    }



}
