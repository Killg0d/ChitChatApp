package com.example.project;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatManager {

    private FirebaseFirestore firestore;

    public ChatManager() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void createChat(boolean isGroup, List<String> participants, String lastMessage) {
        // Create a new chat document
        String chatId = firestore.collection("chats").document().getId(); // Auto-generated ID
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("isGroup", isGroup);
        chatData.put("participants", participants);
        chatData.put("createdAt", Timestamp.now());
        chatData.put("lastMessage", lastMessage);
        chatData.put("lastMessageTime", Timestamp.now());

        firestore.collection("chats")
                .document(chatId)
                .set(chatData)
                .addOnSuccessListener(aVoid -> {
                    // Chat created successfully
                    Log.d("ChatManager", "Chat created with ID: " + chatId);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("ChatManager", "Error creating chat", e);
                });
    }
}
