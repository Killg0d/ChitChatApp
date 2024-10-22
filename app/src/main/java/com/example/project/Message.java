package com.example.project;

public class Message {
    private String senderId;
    private String message;
    private long sentAt;

    // Required empty constructor for Firestore serialization
    public Message() {}

    public Message(String senderId, String message, long timestamp) {
        this.senderId = senderId;
        this.message = message;
        this.sentAt = timestamp;
    }

    // Getters
    public String getSenderId() {
        return senderId;
    }


    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return sentAt;
    }
}
