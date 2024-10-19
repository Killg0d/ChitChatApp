package com.example.project;

public class User {
    private String fullName;
    private String email;

    // Empty constructor required for Firestore serialization
    public User() {}

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
