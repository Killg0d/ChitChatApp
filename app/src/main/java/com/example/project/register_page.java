package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class register_page extends AppCompatActivity {

    TextView alreadyHaveAccount;
    EditText fullname, email, password, confirmPassword;
    Button register;

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private FirebaseFirestore db; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.registerButton);

        // Navigate to Login page
        alreadyHaveAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

        // Register button logic
        register.setOnClickListener(view -> {
            registerUser();
        });
    }

    // Method to register the user using Firebase
    public void registerUser() {
        String fullnamee = fullname.getText().toString().trim();
        String emaill = email.getText().toString().trim();
        String passwordd = password.getText().toString().trim();
        String confirmPasswordd = confirmPassword.getText().toString().trim();

        // Validate user inputs
        if (fullnamee.isEmpty()) {
            fullname.setError("Enter Full Name");
            return;
        }

        if (emaill.isEmpty()) {
            email.setError("Enter Email Address");
            return;
        }

        if (passwordd.isEmpty()) {
            password.setError("Enter Password");
            return;
        }

        if (confirmPasswordd.isEmpty()) {
            confirmPassword.setError("Confirm Password");
            return;
        }

        if (passwordd.length() < 6) {
            password.setError("Password must be at least 6 characters");
            return;
        }

        if (!passwordd.equals(confirmPasswordd)) {
            confirmPassword.setError("Passwords do not match");
            return;
        }

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(emaill, passwordd)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration successful, update user profile
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullnamee)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(tassk -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(register_page.this, "ProfileSetting updated with display name", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        // Store user data in Firestore
                        storeUserDataInFirestore(user.getUid(), fullnamee, emaill);

                        Toast.makeText(register_page.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        // Navigate to HomeActivity after successful registration
                        startActivity(new Intent(register_page.this, Login.class));
                        finish();
                    } else {
                        // Registration failed, check the reason
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(register_page.this, "Email already in use", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(register_page.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to store user data in Firestore
    private void storeUserDataInFirestore(String userId, String fullname, String email) {
        // Create a new user with name and email
        User user = new User(userId,fullname, email,"Hi! Using Chitchat",null);
        // Add a new document with a generated ID
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(register_page.this, "User data stored in Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(register_page.this, "Error storing user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
