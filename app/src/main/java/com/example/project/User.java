package com.example.project;

import android.util.Log;

import androidx.annotation.NonNull;

public class User {
    private String userId;
    private String fullName;
    private String email;
    private String description;
    private String profileurl;

    // Empty constructor required for Firestore serialization
    public User() {}

    // Constructor without userId and description (for flexibility)
    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    // Constructor with all fields
    public User(String userId, String fullName, String email, String description, String profileurl) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.description = description;
        this.profileurl = profileurl;
    }

    // Getters for Firebase to map data correctly
    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileurl() {
        return profileurl;
    }

    // You can optionally add setters if you plan to update the object after creation
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    @NonNull
    @Override
    public String toString() {
        Log.d("User:",getUserId()+" "+getFullName()+" "+getDescription()+" "+getEmail());
        return super.toString();
    }
}
