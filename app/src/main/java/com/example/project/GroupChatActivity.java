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
    private ScrollView scrollView;

    private String chatId;               // Unique group ID for each group conversation
    private String groupName;             // Group name for display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        emojiButton = findViewById(R.id.emoji_button);
        backButton = findViewById(R.id.back_button);
        clickToName = findViewById(R.id.clicktoname);

        messagesLayout = findViewById(R.id.messages);

        scrollView=findViewById(R.id.messages_scroll_view);


        // Retrieve the group name and ID from the Intent
        Intent intent = getIntent();
        groupName = intent.getStringExtra("GROUP_NAME");
        chatId = intent.getStringExtra("chatId"); // Ensure GROUP_ID is passed for unique group chat

        // Display the group name

        TextView chatname=findViewById(R.id.chat_name);
        chatname.setText(groupName);
        // Back button functionality
        backButton.setOnClickListener(v -> {
            onBackPressed();
            finish();

        });

        // Handle send button click event
        sendButton.setOnClickListener(view -> {
            String message = messageInput.getText().toString();
            if (!message.isEmpty()) {
                new ChatManager().sendMessage(chatId,FirebaseAuth.getInstance().getCurrentUser().getUid(),message);
                fetchMessages(chatId);
                messageInput.setText(""); // Clear input field after sending
            }
        });

        // Fetch previous messages for the group
        fetchMessages(chatId);
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
        // Create a new TextView for the message
        TextView messageTextView = new TextView(this);

        // Set the text of the TextView to the message
        messageTextView.setText(messageText);

        // Set layout parameters for the TextView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 8, 16, 8); // Optional: Add margins
        messageTextView.setLayoutParams(params);

        // Set style for the TextView (e.g., padding, background)
        messageTextView.setPadding(16, 16, 16, 16);
        messageTextView.setBackgroundResource(R.drawable.message_background); // Set background drawable
        messageTextView.setTextColor(getResources().getColor(R.color.black)); // Set text color

        // Add the new TextView to the messages layout
        messagesLayout.addView(messageTextView);

        // Scroll to the bottom of the ScrollView when a new message is added
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

}
