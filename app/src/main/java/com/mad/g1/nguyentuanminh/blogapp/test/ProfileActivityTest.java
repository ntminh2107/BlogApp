package com.mad.g1.nguyentuanminh.blogapp.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.view.LoginView;

public class ProfileActivityTest extends AppCompatActivity {


    private TextView TestfullnameText,TestbirthdateText,TestbirthplaceText,TestgenderText;
    private Button testEditBtn, testchangepassBtn, testlogoutBtn;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private ImageView testAvaView;
    String fullname, birthdate,birthplace, gender,email;

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
        TestbirthdateText = findViewById(R.id.birthDateTextViewTest);
        TestbirthplaceText = findViewById(R.id.birtPlaceTextViewTest);
        TestgenderText = findViewById(R.id.genderTextViewTest);
        testEditBtn =findViewById(R.id.editProfileBtnTest);
        testchangepassBtn = findViewById(R.id.changePassBtnTest);
        testlogoutBtn =findViewById(R.id.LogoutBtnTest);

        String useruid = firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(useruid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                TestfullnameText.setText(user.getFullname());
                TestbirthdateText.setText(user.getDob());
                TestbirthplaceText.setText(user.getPob());
                TestgenderText.setText(user.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        testlogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    birthdate = user.dob;
                    birthplace = user.pob;
                    gender = user.gender;

                    TestfullnameText.setText(fullname);
                    TestbirthdateText.setText(birthdate);
                    TestbirthplaceText.setText(birthplace);
                    TestgenderText.setText(gender);

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