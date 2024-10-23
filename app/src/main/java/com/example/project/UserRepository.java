package com.example.project;

import com.google.firebase.firestore.FirebaseFirestore;


public class UserRepository {

    // Get Firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Method to retrieve user by userId
    public void getUserById(String userId, final FirestoreCallback callback) {
        // Access the "users" collection and document with the given userId
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    // Convert the document to a User object
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onCallback(user); // Pass the User object to the callback
                    } else {
                        callback.onFailure("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }

    // Callback interface to handle async results
    public interface FirestoreCallback {
        void onCallback(User user);
        void onFailure(String errorMessage);
    }
}

