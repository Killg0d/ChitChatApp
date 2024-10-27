package com.example.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailsActivity extends AppCompatActivity {

    private Toolbar topAppBar;
    private FirebaseFirestore firestore;
    private String chatId;
    ListView listView;
    CustomMessageAdapter messageAdapter;
    List<UserMessage> messageList;
    String groupName;
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

        listView = findViewById(R.id.listView);
        messageList = new ArrayList<>();
        messageAdapter = new CustomMessageAdapter(this, messageList, 0);
        listView.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();

    }

    private void fetchGroupName() {
        firestore.collection("chats")
                .document(chatId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch the group name using the key "groupName"
                         groupName = documentSnapshot.getString("groupName");
                        Log.d("GroupDetailsActivity", "Fetched group name: " + groupName); // Log fetched group name
                        if (groupName != null) {
                           // Set toolbar title to group name
                           getSupportActionBar().setTitle(documentSnapshot.getString("groupName"));
                        } else {
                            Log.e("GroupDetailsActivity", "groupName field is missing in the document");
                        }
                        // Fetch participant IDs
                        List<String> participantIds = (List<String>) documentSnapshot.get("participants");
                        if (participantIds != null) {
                            fetchParticipantsDetails(participantIds);
                        } else {
                            Log.e("GroupDetailsActivity", "Participants field is missing in the document");
                        }
                    } else {
                        Log.e("GroupDetailsActivity", "Document does not exist for chatId: " + chatId);
                    }
                })
                .addOnFailureListener(e -> Log.e("GroupDetailsActivity", "Error fetching group name", e));
    }
    private void fetchParticipantsDetails(List<String> participantIds) {
        List<UserMessage> fetchedMessages = new ArrayList<>();
        for (String participantId : participantIds) {
            firestore.collection("users")
                    .document(participantId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String chatName = documentSnapshot.getString("fullName");
                            String profileImage = documentSnapshot.getString("profileurl");
                            String description = documentSnapshot.getString("description");

                            // Log participant details for debugging
                            Log.d("ParticipantDetails", "Name: " + chatName + ", Description: " + description);

                            UserMessage newMessage = new UserMessage(
                                    chatName,
                                    description,
                                    profileImage,
                                    chatId,
                                    participantId,
                                    false,
                                    null
                            );

                            fetchedMessages.add(newMessage);
                            messageAdapter.notifyDataSetChanged();

                            // Update message list once all participants have been fetched
                            if (fetchedMessages.size() == participantIds.size()) {
                                messageList.addAll(fetchedMessages);
                                messageAdapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("GroupDetailsActivity", "Error fetching participant details", e));
        }
    }

}
