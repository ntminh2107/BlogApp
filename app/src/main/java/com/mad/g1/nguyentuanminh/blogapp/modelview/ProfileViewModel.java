package com.mad.g1.nguyentuanminh.blogapp.modelview;

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

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<User> userProfileData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
    private final FirebaseAuth auth;

    public ProfileViewModel() {
        auth = FirebaseAuth.getInstance();
        loadUserProfile();
    }

    public MutableLiveData<User> getUserProfileData() {
        return userProfileData;
    }

    public MutableLiveData<Boolean> getLogoutResult() {
        return logoutResult;
    }

    public void loadUserProfile() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            String userUid = firebaseUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userUid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    userProfileData.setValue(user);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    public void logout() {
        auth.signOut();
        logoutResult.setValue(true);
    }
}
