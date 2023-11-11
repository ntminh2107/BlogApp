package com.mad.g1.nguyentuanminh.blogapp.modelview;


import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileViewModel extends ViewModel {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private DatabaseReference reference;

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

    public void updateProfileImage(String userId, String imageURL) {
        DatabaseReference editRef = reference.child(userId);
        editRef.child("image").setValue(imageURL);
    }
}
