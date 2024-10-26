package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;

public class NotificationSettings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Notification");
        actionbar.setDisplayHomeAsUpEnabled(true);

        ToggleButton toggleButton = findViewById(R.id.notification); // Ensure this ID matches your XML
        ToggleButton toggleButton1 = findViewById(R.id.notification2); // Ensure this ID matches your XML)
        // Retrieve saved state from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isNotificationEnabled = prefs.getBoolean("isNotification", false);
        toggleButton.setChecked(isNotificationEnabled);

        // Set listener to save state whenever the toggle is changed
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isNotification", isChecked);
            editor.apply();
            if (isChecked) {
                startNotificationService();
            } else {
                stopNotificationService();
            }
        });
        toggleButton1.setChecked(isNotificationEnabled);

        // Set listener to save state whenever the toggle is changed
        toggleButton1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isNotification", isChecked);
            editor.apply();
            if (isChecked) {
                startNotificationService();
            } else {
                stopNotificationService();
            }
        });
        if(isNotificationEnabled)
        {
            startNotificationService();
        }
        else
        {
            stopNotificationService();
        }

    }
}