package com.mad.g1.nguyentuanminh.blogapp.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.test.ProfileActivityTest;

public class LoginView extends AppCompatActivity {
    private TextView registerTV;
    private EditText emailLoginET,passLoginET;
    private Button loginBTN;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerTV = findViewById(R.id.registerText);
        emailLoginET = findViewById(R.id.inpEmail);
        passLoginET = findViewById(R.id.inpPw);
        loginBTN = findViewById(R.id.loginBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = emailLoginET.getText().toString();
                String textPassword = passLoginET.getText().toString();
                if(TextUtils.isEmpty(textEmail))
                {
                    Toast.makeText(LoginView.this, "pls entered your email", Toast.LENGTH_SHORT).show();
                    emailLoginET.setError("email is required");
                    emailLoginET.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches())
                {
                    Toast.makeText(LoginView.this, "wrong email", Toast.LENGTH_SHORT).show();
                    emailLoginET.setError("invalid email");
                    emailLoginET.requestFocus();
                } else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(LoginView.this, "pls entered your password", Toast.LENGTH_SHORT).show();
                    emailLoginET.setError("password is required");
                    emailLoginET.requestFocus();
                } else {
                    loginUser(textEmail,textPassword);
                }

            }
        });



        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginView.this,RegisterView.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(String textEmail, String textPassword) {
        firebaseAuth.signInWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginView.this, "login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginView.this, ProfileActivityTest.class);
                    startActivity(intent);
                    finish();
                } else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e)
                    {
                        emailLoginET.setError("User does not exists!!!!");
                        emailLoginET.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        emailLoginET.setError("invalid credential");
                        emailLoginET.requestFocus();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginView.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(LoginView.this, "somethings went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}