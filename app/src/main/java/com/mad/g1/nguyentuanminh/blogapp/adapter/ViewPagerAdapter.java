package com.mad.g1.nguyentuanminh.blogapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mad.g1.nguyentuanminh.blogapp.Fragment.HomeFragment;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.ProfileFragment;
import com.mad.g1.nguyentuanminh.blogapp.Fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int numPage;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        numPage = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 : return new SearchFragment();
            case 1 : return new HomeFragment();
            case 2: return new ProfileFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return numPage;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0: return "search";
            case 1: return "home";
            case 2: return "profile";
        }
        return "home";
    }
}
