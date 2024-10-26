package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Chat_partner_profile extends AppCompatActivity {

    private Toolbar topAppBar;
    User user;
    TextView emailAddress;
    TextView statusMessage;
    ImageView profileurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_partner_profile);

       getUser(getIntent().getStringExtra("receiverId"));
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        getSupportActionBar().setTitle("Full Name");

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         emailAddress = findViewById(R.id.emailAddress);
        emailAddress.setText("Emai Address");
        profileurl= findViewById(R.id.profilePicture);
        statusMessage = findViewById(R.id.statusMessage);
        statusMessage.setText("Hey! I am using ChitChat.");

        TextView groupOneText = findViewById(R.id.groupOneText);
        groupOneText.setText("Friends");

        TextView groupTwoText = findViewById(R.id.groupTwoText);
        groupTwoText.setText("Family");
    }

    private void getUser(String receiverId) {
        FirebaseFirestore.getInstance().collection("users").document(receiverId).
                get()
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                user= documentSnapshot.toObject(User.class);
                                getSupportActionBar().setTitle(documentSnapshot.getString("fullName"));
                                Glide.with(getApplicationContext())
                                        .load(documentSnapshot.getString("profileurl"))
                                        .placeholder(R.drawable.download)  // Show a default placeholder while loading
                                        .error(R.drawable.person)          // Show default image in case of error
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)  // Cache strategy to avoid flickering
                                        .into(profileurl);
                                emailAddress.setText(documentSnapshot.getString("email"));
                                statusMessage.setText(documentSnapshot.getString("description"));
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Error",e.toString());
                            }
                        }
                );
    }
}
