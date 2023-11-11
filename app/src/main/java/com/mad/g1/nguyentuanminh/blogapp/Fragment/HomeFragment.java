package com.mad.g1.nguyentuanminh.blogapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.adapter.PostAdapter;
import com.mad.g1.nguyentuanminh.blogapp.modelview.PostViewModel;
import com.mad.g1.nguyentuanminh.blogapp.view.AddPostView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
private EditText addNewPost;
private ImageView imageView;
private RecyclerView recyclerView;
private PostViewModel postViewModel;
private PostAdapter postAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home,container,false);

        addNewPost = root.findViewById(R.id.postET);
        imageView = root.findViewById(R.id.ProfilepicsView);
        recyclerView = root.findViewById(R.id.PostRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);

        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Observe changes in the post data
        postViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            postAdapter.setPosts(posts);
        });

        addNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), AddPostView.class);
                startActivity(intent);
            }
        });
        return root;
    }
}