package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChatsSettings extends BaseActivity {

    private static final String TAG = "ChatsSettings";
    RadioGroup theme, font;
    LinearLayout local_copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_setting);
        float fontSize = FontSizeManager.getFontSize(this);

        // Find the root view and apply the font size
        ViewGroup rootView = findViewById(android.R.id.content);
        applyFontSizeToViews(rootView, fontSize);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Chats");
        actionbar.setDisplayHomeAsUpEnabled(true);

        RadioButton smallRadioButton = findViewById(R.id.small);
        RadioButton mediumRadioButton = findViewById(R.id.medium);
        RadioButton largeRadioButton = findViewById(R.id.large);
        // Initialize theme radio group and check current mode
        theme = findViewById(R.id.theme);
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            theme.check(R.id.dark); // Set dark mode as selected if it’s currently active
        } else {
            theme.check(R.id.light); // Set light mode as selected otherwise
        }

// Set listener for dark mode switch
        theme.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.light) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (i == R.id.dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            // Recreate activity to apply the mode change
            recreate();

            // Redirect to ClassList activity after theme change
            Intent intent = new Intent(ChatsSettings.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        // Set the selected font size based on saved preferences
        if (fontSize == 18f) {
            largeRadioButton.setChecked(true);
        } else if (fontSize == 16f) {
            mediumRadioButton.setChecked(true);
        } else {
            smallRadioButton.setChecked(true);
        }


        font = findViewById(R.id.font);
        font.setOnCheckedChangeListener((fontGroup, checkedId) -> {
            float rfontSize = 16f; // Default to medium

            if (checkedId == R.id.small) {
                rfontSize = 12f;
            } else if (checkedId == R.id.medium) {
                rfontSize = 16f;
            } else if (checkedId == R.id.large) {
                rfontSize = 18f;
            }

            // Save the selected font size in SharedPreferences
            FontSizeManager.saveFontSize(getApplicationContext(), rfontSize);
            applyFontSizeToViews(rootView, rfontSize);
        });

        local_copy = findViewById(R.id.localcopy);
        local_copy.setOnClickListener(view -> {
            // Show a Toast indicating that the download process has started
            Toast.makeText(this, "Downloading chats locally...", Toast.LENGTH_SHORT).show();

            // Proceed directly with downloading chats, as no permissions are required for internal storage
            downloadChatsLocally();
        });
    }

    private void downloadChatsLocally() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("chats")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder chatData = new StringBuilder();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            chatData.append(document.getString("message")).append("\n");
                        }
                        saveChatsToLocalFile(chatData.toString());
                    } else {
                        Log.e(TAG, "Error getting chats: ", task.getException());
                    }
                });
    }

    private void saveChatsToLocalFile(String chatData) {
        // Use app-specific internal storage
        File file = new File(getFilesDir(), "chats_backup.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(chatData.getBytes());
            // Show a success Toast when chats are saved
            Toast.makeText(this, "Chats saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error writing file", e);
            Toast.makeText(this, "Error saving chats: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}