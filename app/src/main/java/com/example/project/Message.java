package com.example.project;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private String senderId;
    private String message;
    private Timestamp sentAt;

    // Required empty constructor for Firestore serialization
    public Message() {}

    public Message(String senderId, String message, Timestamp timestamp) {
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

    public Timestamp getTimestamp() {
        return sentAt;
    }

    @NonNull
    @Override
    public String toString() {
        String formattedDate = "null";
        if (sentAt != null) {
            Date date = sentAt.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            formattedDate = sdf.format(date);
        }

        return "Message{" +
                "senderId='" + senderId + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + formattedDate +
                '}';
    }

}
