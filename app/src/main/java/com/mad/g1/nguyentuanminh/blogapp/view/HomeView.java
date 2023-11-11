package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.HomeFragment;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.SearchFragment;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.ProfileFragment;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.adapter.ViewPagerAdapter;

public class HomeView extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragement_container, new HomeFragment())
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int Itemid = item.getItemId();
            if(Itemid == R.id.navigation_home)
            {
                selectedFragment = new HomeFragment();
            } else if (Itemid == R.id.navigation_search) {
                selectedFragment = new SearchFragment();
            } else if(Itemid == R.id.navigation_profile)
            {
                selectedFragment = new ProfileFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container,selectedFragment).commit();
            return true;
        });




    }
}