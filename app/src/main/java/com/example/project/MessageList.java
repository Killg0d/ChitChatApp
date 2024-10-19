package com.example.project;

public class MessageList {
    private String name;
    private String message;
    private int profilePictureResId;
    private String chatName;
    private String lastMessage;


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
    public String getChatName() {
        return chatName;
    }

    // Getter for lastMessage
    public String getLastMessage() {
        return lastMessage;
    }



}