package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainChat extends AppCompatActivity {

    private ListView chatListView;
    private MessageAdapter messageAdapter;
    private List<MessageList> messageList;

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


        // Create sample chat data (replace this with actual data from the server or database)
        messageList = new ArrayList<>();
        messageList.add(new MessageList("John", "Hello! How are you?", R.drawable.person));
        messageList.add(new MessageList("Alice", "Hey! What's up?", R.drawable.person));
        messageList.add(new MessageList("Bob", "Did you have lunch?", R.drawable.person));

        // Initialize the adapter and set it to the ListView
        messageAdapter = new MessageAdapter(this, messageList, 0);  // Layout type 1 uses EditText for messages
        chatListView.setAdapter(messageAdapter);

        // Handle clicks on ListView items to open personal chat
        chatListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainChat.this, personal_chat.class);

            // Pass the selected chat's data (e.g., name, last message) to the personal_chat activity
            intent.putExtra("chatName", messageList.get(position).getName());
            intent.putExtra("lastMessage", messageList.get(position).getMessage());
            intent.putExtra("profileImage", messageList.get(position).getProfilePictureResId());


            startActivity(intent);
        });
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
            startActivity(new Intent(MainChat.this, Login.class));
            finish();  // To prevent going back to this activity after sign-out
        }
        return super.onOptionsItemSelected(item);
    }
}
