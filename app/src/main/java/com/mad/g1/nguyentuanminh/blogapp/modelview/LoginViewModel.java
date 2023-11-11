package com.mad.g1.nguyentuanminh.blogapp.modelview;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private MutableLiveData<String> errorMSG = new MutableLiveData<>();
    private FirebaseAuth authm;

    public LoginViewModel() {
        authm = FirebaseAuth.getInstance();
    }

    public MutableLiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public MutableLiveData<String> getErrorMSG() {
        return errorMSG;
    }

    public void loginUser(String email, String pass) {
        authm.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Update LiveData on the main thread
                loginResult.setValue(true);
                // Clear error message
                errorMSG.setValue("");
            } else {
                // Handle login failure on the main thread
                handleLoginFailed(task.getException());
            }
        });
    }

    private void handleLoginFailed(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidUserException) {
            loginResult.setValue(false);
            errorMSG.setValue("Invalid user");
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            loginResult.setValue(false);
            errorMSG.setValue("Invalid Credential");
        } else {
            loginResult.setValue(false);
            errorMSG.setValue("Login Failed, please try again");
        }
    }
}
