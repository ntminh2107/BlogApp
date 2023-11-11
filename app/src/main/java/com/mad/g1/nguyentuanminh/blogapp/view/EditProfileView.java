package com.mad.g1.nguyentuanminh.blogapp.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.modelview.EditProfileViewModel;

public class EditProfileView extends AppCompatActivity {
    private ImageView avaIMG;
    private EditText editFullnameET, editDOBET, editPOBET, editDesET, editUsernameET;
    private Spinner GenderSpinner;

    private Button submitButton;
    private EditProfileViewModel viewModel;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_view);

        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        avaIMG = findViewById(R.id.ChangeAvaIV);
        editFullnameET = findViewById(R.id.editFullnameET);
        editDOBET = findViewById(R.id.editDOBET);
        editPOBET = findViewById(R.id.editPOBET);
        editDesET = findViewById(R.id.EditDesET);
        editUsernameET = findViewById(R.id.EditUsernameET);
        submitButton = findViewById(R.id.submitEditBTN);
        GenderSpinner = findViewById(R.id.EditGenderET);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.add("male");
        genderAdapter.add("female");
        GenderSpinner.setAdapter(genderAdapter);

        // Set up spinner and other UI components


        avaIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        viewModel.getUserReference(viewModel.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        editFullnameET.setText(user.getFullname());
                        editDOBET.setText(user.getDob());
                        editPOBET.setText(user.getPob());
                        editDesET.setText(user.getDes());
                        editUsernameET.setText(user.getUsername());

                        Glide.with(EditProfileView.this).load(user.getImage()).into(avaIMG);
                        if ("male".equalsIgnoreCase(user.getGender())) {
                            GenderSpinner.setSelection(0);
                        } else if ("female".equalsIgnoreCase(user.getGender())) {
                            GenderSpinner.setSelection(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = viewModel.getCurrentUser().getUid();
                String fullname = editFullnameET.getText().toString();
                String dob = editDOBET.getText().toString();
                String pob = editPOBET.getText().toString();
                String username = editUsernameET.getText().toString();
                String des = editDesET.getText().toString();
                String gender = GenderSpinner.getSelectedItem().toString();

                viewModel.updateProfileData(userId, fullname, dob, pob, username, des, gender);

                viewModel.uploadImageAndSetProfile(userId, selectedImage, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            viewModel.getImageDownloadUrl(userId, new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURL = uri.toString();
                                    viewModel.updateProfileImage(userId, imageURL);
                                }
                            });
                        }
                    }
                });

                Intent intent = new Intent(EditProfileView.this, HomeView.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            Glide.with(EditProfileView.this).load(selectedImage).into(avaIMG);
            avaIMG.setImageURI(data.getData());
            selectedImage = data.getData();
        }
    }


}
