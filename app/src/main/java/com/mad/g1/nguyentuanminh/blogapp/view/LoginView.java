package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.modelview.LoginViewModel;

public class LoginView extends AppCompatActivity {
    private TextView registerTV;
    private EditText emailLoginET,passLoginET;
    private Button loginBTN;
    private FirebaseAuth firebaseAuth;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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
                    loginViewModel.loginUser(textEmail,textPassword);
                    loginViewModel.getLoginResult().observe(LoginView.this,loginSuccess ->{
                        if(loginSuccess != null && loginSuccess){
                            Toast.makeText(LoginView.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(LoginView.this, HomeView.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String error = loginViewModel.getErrorMSG().getValue();
                            Toast.makeText(LoginView.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
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
}