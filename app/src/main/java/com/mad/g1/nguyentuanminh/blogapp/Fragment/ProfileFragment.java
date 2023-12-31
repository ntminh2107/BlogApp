package com.mad.g1.nguyentuanminh.blogapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.adapter.PostAdapter;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.modelview.ProfileViewModel;
import com.mad.g1.nguyentuanminh.blogapp.view.DetailProfileView;
import com.mad.g1.nguyentuanminh.blogapp.view.EditProfileView;
import com.mad.g1.nguyentuanminh.blogapp.view.LoginView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private TextView fullnameText, usernameText, desText;
    private Button editBtn, logoutBtn;
    private ImageView avaView;
    private PostAdapter postAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userid = firebaseUser.getUid();


        fullnameText = root.findViewById(R.id.fullnameTextViewTest);
        usernameText = root.findViewById(R.id.UsernameTextViewTest);
        desText = root.findViewById(R.id.DesTextViewTest);
        editBtn = root.findViewById(R.id.editProfileBtnTest);
        logoutBtn = root.findViewById(R.id.LogoutBtnTest);
        avaView = root.findViewById(R.id.avatarImageViewTest);

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileView.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> viewModel.logout());
        avaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailProfileView.class);
                intent.putExtra("userId",userid);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getUserProfileData().observe(getViewLifecycleOwner(), this::updateUI);
        viewModel.getLogoutResult().observe(getViewLifecycleOwner(), this::handleLogoutResult);
    }

    private void updateUI(User user) {
        if (user != null) {
            fullnameText.setText(user.getFullname());
            usernameText.setText(user.getUsername());
            desText.setText("Des: " + user.getDes());
            Glide.with(requireContext()).load(user.getImage()).into(avaView);
        }
    }

    private void handleLogoutResult(Boolean result) {
        if (result) {
            Intent intent = new Intent(requireContext(), LoginView.class);
            Toast.makeText(requireContext(), "Logout Success", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            requireActivity().finish();
        }
    }
}
