package com.example.project;

public class MessageList {
    private String name;
    private String message;
    private int profilePictureResId;



    public MessageList(String name, String message, int profilePictureResId) {
        this.name = name;
        this.message = message;
        this.profilePictureResId = profilePictureResId;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public int getProfilePictureResId() {
        return profilePictureResId;
    }


    // Getter for lastMessage



}