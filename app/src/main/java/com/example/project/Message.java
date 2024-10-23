package com.example.project;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "senderId='" + senderId + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
