package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.modelview.RegisterViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterView extends AppCompatActivity {
    private EditText emailReg, fullnameReg, passReg, dobReg, pobReg;
    private RadioButton genderSelected;
    private RadioGroup groupGender;
    private Button RegisterBTN;
    private ProgressBar progressBar;
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        emailReg = findViewById(R.id.emailET);
        fullnameReg = findViewById(R.id.fullnameET);
        passReg = findViewById(R.id.passwordET);
        dobReg = findViewById(R.id.dobET);
        pobReg = findViewById(R.id.pobET);
        progressBar = findViewById(R.id.progressBar);

        //radiobutton for gender
        groupGender = findViewById(R.id.genderGroup);
        groupGender.clearCheck();

        RegisterBTN = findViewById(R.id.registerButton);
        RegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int genderId = groupGender.getCheckedRadioButtonId();
                genderSelected = findViewById(genderId);
                String textEmail = emailReg.getText().toString();
                String textFullname = fullnameReg.getText().toString();
                String textPass = passReg.getText().toString();
                String textDob = dobReg.getText().toString();
                String textPob = pobReg.getText().toString();
                String textGender;

                //thong bao khi de trong 1 truong nao do
                if(TextUtils.isEmpty(textEmail))
                {
                    Toast.makeText(RegisterView.this, "pls enter your email", Toast.LENGTH_SHORT).show();
                    emailReg.setError("email is required");
                    emailReg.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterView.this, "Wrong email format", Toast.LENGTH_SHORT).show();
                    emailReg.setError("Valid email is required");
                    emailReg.requestFocus();

                } else if(TextUtils.isEmpty(textFullname))
                {
                    Toast.makeText(RegisterView.this, "pls enter your fullname", Toast.LENGTH_SHORT).show();
                    fullnameReg.setError("fullname is required");
                    fullnameReg.requestFocus();
                } else if(TextUtils.isEmpty(textPass))
                {
                    Toast.makeText(RegisterView.this, "pls enter your Password", Toast.LENGTH_SHORT).show();
                    passReg.setError("password is required");
                    passReg.requestFocus();
                } else if (textPass.length() < 6 && textPass.length() > 12) {
                    Toast.makeText(RegisterView.this, "Password too short or too long", Toast.LENGTH_SHORT).show();
                    passReg.setError("password needs to have 6-12 characters");
                    passReg.requestFocus();

                } else if (TextUtils.isEmpty(textDob)) {
                    Toast.makeText(RegisterView.this, "Pls enter your Birthdate ", Toast.LENGTH_SHORT).show();
                    dobReg.setError("Birthdate is required");
                    dobReg.requestFocus();
                } else if (TextUtils.isEmpty(textPob))
                {
                    Toast.makeText(RegisterView.this, "Pls enter your Birthplace", Toast.LENGTH_SHORT).show();
                    pobReg.setError("Birthplace is required");
                    pobReg.requestFocus();
                } else if(genderId == -1)
                {
                    Toast.makeText(RegisterView.this, "Pls choose your gender", Toast.LENGTH_SHORT).show();
                    genderSelected.setError("gender is required");
                    genderSelected.requestFocus();
                } else {
                    textGender = genderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);

                    User user = new User(textFullname, textDob, textPob, textGender, textEmail, "no des", "no image");
                    registerViewModel.registerUser(user,textEmail,textPass);
                    registerViewModel.getRegisResult().observe(RegisterView.this, RegisSuccess ->{
                        if(RegisSuccess && RegisSuccess != null)
                        {
                            Toast.makeText(RegisterView.this, "Register Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterView.this,LoginView.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterView.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

}