package com.example.project;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectContactActivity extends AppCompatActivity {

    private ListView contactsListView;
    private SearchView contactSearchView;
    private FirebaseFirestore db;
    private List<User> userList, filteredList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        // Initialize Firestore and UI components
        db = FirebaseFirestore.getInstance();
        contactsListView = findViewById(R.id.contactsListView);
        contactSearchView = findViewById(R.id.contactSearchView);

        // Initialize the user list and filtered list
        userList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize the adapter for the filtered list
        userAdapter = new UserAdapter(this, filteredList);
        contactsListView.setAdapter(userAdapter);

        // Fetch users from Firestore
        fetchUsersFromFirestore();

        // Set up the search functionality
        contactSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filter the list based on query
                filterUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list dynamically as the user types
                filterUsers(newText);
                return true;
            }
        });
    }

    private void fetchUsersFromFirestore() {
        db.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            userList.clear();
                            userList.addAll(querySnapshot.toObjects(User.class));  // Add all users to the list
                            filteredList.addAll(userList);  // Initially, the filtered list contains all users
                            userAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(SelectContactActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to filter users by name or email
    private void filterUsers(String query) {
        filteredList.clear();  // Clear the filtered list

        if (query.isEmpty()) {
            filteredList.addAll(userList);  // If query is empty, show all users
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (User user : userList) {
                if (user.getFullName().toLowerCase().contains(lowerCaseQuery) ||
                        user.getEmail().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(user);  // Add matching users to the filtered list
                }
            }
        }

        userAdapter.notifyDataSetChanged();  // Notify the adapter to refresh the ListView
    }
}
