package com.mad.g1.nguyentuanminh.blogapp.modelview;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.g1.nguyentuanminh.blogapp.model.User;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<Boolean> regisResult = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;

    public RegisterViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public MutableLiveData<Boolean> getRegisResult() {
        return regisResult;
    }

    public void registerUser(User u, String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                u.setIdUser(firebaseUser.getUid());
                saveUserToDB(u, firebaseUser.getUid());
            } else {
                regisResult.setValue(false);
            }
        });
    }

    private void saveUserToDB(User u, String uid) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("user");
        db.child(uid).setValue(u).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Notify observers about successful registration
                regisResult.setValue(true);
                // Optionally, clear the state after a successful registration
                // regisResult.setValue(null);
            } else {
                // Notify observers about failed registration
                regisResult.setValue(false);
            }
        });
    }
}
