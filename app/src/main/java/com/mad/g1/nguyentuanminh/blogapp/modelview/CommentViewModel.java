package com.mad.g1.nguyentuanminh.blogapp.modelview;

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
import com.google.firebase.database.ValueEventListener;
import com.mad.g1.nguyentuanminh.blogapp.model.Comments;

import java.util.ArrayList;
import java.util.List;

public class CommentViewModel extends ViewModel {
    private MutableLiveData<List<Comments>> commentList = new MutableLiveData<>();
    private DatabaseReference postRef;



    public CommentViewModel() {
        // Initialize DatabaseReference for posts
        postRef = FirebaseDatabase.getInstance().getReference().child("post");
    }


    public void addComment(String postId, String content) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Retrieve the username and user profile pic from the user's database entry
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        String userProfilePic = snapshot.child("image").getValue(String.class);

                        if (username != null) {
                            // Create a new comment
                            Comments comment = new Comments(userId, username, userProfilePic, content);

                            // Add the comment to the post
                            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("post").child(postId);
                            DatabaseReference commentsRef = postRef.child("comment"); // Nested under "comments"

                            String commentId = commentsRef.push().getKey();
                            comment.setCommentId(commentId);

                            commentsRef.child(commentId).setValue(comment)
                                    .addOnSuccessListener(aVoid -> {
                                        System.out.println("Comment added successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        System.out.println("Failed to add comment");
                                    });
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

    public LiveData<List<Comments>> getCommentList(String postId) {
        DatabaseReference commentsRef = postRef.child(postId).child("comment");
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Comments> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    // Get the comment ID from the key of the comment snapshot
                    String commentId = commentSnapshot.getKey();

                    // Get the comment data
                    Comments comment = commentSnapshot.getValue(Comments.class);

                    if (comment != null) {
                        // Set the comment ID in the comment object
                        comment.setCommentId(commentId);

                        // Add the comment to the list
                        comments.add(comment);
                    }
                }
                commentList.setValue(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error fetching comments: " + databaseError.getMessage());
            }
        });
        return commentList;
    }

}
