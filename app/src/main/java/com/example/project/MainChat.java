package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainChat extends BaseActivity {

    private ListView chatListView;
    private CustomMessageAdapter messageAdapter;
    private List<UserMessage> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainchat);

        // Initialize the ListView for displaying chat messages
        chatListView = findViewById(R.id.chatListView);
        FloatingActionButton fabNewChat = findViewById(R.id.fab_new_chat);
        fabNewChat.setOnClickListener(v -> {
            // Handle FAB click, for example, start a new activity to create a chat
            Intent intent = new Intent(MainChat.this, SelectContactActivity.class);
            startActivity(intent);
        });
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("chats")
                .whereArrayContains("participants", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String>participants = (List<String>) document.get("participants");
                            if (participants != null && participants.size() == 2) { // Assuming 2 participants in a chat
                                String recieverid = participants.get(0).equals(userId) ? participants.get(1) : participants.get(0);
                                String chatId = document.getId();
                                FirebaseFirestore.getInstance().collection("users").document(recieverid)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    String chatName = documentSnapshot.getString("fullName"); // Assuming "fullName" field in "users"
                                                    String profileImage = documentSnapshot.getString("profileurl");
                                                    String description = documentSnapshot.getString("description");// Assuming "profileImage" field in "users"
                                                    //int profileImage = R.drawable.person;
                                                    messageList.add(new UserMessage(chatName, description, profileImage,chatId,recieverid)); // Add to messageList
                                                    messageAdapter.notifyDataSetChanged(); // Notify adapter of data change
                                                }
                                            }
                                        });


                                // Use otherParticipantId as needed
                                // ...
                            }
                        }
                    } else {
                        // Handle errors
                        // ...
                    }
                });


//        // Create sample chat data (replace this with actual data from the server or database)
        messageList = new ArrayList<>();
//        messageList.add(new MessageList("John", "Hello! How are you?", R.drawable.person));
//        messageList.add(new MessageList("Alice", "Hey! What's up?", R.drawable.person));
//        messageList.add(new MessageList("Bob", "Did you have lunch?", R.drawable.person));
//
//        // Initialize the adapter and set it to the ListView
        messageAdapter = new CustomMessageAdapter(this, messageList, 0);  // Layout type 1 uses EditText for messages
        chatListView.setAdapter(messageAdapter);
//
//        // Handle clicks on ListView items to open personal chat
        chatListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainChat.this, personal_chat.class);

            // Pass the selected chat's data (e.g., name, last message) to the personal_chat activity
            intent.putExtra("chatName", messageList.get(position).getName());
            intent.putExtra("lastMessage", messageList.get(position).getMessage());
            intent.putExtra("profileImage", messageList.get(position).getProfilePictureURL());
            intent.putExtra("receiverId", messageList.get(position).getReceiverId());
            intent.putExtra("chatId",messageList.get(position).getChatId());
            Log.d("ChatId", messageList.get(position).getChatId());
            startActivity(intent);
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is still logged in
            Log.d("FirebaseAuth", "User is authenticated: " + currentUser.getUid());
        } else {
            // User is not logged in
            Log.d("FirebaseAuth", "User is not authenticated");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newgrp) {
            startActivity(new Intent(MainChat.this, GroupActivity.class));
            return true;
        } else if (id == R.id.settings) {
            Intent i = new Intent(MainChat.this, ActivitySettings.class);
            startActivity(i);
            return true;
        } else if (id == R.id.signout) {
            SharedPreferences p = getSharedPreferences("Login", MODE_PRIVATE);
            p.edit().putBoolean("isLogin?", false).commit();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainChat.this, Login.class));
            finish();  // To prevent going back to this activity after sign-out
        }
        return super.onOptionsItemSelected(item);
    }
}
