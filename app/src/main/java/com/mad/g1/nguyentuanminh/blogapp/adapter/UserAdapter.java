package com.mad.g1.nguyentuanminh.blogapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.view.DetailProfileView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    // ViewHolder class to hold the views
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userPics;
        TextView usernameView, fullnameView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userPics = itemView.findViewById(R.id.userPics);
            usernameView = itemView.findViewById(R.id.usernameView);
            fullnameView = itemView.findViewById(R.id.fullnameView);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a user item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Bind data to views for each user
        User user = userList.get(position);

        // Set user profile picture, username, and full name
        Glide.with(holder.itemView.getContext())
                .load(user.getImage())
                .into(holder.userPics);

        holder.usernameView.setText(user.getUsername());
        holder.fullnameView.setText(user.getFullname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getIdUser();
                System.out.println(userId);
                Intent intent = new Intent(v.getContext(), DetailProfileView.class);
                intent.putExtra("userId", userId);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Method to update the dataset and refresh the RecyclerView
    public void setUserList(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }
}
