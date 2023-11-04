package com.mad.g1.nguyentuanminh.blogapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.User;
import com.mad.g1.nguyentuanminh.blogapp.test.ProfileActivityTest;

import java.util.Date;
import java.util.HashMap;

public class EditProfileView extends AppCompatActivity {
    private ImageView avaIMG;
    private EditText editFullnameET, editDOBET, editPOBET, editDesET, editUsernameET;
    private Spinner GenderSpinner;

    private Button submitButton;
    FirebaseAuth AuthProfile;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Uri selectedImage;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_view);

        AuthProfile = FirebaseAuth.getInstance();
        firebaseUser = AuthProfile.getCurrentUser();
        String userID = firebaseUser.getUid();

        avaIMG = findViewById(R.id.ChangeAvaIV);
        editFullnameET = findViewById(R.id.editFullnameET);
        editDOBET = findViewById(R.id.editDOBET);
        editPOBET = findViewById(R.id.editPOBET);
        editDesET = findViewById(R.id.EditDesET);
        editUsernameET = findViewById(R.id.EditUsernameET);

        submitButton = findViewById(R.id.submitEditBTN);

        //Tạo GenderSpinner để chọn giới tính
        GenderSpinner = findViewById(R.id.EditGenderET);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.add("male");
        genderAdapter.add("female");
        GenderSpinner.setAdapter(genderAdapter);


        avaIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("user").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //đọc dữ liệu từ db firestore và set text vào ET của các trường trong user
                User user = snapshot.getValue(User.class);
                editFullnameET.setText(user.getFullname());
                editDOBET.setText(user.getDob());
                editPOBET.setText(user.getPob());
                editDesET.setText(user.getDes());
                editUsernameET.setText(user.getUsername());

                //Glide sẽ được sử dụng để fetch image từ link mà user.getImage có
                // được để có thể hiển thị ảnh của hồ sơ người dùng
                Glide.with(EditProfileView.this).load(user.getImage()).into(avaIMG);
                if (user.getGender().toString().equalsIgnoreCase("male")) {
                    GenderSpinner.setSelection(0);
                } else if (user.getGender().toString().equalsIgnoreCase("female")) {
                    GenderSpinner.setSelection(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference();

                                        /*nhận input từ các trường sau khi hoàn thành chỉnh sửa
                                        các trường trong thông tin cá nhân và cập nhật lại trong database*/
                DatabaseReference editRef = reference.child("user").child(userID);
                editRef.child("fullname").setValue(editFullnameET.getText().toString());
                editRef.child("dob").setValue(editDOBET.getText().toString());
                editRef.child("pob").setValue(editPOBET.getText().toString());
                editRef.child("username").setValue(editUsernameET.getText().toString());
                editRef.child("des").setValue(editDesET.getText().toString());
                editRef.child("gender").setValue(GenderSpinner.getSelectedItem().toString());

                if (selectedImage != null) {
                    StorageReference storageReference = storage.getReference().child("user").child(userID);
                    storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageURL = uri.toString();
                                        User user = new User();
                                        user.setImage(imageURL);
                                        reference = FirebaseDatabase.getInstance().getReference();
                                        editRef.child("image").setValue(imageURL);

                                        //sau khi cập nhật hoàn thành tạo intent để quay lại giao diện
                                        // thông tin cá nhân và hoàn thành activity này
                                    }
                                });

                            }
                        }
                    });
                }
                Intent intent = new Intent(EditProfileView.this, ProfileActivityTest.class);
                startActivity(intent);
                finish();
            }

        });

    }
//xử lý ảnh sau khi được upload và lưu vào trong realtimeDB
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //kiem tra du lieu truoc co ton tai khong
        if (data != null) {
            if (data.getData() != null) {

                //Lấy URI (Uniform Resource Identifier) của hình ảnh đã được chọn từ dữ liệu trả về.
                // URI này đại diện cho địa chỉ tới tệp hình ảnh trên thiết bị.
                Uri uri = data.getData(); // filepath
                FirebaseStorage storage = FirebaseStorage.getInstance();
                String uid = firebaseUser.getUid();

                /* Xây dựng một đối tượng StorageReference để đại diện cho vị trí
                 mà bạn muốn lưu trữ hình ảnh trong Firebase Storage.
                 Đường dẫn này được xây dựng dựa trên UID của người dùng
                 để lưu trữ hình ảnh trong thư mục của họ. */
                StorageReference reference = storage.getReference().child("user").child(uid);


               /* Tải hình ảnh đã được chọn lên Firebase Storage. Đây là một quy trình tải lên,
                            và nếu nó thành công, bạn sẽ có một "snapshot" của quá trình tải lên trong task.*/
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("image", filePath);
                                    //Cập nhật dữ liệu người dùng trong Firebase Realtime Database bằng cách thêm URL hình ảnh đã tải lên vào nút người dùng.
                                    FirebaseDatabase.getInstance().getReference().child("user")
                                            .child(firebaseUser.getUid())
                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                }
                            });
                            //Đặt hình ảnh đã được chọn vào một ImageView (giả sử avaIMG là một ImageView).
                            // Điều này giúp bạn xem trước hình ảnh đã chọn.
                            avaIMG.setImageURI(data.getData());
                            selectedImage = data.getData();
                        }
                    }
                });
            }
        }
    }
}