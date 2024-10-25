package com.example.project;

import android.util.Log;
import androidx.annotation.NonNull;

public class User {
    private String userId;
    private String fullName;
    private String email;
    private String description;
    private String profileurl;
    private boolean isSelected; // New field for selection tracking

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
        this.isSelected = false; // Default to false
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

    public boolean isSelected() {
        return isSelected;
    }

    // Setters for Firebase or updating selection status
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

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        Log.d("User:", getUserId() + " " + getFullName() + " " + getDescription() + " " + getEmail()+" "+getProfileurl());
        return super.toString();
    }
}
