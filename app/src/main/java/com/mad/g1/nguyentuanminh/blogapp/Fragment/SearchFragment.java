package com.mad.g1.nguyentuanminh.blogapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.adapter.UserAdapter;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.modelview.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private UserViewModel userViewModel;
    private UserAdapter userAdapter;
    private androidx.appcompat.widget.SearchView searchView;
    private List<User> userList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel and Adapter
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userAdapter = new UserAdapter(new ArrayList<>());

        // Observe changes in the user list
        userViewModel.getUserList().observe(this, users -> {
            userAdapter.setUserList(users);
            userAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize RecyclerView and set its adapter
        RecyclerView userListRV = view.findViewById(R.id.userListRV);
        userListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        userListRV.setAdapter(userAdapter);

        // Initialize SearchView and set its query text listener
        searchView = view.findViewById(R.id.search_user);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userViewModel.searchUsers(newText);
                return true;
            }
        });

        return view;
    }

}
