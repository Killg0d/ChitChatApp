package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class ProfileSetting extends BaseActivity {
    private static final String DEFAULT_IMAGE_URI = "drawable/download"; // Replace with your default image resource
    private static final String USER_IMAGES_PATH = "images/";
    FrameLayout frameLayout;
    ImageView profileImg;
    Uri imageUri;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        getIntent();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ProfileSetting");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ArrayList<MessageList> mitem = new ArrayList<>();
        mitem.add(new MessageList("Name", "Full Name", R.drawable.person));
        mitem.add(new MessageList("About", "Hi! Using ChitChat", R.drawable.baseline_info_outline_24));
        ListView l = findViewById(R.id.list1);
        MessageAdapter item = new MessageAdapter(this, mitem, 1);
        l.setAdapter(item);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedImageUrl = sharedPreferences.getString("profileImageUrl", null);

        frameLayout = findViewById(R.id.image);
        profileImg = findViewById(R.id.profile_image);
        frameLayout.setOnClickListener(view -> {
            chooseImage();
        });
        if (savedImageUrl != null) {
            Glide.with(this)
                    .load(savedImageUrl)
                    .placeholder(R.drawable.download)
                    .error(R.drawable.download)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImg);
        } else {
            loadProfileImage(FirebaseAuth.getInstance().getCurrentUser());
        }

    }

    private void loadProfileImage(FirebaseUser currentUser) {
        if (currentUser != null) {
            // Get the image reference from Firebase Storage
            StorageReference userImageRef = storageReference.child(USER_IMAGES_PATH + currentUser.getUid() + ".jpg"); // Assuming you're saving images with UID

            // Check if the image exists
            userImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // If it exists, set the profile image
                Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.download) // Optional placeholder
                        .error(R.drawable.download)// Optional error image
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImg);
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("profileImageUrl", uri.toString()); // Store the URL
                editor.apply();
            }).addOnFailureListener(e -> {
                // If it doesn't exist, set the default image
                profileImg.setImageResource(R.drawable.download); // Replace with actual drawable resource
            });
        } else {
            // No user is logged in, show the default image
            profileImg.setImageResource(R.drawable.download); // Replace with actual drawable resource
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImg.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference userImageRef = storageReference.child(USER_IMAGES_PATH + userId + ".jpg");

            userImageRef.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ProfileSetting.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ProfileSetting.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
