package com.example.project;

import android.widget.ImageView;

public class UserMessage {
    private String name;
    private String message;
    private String profilePictureURL;

    public UserMessage(String name, String message, String profilePictureURL) {
        this.name = name;
        this.message = message;
        this.profilePictureURL = profilePictureURL;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }
}
