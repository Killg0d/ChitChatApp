package com.example.project;

public class Message {
    private String senderId;
    private String recipientId;
    private String message;
    private long timestamp;

    // Required empty constructor for Firestore serialization
    public Message() {}

    public Message(String senderId, String recipientId, String message, long timestamp) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
