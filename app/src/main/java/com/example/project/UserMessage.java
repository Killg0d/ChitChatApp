package com.example.project;

import android.widget.ImageView;

public class UserMessage {
    private String name;
    private String message;
    private String profilePictureURL;
    String chatId;
    String recieverid;
    boolean isGroup;

    public UserMessage(String name, String message, String profilePictureURL) {
        this.name = name;
        this.message = message;
        this.profilePictureURL = profilePictureURL;
    }
    public UserMessage(String name, String message, String profilePictureResId, String chatId, String recieverid) {
        this.name = name;
        this.message = message;
        this.profilePictureURL = profilePictureResId;
        this.chatId =chatId;
        this.recieverid= recieverid;
    }
    public UserMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }
    public UserMessage(String name, String message, String profilePictureURL, String chatId, String recieverid,boolean isGroup) {
        this.name = name;
        this.message = message;
        this.profilePictureURL = profilePictureURL;
        this.chatId =chatId;
        this.recieverid= recieverid;
        this.isGroup=isGroup;
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
    public String getChatId() {
        return chatId;
    }
    public String getReceiverId() {
        return recieverid;
    }
    public boolean isGroup() {
        return isGroup;
    }
}
