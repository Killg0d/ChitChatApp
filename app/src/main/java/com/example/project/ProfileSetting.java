package com.example.project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileSetting extends BaseActivity {

    FrameLayout frameLayout;
    ImageView profileImg;

    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    String name;
    String description;
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        getIntent();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ProfileSetting");
        actionBar.setDisplayHomeAsUpEnabled(true);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users")
                .document(userId) // Access the document directly by its ID
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if (!document.exists()) {
                            Log.d("FirestoreData", "No user data found");
                            name = "Error";  // Set default error values
                            description = "Error";
                            // Initialize and populate the list here
                            ArrayList<MessageList> mitem = new ArrayList<>();
                            mitem.add(new MessageList("Name", name, R.drawable.person));
                            mitem.add(new MessageList("About", description, R.drawable.baseline_info_outline_24));

                            l = findViewById(R.id.list1);
                            MessageAdapter item = new MessageAdapter(ProfileSetting.this, mitem, 1);
                            l.setAdapter(item);
                            return; // Early return if the document does not exist
                        }

                        // Get the 'name' field from the document
                         name = document.getString("fullName");

                        // Check if 'description' exists, otherwise set a default value
                         description = document.contains("description")
                                ? document.getString("description")
                                : "Hi! I am using chitchat"; // Default description

                        // Use the name and description as needed
                        Log.d("FirestoreData", "Name: " + name + ", Description: " + description);
                        // Initialize and populate the list here
                        ArrayList<MessageList> mitem = new ArrayList<>();
                        mitem.add(new MessageList("Name", name, R.drawable.person));
                        mitem.add(new MessageList("About", description, R.drawable.baseline_info_outline_24));

                        l = findViewById(R.id.list1);
                        MessageAdapter item = new MessageAdapter(ProfileSetting.this, mitem, 1);
                        l.setAdapter(item);
                        l.setOnItemClickListener(
                                (parent, view, position, id) -> {
                                    MessageList selectedItem = (MessageList) parent.getItemAtPosition(position);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSetting.this);
                                    builder.setTitle("Change "+selectedItem.getName());

                                    // Set up the input
                                    final EditText input = new EditText(ProfileSetting.this);
                                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                                    input.setText(selectedItem.getMessage());
                                    input.setPadding(10,10,10,10);
                                    LinearLayout layout = new LinearLayout(ProfileSetting.this);
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    params.setMargins(40, 20, 40, 20); // Set margins (left, top, right, bottom)
                                    input.setLayoutParams(params);
                                    layout.addView(input); // Add EditText to the layout
                                    builder.setView(layout); // Set the layout as the dialog view


                                    // Set up the buttons
                                    builder.setPositiveButton("OK", (dialog, which) -> {
                                        String newName = input.getText().toString();
                                        if (!newName.isEmpty()) {
                                            // Update the ArrayList with the new name
                                            Log.d("New",newName);
                                           }
                                    });
                                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                                    builder.show();
                                }
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure if any
                        Log.e("FirestoreError", "Error fetching data", e);
                    }
                });

        ArrayList<MessageList> mitem = new ArrayList<>();
        mitem.add(new MessageList("Name", name, R.drawable.person));
        mitem.add(new MessageList("About", description, R.drawable.baseline_info_outline_24));
        ListView l = findViewById(R.id.list1);
        MessageAdapter item = new MessageAdapter(this, mitem, 1);
        l.setAdapter(item);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        frameLayout = findViewById(R.id.image);
        profileImg = findViewById(R.id.profile_image);
        frameLayout.setOnClickListener(view -> {
            chooseImage();
        });
        loadProfileImage(FirebaseAuth.getInstance().getCurrentUser(),profileImg);
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
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading Image...");
        dialog.show();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference userImageRef = storageReference.child(USER_IMAGES_PATH + userId + ".jpg");

            userImageRef.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ProfileSetting.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL
                    userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Store the URL in SharedPreferences

                            // Update Firestore with the profile image URL
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userRef = db.collection("users").document(userId);
                            // Use set() to create the document if it doesn't exist, and merge to avoid overwriting existing fields
                            userRef.set(new HashMap<String, Object>() {{
                                        put("profileImageUrl", downloadUri.toString());
                                    }}, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ProfileSetting.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProfileSetting.this, "Firestore update failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                            Toast.makeText(ProfileSetting.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileSetting.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = ((double) (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                    dialog.setMessage("Percentage: " + (int) progressPercent + "%");
                }
            });
        }
    }


}
