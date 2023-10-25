package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mad.g1.nguyentuanminh.blogapp.R;

public class LoginView extends AppCompatActivity {
    TextView registerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerTV = findViewById(R.id.registerText);

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginView.this,RegisterView.class);
                startActivity(intent);
            }
        });

    }
}