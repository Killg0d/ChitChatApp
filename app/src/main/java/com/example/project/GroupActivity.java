package com.example.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private EditText editTextGroupName;
    private RecyclerView recyclerViewMembers;
    private MemberAdapter memberAdapter;
    private List<User> memberList;
    private FirebaseFirestore firestore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // Initialize views
        editTextGroupName = findViewById(R.id.editTextGroupName);
        recyclerViewMembers = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Initialize member list
        memberList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance(); // Initialize Firebase Firestore

        // Set up RecyclerView
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new MemberAdapter(memberList);
        recyclerViewMembers.setAdapter(memberAdapter);

        // Fetch users from Firebase Firestore and populate the RecyclerView
        fetchUsersFromFirebase();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Handle Floating Action Button click
        fab.setOnClickListener(view -> {
            String groupName = editTextGroupName.getText().toString();
            List<User> selectedMembers = memberAdapter.getSelectedMembers();

            if (groupName.isEmpty()) {
                new AlertDialog.Builder(GroupActivity.this)
                        .setTitle("Error")
                        .setMessage("Group name cannot be empty.")
                        .setPositiveButton("OK", null)
                        .show();
            } else if (selectedMembers.isEmpty()) {
                new AlertDialog.Builder(GroupActivity.this)
                        .setTitle("Error")
                        .setMessage("Please select at least one member.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                new AlertDialog.Builder(GroupActivity.this)
                        .setTitle("Create Group")
                        .setMessage("Group Name: " + groupName + "\nSelected Members: " + selectedMembers.size())
                        .setPositiveButton("OK", (dialog, which) -> {

                            // Create a new chat document in Firestore
                            // Generate a unique chatId
                            ChatManager m = new ChatManager();
                            String chatId = m.createChat(true, getUserId(selectedMembers), null, groupName);


                            // Pass chatId and groupName to GroupChatActivity
                            Intent intent = new Intent(GroupActivity.this, GroupChatActivity.class);
                            intent.putExtra("GROUP_NAME", groupName);
                            intent.putExtra("chatId", chatId);// Pass the chatId
                            startActivity(intent);

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

    }

    // Fetch users from Firebase Firestore
    private void fetchUsersFromFirebase() {
        CollectionReference usersRef = firestore.collection("users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Map Firestore document to User object
                        User user = document.toObject(User.class);
                        if (user != null) {
                            memberList.add(user); // Add User to member list
                            Log.d("User:", user.toString()); // Log user details for debugging
                        }
                    }
                    memberAdapter.notifyDataSetChanged(); // Refresh the adapter after data is fetched
                }
            } else {
                Log.d("FirebaseError", "Error fetching users: ", task.getException());
            }
        });
    }

    // Helper method to get list of user IDs from selected members
    private List<String> getUserId(List<User> selectedMembers) {
        List<String> userIds = new ArrayList<>();
        for (User user : selectedMembers) {
            userIds.add(user.getUserId()); // Assuming User class has getUserId() method
        }
        return userIds;
    }
}
