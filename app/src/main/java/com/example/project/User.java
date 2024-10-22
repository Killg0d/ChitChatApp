package com.example.project;

public class User {
    private String fullName;
    private String email;
    private String description;

    // Empty constructor required for Firestore serialization
    public User() {}

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }
    public User(String fullName, String email,String description) {
        this.fullName = fullName;
        this.email = email;
        this.description=description;
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
}
