package com.example.project;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat_partner_profile extends AppCompatActivity {

    private Toolbar topAppBar;
    User user;
    TextView emailAddress;
    TextView statusMessage;
    ImageView profileurl;
    ListView listView;
    CustomMessageAdapter messageAdapter;
    List<UserMessage> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_partner_profile);
        String receiverId = getIntent().getStringExtra("receiverId");
        getUser(receiverId);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        getGroupsInCommon(userId,receiverId);
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
    private void getGroupsInCommon(String userId, String receiverId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        List<String> commonGroups = new ArrayList<>();

        Log.d("Chat_partner_profile", "Fetching common groups for userId: " + userId + " and receiverId: " + receiverId);

        firestore.collection("chats")
                .whereArrayContains("participants", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("Chat_partner_profile", "Query successful, documents found: " + queryDocumentSnapshots.size());

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        List<String> participants = (List<String>) document.get("participants");
                        if (participants != null && participants.contains(receiverId)) {
                            String groupName = document.getString("groupName");
                            commonGroups.add(groupName);
                            Log.d("Chat_partner_profile", "Common group found: " + groupName);
                        } else {
                            Log.d("Chat_partner_profile", "No common group in document: " + document.getId());
                        }
                    }

                    Log.d("Chat_partner_profile", "Total common groups found: " + commonGroups.size());
                    displayCommonGroups(commonGroups);
                })
                .addOnFailureListener(e -> Log.e("Chat_partner_profile", "Error fetching common groups", e));
    }

    private void displayCommonGroups(List<String> commonGroups) {
        LinearLayout groupOneLayout = findViewById(R.id.groupOneLayout);
        groupOneLayout.removeAllViews();

        for (String group : commonGroups) {
            TextView groupTextView = new TextView(this);
            groupTextView.setText(group);
            groupTextView.setTextSize(16);
            groupTextView.setTextColor(Color.BLACK);
            groupOneLayout.addView(groupTextView);

            Log.d("Chat_partner_profile", "Displaying group: " + group);
        }

        Log.d("Chat_partner_profile", "Displayed " + commonGroups.size() + " common groups.");
    }

}
