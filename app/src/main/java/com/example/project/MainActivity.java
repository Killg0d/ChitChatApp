package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends BaseActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isNotificationEnabled = prefs.getBoolean("isNotification", false);

        // Only start the service if notifications are enabled
        if (isNotificationEnabled) {
            startNotificationService();
        }
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                startActivity(loginIntent);
                finish(); // Close this activity
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}