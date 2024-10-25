package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private ImageButton sendButton, emojiButton, backButton;
    private LinearLayout clickToName,messagesLayout;
    private TextView groupNameTextView;
    private FirebaseFirestore firestore;


    private String chatId;               // Unique group ID for each group conversation
    private String groupName;             // Group name for display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        emojiButton = findViewById(R.id.emoji_button);
        backButton = findViewById(R.id.back_button);
        clickToName = findViewById(R.id.clicktoname);
        groupNameTextView = findViewById(R.id.group_name);
        messagesLayout = findViewById(R.id.messages);
        recyclerView = findViewById(R.id.recyclerView);


        // Retrieve the group name and ID from the Intent
        Intent intent = getIntent();
        groupName = intent.getStringExtra("GROUP_NAME");
        chatId = intent.getStringExtra("chatId"); // Ensure GROUP_ID is passed for unique group chat

        // Display the group name
        if (groupName != null && !groupName.isEmpty()) {
            groupNameTextView.setText(groupName);
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the chat as a group in Firestore
        setGroupChatInFirestore();

        // Back button functionality
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(GroupChatActivity.this, MainChat.class));
            finish();
        });

        // Handle send button click event
        sendButton.setOnClickListener(view -> {
            String message = messageInput.getText().toString();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageInput.setText(""); // Clear input field after sending
            }
        });

        // Fetch previous messages for the group
        fetchMessages(chatId);
    }

    // Method to mark chat as group chat in Firestore
    private void setGroupChatInFirestore() {
        DocumentReference groupRef = firestore.collection("chats").document(chatId);
        groupRef.update("isGroup", true)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Group chat marked as group."))
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating isGroup field", e));
    }

    // Method to send a message in a group chat
    private void sendMessage(String messageText) {
        long timestamp = System.currentTimeMillis();

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("message", messageText);
        messageData.put("senderId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        messageData.put("sentAt", timestamp);
        messageData.put("isGroup", true); // Mark message as part of a group

        // Add message to Firestore under the group ID
        firestore.collection("chats").document(chatId).collection("messages")
                .add(messageData)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Message sent successfully"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error sending message", e));

        // Add message to UI
        addMessageToLayout(messageText);
    }

    // Method to fetch previous messages for the group chat
    private void fetchMessages(String groupId) {
        firestore.collection("chats")
                .document(groupId)
                .collection("messages")
                .orderBy("sentAt", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String messageText = document.getString("message");
                            addMessageToLayout(messageText); // Add each message to the UI
                        }
                        // Scroll to the bottom of the ScrollView after loading all messages

                    } else {
                        Log.w("Firestore", "Error fetching messages", task.getException());
                    }
                });
    }

    // Method to dynamically add a message to the layout
    private void addMessageToLayout(String messageText) {
        TextView messageTextView = new TextView(this);
        messageTextView.setText(messageText);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 8, 16, 8);
        messageTextView.setLayoutParams(params);

        messageTextView.setPadding(16, 16, 16, 16);
        messageTextView.setBackgroundResource(R.drawable.message_background); // Background for message
        messageTextView.setTextColor(getResources().getColor(R.color.black)); // Text color

        messagesLayout.addView(messageTextView);

        // Scroll to bottom of ScrollView when new message added

    }
}
