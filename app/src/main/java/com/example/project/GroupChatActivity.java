package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private ImageButton sendButton, emojiButton, backButton;
    private LinearLayout clickToName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat); // Ensure correct layout is used

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        emojiButton = findViewById(R.id.emoji_button);
        backButton = findViewById(R.id.back_button);
        clickToName = findViewById(R.id.clicktoname);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Back button functionality - only handles navigation back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupChatActivity.this, MainChat.class)); // Navigate back to the previous activity
            }
        });

        // Click listener for the group name and profile picture - opens group details
        clickToName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGroupDetails(); // Start GroupDetailsActivity
            }
        });

        // Handle send button click event
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageInput.getText().toString();
                if (!message.isEmpty()) {
                    // Send message logic here
                    messageInput.setText(""); // Clear input field after sending
                }
            }
        });

        // Handle emoji button click event
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Emoji selection logic here
            }
        });
    }

    // Method to open GroupDetailsActivity
    private void openGroupDetails() {
        Intent intent = new Intent(this, GroupDetailsActivity.class);
        startActivity(intent); // Open the GroupDetailsActivity
    }
}
