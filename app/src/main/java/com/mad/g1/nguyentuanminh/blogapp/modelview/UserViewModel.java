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
import com.mad.g1.nguyentuanminh.blogapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<List<User>> userList;
    private FirebaseAuth firebaseAuth;

    public UserViewModel() {
        userList = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        fetchAllUsers();
    }

    // Method to get a LiveData of the list of users
    public LiveData<List<User>> getUserList() {
        return userList;
    }

    public String getCurrentUserID()
    {
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
        return firebaseUser.getUid();
    }



    // Method to fetch all users from Firebase Realtime Database
    private void fetchAllUsers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        // Exclude the current user from the list
                        if (!user.getIdUser().equals(getCurrentUserID())) {
                            users.add(user);
                        }
                    }
                }

                userList.setValue(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("error fetching user");
                // Handle errors, e.g., show a toast or log the error
            }
        });
    }

    // Method to search users based on username or fullname
    public void searchUsers(String query) {
        String currentUserId = getCurrentUserID();

        if (query.trim().isEmpty()) {
            // If the query is empty, show all users
            userList.setValue(userList.getValue());
            return;
        }

        List<User> filteredUsers = new ArrayList<>();

        for (User user : userList.getValue()) {
            // Search by username or fullname (case-insensitive)
            if (user.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                    user.getFullname().toLowerCase().contains(query.toLowerCase())) {
                // Kiểm tra xem user có phải là current user hay không
                if (currentUserId == null || !user.getIdUser().equals(currentUserId)) {
                    filteredUsers.add(user);
                }
            }
        }

        userList.setValue(filteredUsers);
    }

    public LiveData<User> getUserDetails(String userId) {
        MutableLiveData<User> userDetails = new MutableLiveData<>();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    userDetails.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        return userDetails;
    }


}
