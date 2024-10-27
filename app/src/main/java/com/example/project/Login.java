package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView register;

    private FirebaseAuth mAuth; // Firebase Authentication instance
    SharedPreferences p ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        p= getSharedPreferences("Login",MODE_PRIVATE);
        if(p.getBoolean("isLogin?",false))
        {
            Intent intent = new Intent(Login.this, MainChat.class);
            startActivity(intent);
        }
        TextView t = findViewById(R.id.forgot_password);
        t.setOnClickListener(v->
        {
            startActivity(new Intent(Login.this,ForgotPassword.class));
        });
        //
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerLink);

        // Set click listener for the Login button
        login.setOnClickListener(view -> {
            loginUser();
        });

        // Set click listener for the register link
        register.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, register_page.class);
            startActivity(intent);
        });
    }

    // Method to handle user Login with Firebase
    private void loginUser() {
        String emaill = email.getText().toString().trim();
        String passwordd = password.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(emaill)) {
            email.setError("Enter Email Address");
            return;
        }

        if (TextUtils.isEmpty(passwordd)) {
            password.setError("Enter Password");
            return;
        }

        // Firebase Authentication logic
        mAuth.signInWithEmailAndPassword(emaill, passwordd)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        p.edit().putBoolean("isLogin?",true).commit();
                        // Navigate to main chat screen
                        Intent intent = new Intent(Login.this, MainChat.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}
