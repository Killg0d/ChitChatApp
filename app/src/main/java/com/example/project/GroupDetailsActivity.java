package com.example.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GroupDetailsActivity extends AppCompatActivity {

    private Toolbar topAppBar;
    private FirebaseFirestore firestore;
    private String chatId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Retrieve the chatId from Intent
        chatId = getIntent().getStringExtra("chatId");

        // Set up the toolbar
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set a placeholder title while loading
        getSupportActionBar().setTitle("Loading...");

        // Fetch and set group name from Firestore
        fetchGroupName();

        // Set back button functionality

    }

    private void fetchGroupName() {
        firestore.collection("chats")
                .document(chatId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch the group name using the key "groupName"
                        String groupName = documentSnapshot.getString("groupName");
                        Log.d("GroupDetailsActivity", "Fetched group name: " + groupName); // Log fetched group name
                        if (groupName != null) {
                           // Set toolbar title to group name
                           getSupportActionBar().setTitle(documentSnapshot.getString("groupName"));
                        } else {
                            Log.e("GroupDetailsActivity", "groupName field is missing in the document");
                        }
                    } else {
                        Log.e("GroupDetailsActivity", "Document does not exist for chatId: " + chatId);
                    }
                })
                .addOnFailureListener(e -> Log.e("GroupDetailsActivity", "Error fetching group name", e));
    }


}
