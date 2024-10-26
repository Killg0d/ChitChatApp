package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private String chatId;// Unique chat ID for each conversation
    String receiverId;
    String chatName;
    String profileImage;
    ListView messageView;
    List<Message> messageTextList;
    MessageTextAdapter aa;
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

        Log.d("chatId",chatId);
        Log.d("recieverId",receiverId);
        // Load the previous messages for this conversation
//        loadMessages(chatId); // Load messages using the unique chat ID
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Send button click listener
        sendbutton.setOnClickListener(v -> {
            msgtext = messageInput.getText().toString();
            if (!msgtext.isEmpty()) {
                new ChatManager().sendMessage(chatId, user, msgtext); // Send message to Firebase Firestore
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
        messageView= findViewById(R.id.messageListView);
        messageTextList = new ArrayList<>();
        aa = new MessageTextAdapter(this.getApplicationContext(), messageTextList);
        messageView.setAdapter(aa);
        fetchMessages(chatId,receiverId);
    }



    public void fetchMessages(String chatId, String receiverId) {
        Log.d("fetchMessages","Working");
        messageTextList.clear();
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
                            Timestamp sentAtTimestamp = document.getTimestamp("sentAt");
                            if (sentAtTimestamp != null) {
                                Date sentAtDate = sentAtTimestamp.toDate();
                                Log.d("sentAt", new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(sentAtDate));
                            } else {
                                Log.d("sentAt", "Timestamp is null");
                            }
                            Message message = new Message(document.getString("senderId"), document.getString("message"), sentAtTimestamp);
                            Log.d("fetchmsg",message.toString());
                            messageTextList.add(message);
                           // Add each message to the UI
                        }
                        MessageTextAdapter aa = new MessageTextAdapter(this.getApplicationContext(), messageTextList);
                        messageView.setAdapter(aa);
                        aa.notifyDataSetChanged();
                        // Scroll to the bottom of the ScrollView after loading all messages
                        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                    } else {
                        Log.w("Firestore", "Error getting messages.", task.getException());
                    }
                });

    }
}
