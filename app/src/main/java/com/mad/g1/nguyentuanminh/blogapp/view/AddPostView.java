package com.mad.g1.nguyentuanminh.blogapp.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mad.g1.nguyentuanminh.blogapp.R;
import com.mad.g1.nguyentuanminh.blogapp.model.Post;
import com.mad.g1.nguyentuanminh.blogapp.modelview.AddPostViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddPostView extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgView;
    private Button addImgBtn;
    private Button postBtn;
    private Uri selectedImageUri;
    private Bitmap resizedBitmap;
    private EditText titleET, contentET;

    private AddPostViewModel addPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_view);

        titleET = findViewById(R.id.ETtitle);
        contentET = findViewById(R.id.contentET);
        imgView = findViewById(R.id.imgView);
        addImgBtn = findViewById(R.id.addImgBTN);
        postBtn = findViewById(R.id.addBTN);

        addPostViewModel = new ViewModelProvider(this).get(AddPostViewModel.class);

        addImgBtn.setOnClickListener(v -> openGallery());
        postBtn.setOnClickListener(v -> postImage());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(imageStream, null, options);

                options.inSampleSize = calculateInSampleSize(options, 1200, 630);
                options.inJustDecodeBounds = false;
                imageStream.close();
                imageStream = getContentResolver().openInputStream(selectedImageUri);
                resizedBitmap = BitmapFactory.decodeStream(imageStream, null, options);

                imgView.setImageBitmap(resizedBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void postImage() {
        String title = titleET.getText().toString();
        String content = contentET.getText().toString();
        if (selectedImageUri != null && resizedBitmap != null) {
            addPostViewModel.addPost(title, content, selectedImageUri);
        } else {
            addPostViewModel.addPost(title, content, null);
        }

        Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeView.class);
        startActivity(intent);
        finish();
    }
}
