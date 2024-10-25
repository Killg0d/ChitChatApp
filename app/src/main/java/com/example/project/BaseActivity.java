package com.example.project;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class BaseActivity extends AppCompatActivity {
    protected static final String DEFAULT_IMAGE_URI = "drawable/download"; // Replace with your default image resource
    protected static final String USER_IMAGES_PATH = "images/";
    Uri imageUri;
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    StorageReference storageReference=firebaseStorage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void applyFontSizeToViews(View root, float fontSize) {
        if (root instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) root;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyFontSizeToViews(viewGroup.getChildAt(i), fontSize);
            }
        } else if (root instanceof TextView) {
            // Check if the TextView has the "exclude_font_change" tag
            Object tag = root.getTag();
            if (tag == null || !tag.equals("exclude_font_change")) {
                ((TextView) root).setTextSize(fontSize);
            }
        }
    }

    public void loadProfileImage(FirebaseUser currentUser, ImageView profileImg) {
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
                        .into(profileImg);
            }).addOnFailureListener(e -> {
                // If it doesn't exist, set the default image
                profileImg.setImageResource(R.drawable.download); // Replace with actual drawable resource
            });


        } else {
            // No user is logged in, show the default image
            profileImg.setImageResource(R.drawable.download); // Replace with actual drawable resource
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

}