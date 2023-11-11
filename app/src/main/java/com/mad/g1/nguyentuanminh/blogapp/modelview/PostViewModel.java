package com.mad.g1.nguyentuanminh.blogapp.modelview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {

    private MutableLiveData<List<Post>> postList;

    public LiveData<List<Post>> getPosts() {
        if (postList == null) {
            postList = new MutableLiveData<>();
            loadPosts();
        }
        return postList;
    }

    private void loadPosts() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("post");

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
}
