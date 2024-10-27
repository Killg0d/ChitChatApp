package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private String groupName;
    ListView messageView;
    List<Message> messageTextList;
    MessageTextAdapter aa;// Group name for display

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
                fetchMessages(chatId,null);
                messageInput.setText(""); // Clear input field after sending
            }
        });

        clickToName.setOnClickListener(v->{
            Intent intent1=new Intent(GroupChatActivity.this,GroupDetailsActivity.class);
            intent1.putExtra("Group_name",groupName);
            intent1.putExtra("chatId",chatId);
            startActivity(intent1);
        });

        // Fetch previous messages for the group
        messageView= findViewById(R.id.messageListView);
        messageTextList = new ArrayList<>();
        aa = new MessageTextAdapter(this.getApplicationContext(), messageTextList);
        messageView.setAdapter(aa);
        fetchMessages(chatId,null);
    }
    // Method to fetch previous messages for the group chat
    public void fetchMessages(String chatId, String receiverId) {
        Log.d("fetchMessages", "Working");
        messageTextList.clear();

        firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("sentAt", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Error listening for messages.", e);
                        return;
                    }

                    // Clear the previous messages
                    messageTextList.clear();

                    // Check if there are any documents in the snapshot
                    if (snapshots != null && !snapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : snapshots) {
                            Timestamp sentAtTimestamp = document.getTimestamp("sentAt");
                            Date sentAtDate = sentAtTimestamp != null ? sentAtTimestamp.toDate() : null;


                            Message message = new Message(
                                    document.getString("senderId"),
                                    document.getString("message"),
                                    sentAtTimestamp
                            );
                            Log.d("fetchmsg", message.toString());
                            messageTextList.add(message);
                        }
                    } else {
                        Log.d("No messages found", "No messages found");
                    }

                    // Update the adapter and scroll to the bottom
                    MessageTextAdapter aa = new MessageTextAdapter(this.getApplicationContext(), messageTextList);
                    messageView.setAdapter(aa);
                    aa.notifyDataSetChanged();

                    // Scroll to the bottom of the ListView after loading all messages
                    messageView.post(() -> {
                        if (aa.getCount() > 0) {
                            messageView.setSelection(aa.getCount() - 1);
                        }
                    });
                });
    }




}
