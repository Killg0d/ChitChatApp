package com.example.project;

public class MessageList {
    private String name;
    private String message;
    private int profilePictureResId;
    String chatId;
    String recieverid;

    public MessageList(String name, String message, int profilePictureResId) {
        this.name = name;
        this.message = message;
        this.profilePictureResId = profilePictureResId;
    }

    public MessageList(String name, String message, int profilePictureResId, String chatId, String recieverid) {
        this.name = name;
        this.message = message;
        this.profilePictureResId = profilePictureResId;
        this.chatId =chatId;
        this.recieverid= recieverid;
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
    public String getChatId() {
        return chatId;
    }
    public String getReceiverId() {
        return recieverid;
    }

    // Getter for lastMessage



}