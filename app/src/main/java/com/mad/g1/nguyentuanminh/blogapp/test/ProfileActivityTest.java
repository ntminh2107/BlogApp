package com.mad.g1.nguyentuanminh.blogapp.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.view.EditProfileView;
import com.mad.g1.nguyentuanminh.blogapp.view.LoginView;
import com.squareup.picasso.Picasso;

public class ProfileActivityTest extends AppCompatActivity {


    private TextView TestfullnameText,TestUsernameText,TestDesText,TestgenderText;
    private Button testEditBtn, testchangepassBtn, testlogoutBtn;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private ImageView testAvaView;
    private StorageReference reference;
    String fullname, username,des, gender,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_test);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if(firebaseUser == null)
        {
            Toast.makeText(this, "something wents wrong, pls try again", Toast.LENGTH_SHORT).show();
        } else {
            showUserProfile(firebaseUser);
        }

        TestfullnameText = findViewById(R.id.fullnameTextViewTest);
        TestUsernameText = findViewById(R.id.UsernameTextViewTest);
        TestDesText = findViewById(R.id.DesTextViewTest);
        testEditBtn =findViewById(R.id.editProfileBtnTest);
        testchangepassBtn = findViewById(R.id.changePassBtnTest);
        testlogoutBtn =findViewById(R.id.LogoutBtnTest);


        testAvaView = findViewById(R.id.avatarImageViewTest);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("user/" + firebaseUser.getUid().toString());


        String useruid = firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(useruid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                TestfullnameText.setText(user.getFullname());
                TestUsernameText.setText(user.getUsername());
                TestDesText.setText("Des: " +user.getDes());
                Glide.with(ProfileActivityTest.this).load(user.getImage()).into(testAvaView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        testEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivityTest.this, EditProfileView.class);
                 startActivity(intent);
            }
        });

        testlogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(ProfileActivityTest.this, LoginView.class);
                Toast.makeText(ProfileActivityTest.this, "logoutSuccess", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String Useruid = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.child(Useruid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null)
                {
                    fullname = user.fullname;
                    email = firebaseUser.getEmail();
                    username = user.username;
                    des = user.des;



                    TestfullnameText.setText(fullname);
                    TestUsernameText.setText(username);
                    TestDesText.setText(des);

                }else{
                    Toast.makeText(ProfileActivityTest.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}