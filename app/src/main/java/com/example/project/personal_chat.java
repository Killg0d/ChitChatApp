package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class personal_chat extends AppCompatActivity {
    FirebaseFirestore firestore;
    private LinearLayout msgbox;
    private LinearLayout clicktoname;
    private TextView chatNameTextView;
    private ImageView profileImageView;
    private FirebaseFirestore db;
    private ImageButton sendbutton;
    private LinearLayout messageInputLayout;
    private LinearLayout messagesLayout;  // LinearLayout for messages
    private ScrollView scrollView;        // ScrollView for scrolling messages
    private String msgtext;
    private EditText messageInput;
    private String chatId;                // Unique chat ID for each conversation
    String receiverId;
    String chatName;
    String profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        firestore= FirebaseFirestore.getInstance();
        chatId = getIntent().getStringExtra("chatId"); // Get the chat ID from the intent
        // Retrieve the passed data from MainChat activity
        chatName = getIntent().getStringExtra("chatName");
        profileImage = getIntent().getStringExtra("profileImage");
        receiverId = getIntent().getStringExtra("receiverId");
        FirebaseFirestore.getInstance().collection("users").document(receiverId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            chatName = documentSnapshot.getString("fullName");
                            profileImage = documentSnapshot.getString("profileImageUrl");

                            // Use the retrieved fullName and profileImageUrl as needed
                            // ...
                        } else {
                            // Handle the case where the document doesn't exist
                            // ...
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred during the dataretrieval
                        // ...
                    }
                });



        // Initialize the views
        chatNameTextView = findViewById(R.id.chat_name);
        profileImageView = findViewById(R.id.profile_image);
        chatNameTextView.setText(chatName);
        if (profileImage != null) {
            Glide.with(this)
                    .load(profileImage)
                    .placeholder(R.drawable.person) // Placeholder while loading
                    .error(R.drawable.person) // Error image if loading fails
                    .into(profileImageView);
        }
        else
        {
            profileImageView.setImageResource(R.drawable.person);
        }
        sendbutton = findViewById(R.id.send_button);
        messageInputLayout = findViewById(R.id.message_input_layout);
        messageInput = findViewById(R.id.message_input);
        messagesLayout = findViewById(R.id.messages); // LinearLayout where messages are added
        scrollView = findViewById(R.id.messages_scroll_view); // ScrollView for scrolling

        // Set up the LinearLayout for navigating to the profile
        clicktoname = findViewById(R.id.clicktoname);
        clicktoname.setOnClickListener(v -> {
            Intent intent = new Intent(this, Chat_partner_profile.class);
            startActivity(intent);
        });
        fetchMessages(chatId,receiverId);
        Log.d("chatId",chatId);
        Log.d("recieverId",receiverId);
        // Load the previous messages for this conversation
//        loadMessages(chatId); // Load messages using the unique chat ID

        // Send button click listener
        sendbutton.setOnClickListener(v -> {
            msgtext = messageInput.getText().toString();
            if (!msgtext.isEmpty()) {
                new ChatManager().sendMessage(chatId, receiverId, msgtext); // Send message to Firebase Firestore
//                addMessageToLayout(msgtext); // Add the message to the UI
                fetchMessages(chatId, receiverId); // Save message in Firestore
                messageInput.setText(""); // Clear the input field
            }
        });

        // Handle the back button click
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            finish();
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

    // Method to send the message to Firebase Firestore
//    private void fetchMessage(String chatId, String senderEmail, String receiverEmail, String messageText) {
//        long timestamp = System.currentTimeMillis();
//        Message message = new Message(senderEmail, receiverEmail, messageText, timestamp);
//
//        // Add the message to Firestore under the unique chat ID
//        db.collection("chats")
//                .document(chatId)           // Use chatId instead of senderEmail
//                .collection("messages")
//                .add(message)
//                .addOnSuccessListener(documentReference -> {
//                    Log.d("Firestore", "Message sent successfully");
//                })
//                .addOnFailureListener(e -> {
//                    Log.w("Firestore", "Error sending message", e);
//                });
//    }

    // Method to load previous messages from Firestore
//    private void loadMessages(String chatId) {
//        // Query Firestore to retrieve the messages from the unique chat ID
//        db.collection("chats")
//                .document(chatId)           // Use chatId instead of senderEmail
//                .collection("messages")
//                .orderBy("timestamp")        // Order by the timestamp to display messages in the correct order
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Message message = document.toObject(Message.class);
//                            addMessageToLayout(message.getMessage()); // Add each message to the UI
//                        }
//
//                        // Scroll to the bottom of the ScrollView after loading all messages
//                        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
//                    } else {
//                        Log.w("Firestore", "Error getting messages.", task.getException());
//                    }
//                });
//    }
    public void fetchMessages(String chatId, String receiverId) {
        Log.d("fetchMessages","Working");
        firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("sentAt", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    Log.w("Firestore", "Successful");
                    if (task.isSuccessful()) {
                        Log.w("Firestore", "Successful2");
                        if(task.getResult().isEmpty())
                        {
                            Log.d("No messages foumd","No messages found");
                        }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Message message = document.toObject(Message.class);
                            Log.d("fetchmsg",message.toString());
                            addMessageToLayout(message.getMessage()); // Add each message to the UI
                        }

                        // Scroll to the bottom of the ScrollView after loading all messages
                        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                    } else {
                        Log.w("Firestore", "Error getting messages.", task.getException());
                    }
                });

    }
}
