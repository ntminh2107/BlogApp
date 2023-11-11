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

    public AddPostViewModel() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();
        postRef = FirebaseDatabase.getInstance().getReference().child("post");
        userRef = FirebaseDatabase.getInstance().getReference().child("user");
    }

    public void addPost(Post p, Uri imguri)
    {
        if(user != null)
        {
            userRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String username = snapshot.child("username").getValue(String.class);
                        if(username!=null)
                        {
                            p.setUsername(username);
                            p.setUserid(user.getUid());
                            if(imguri != null)
                            {
                                StorageReference imgRef = storage.child("images/" + user.getUid() + "/"
                                        + System.currentTimeMillis() +".jpg");
                                imgRef.putFile(imguri).addOnSuccessListener(taskSnapshot -> {
                                    imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        p.setImg(uri.toString());
                                        addPostToDB(p);

                                    });
                                }).addOnFailureListener(e -> {addPostToDB(p);});
                            } else {
                                addPostToDB(p);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void addPostToDB(Post post)
    {
        String PostID = postRef.push().getKey();
        postRef.child(PostID).setValue(post).addOnSuccessListener(task ->{
            addPostResult.setValue(true);
;        }).addOnFailureListener(e -> {
            addPostResult.setValue(false);
        });
    }

}
