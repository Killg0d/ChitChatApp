package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class personal_chat extends AppCompatActivity {

    LinearLayout clicktoname;
    private TextView chatNameTextView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        // Retrieve the passed data from MainChat activity
        String chatName = getIntent().getStringExtra("chatName");
        int profileImage = getIntent().getIntExtra("profileImage", R.drawable.person);

        // Initialize the views
        chatNameTextView = findViewById(R.id.chat_name); // Assuming there's a TextView for the chat name
        profileImageView = findViewById(R.id.profile_image); // Assuming there's an ImageView for the profile image

        // Set the data to views
        chatNameTextView.setText(chatName);
        profileImageView.setImageResource(profileImage);

        // Set up the LinearLayout for navigating to the profile
        clicktoname = findViewById(R.id.clicktoname);
        clicktoname.setOnClickListener(v -> {
            Intent intent = new Intent(this, Chat_partner_profile.class);
            startActivity(intent);
        });

        // Handle the back button click
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}
