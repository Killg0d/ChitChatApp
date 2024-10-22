package com.example.project;

public class User {
    private String userId;
    private String fullName;
    private String email;
    private String description;
    private String profileurl;

    // Empty constructor required for Firestore serialization
    public User() {}

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;

    }
    public User(String userId, String fullName, String email,String description,String profileurl) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.description=description;
        this.profileurl=profileurl;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public  String getDescription(){
        return description;
    }
    public String getProfileurl(){return profileurl;}

    public String getUserId() {
        return userId;
    }
}
