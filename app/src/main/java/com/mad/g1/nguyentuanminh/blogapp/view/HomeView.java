package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.HomeFragment;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.SearchFragment;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.ProfileFragment;
import com.mad.g1.nguyentuanminh.blogapp.R;

public class HomeView extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if(item.getItemId() == R.id.navigation_home)
            {
                selectedFragment = new HomeFragment();
            } else if(item.getItemId() == R.id.navigation_search)
            {
                selectedFragment = new SearchFragment();
            } else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            }
            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}