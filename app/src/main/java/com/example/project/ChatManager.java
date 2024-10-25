package com.example.project;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatManager {
    String chatId;
    private FirebaseFirestore firestore;

    public ChatManager() {
        firestore = FirebaseFirestore.getInstance();
    }

    public String createChat(boolean isGroup, List<String> participants, String lastMessage) {
        // Create a new chat document
        chatId = firestore.collection("chats").document().getId(); // Auto-generated ID
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
        return chatId;
    }

    public String createChat(boolean isGroup, List<String> participants, String lastMessage,String groupName) {
        // Create a new chat document
        chatId = firestore.collection("chats").document().getId(); // Auto-generated ID
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("isGroup", isGroup);
        chatData.put("participants", participants);
        chatData.put("createdAt", Timestamp.now());
        chatData.put("lastMessage", lastMessage);
        chatData.put("lastMessageTime", Timestamp.now());
        chatData.put("groupName",groupName);

        firestore.collection("chats")
                .document(chatId)
                .set(chatData)
                .addOnSuccessListener(aVoid -> {
                    // Chat created successfully
                    Log.d("ChatManager", "Group Chat created with ID: " + chatId);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("ChatManager", "Error creating group chat", e);
                });
        return chatId;
    }


    public void sendMessage(String chatId, String senderId, String messageText) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("senderId", senderId);
        messageData.put("message", messageText);
        messageData.put("sentAt", FieldValue.serverTimestamp()); // Use server timestamp for consistency
        String messageId = firestore.collection("chats").document(chatId)
                .collection("messages").document().getId();
        firestore.collection("chats").document(chatId)
                .collection("messages").document(messageId)
                .set(messageData)
                .addOnSuccessListener(new OnSuccessListener<Void>() { // Use Void here
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Message sent successfully
                        // ...
                        Log.d("ChatManager", "Message sent successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        // ...
                    }
                });
    }
    public void sendMessage(String chatId, String senderId, String messageText,boolean isGroup) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("senderId", senderId);
        messageData.put("message", messageText);
        messageData.put("sentAt", FieldValue.serverTimestamp()); // Use server timestamp for consistency
        String messageId = firestore.collection("chats").document(chatId)
                .collection("messages").document().getId();
        firestore.collection("chats").document(chatId)
                .collection("messages").document(messageId)
                .set(messageData)
                .addOnSuccessListener(new OnSuccessListener<Void>() { // Use Void here
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Message sent successfully
                        // ...
                        Log.d("ChatManager", "Message sent successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        // ...
                    }
                });
    }

}
