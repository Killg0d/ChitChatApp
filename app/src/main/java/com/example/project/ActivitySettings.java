package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ActivitySettings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionbar = getSupportActionBar();


        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Setting");
        ImageView profileImg = new ImageView(ActivitySettings.this);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedImageUrl = sharedPreferences.getString("profileImageUrl", null);

// Initialize the ListView
        ListView listView = findViewById(R.id.list_view);
        ListView optionView = findViewById(R.id.option_view);

// Create a list of UserMessage objects
        ArrayList<UserMessage> messageItems = new ArrayList<>();

// Add UserMessage objects to the list
        messageItems.add(new UserMessage("Alice", "Hello!", savedImageUrl)); // Example

// Create an adapter and set it to the ListView
        CustomMessageAdapter adapter = new CustomMessageAdapter(this, messageItems);
        listView.setAdapter(adapter);

// Set an item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserMessage item = (UserMessage) listView.getItemAtPosition(i);
                if (item != null) { // Updated null check
                    startActivity(new Intent(ActivitySettings.this, ProfileSetting.class));
                }
            }
        });

        ArrayList<SettingOption> options = new ArrayList<>();
        options.add(new SettingOption("Accounts", R.mipmap.ic_key));
        options.add(new SettingOption("Notification", R.mipmap.ic_bell));
        options.add(new SettingOption("Chats", R.mipmap.ic_chat));
        SettingOptionAdapter adapter1 = new SettingOptionAdapter(this, options);
        optionView.setAdapter(adapter1);
        optionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                  SettingOption item = (SettingOption) optionView.getItemAtPosition(i);
                                                  Toast.makeText(ActivitySettings.this, item.getName(), Toast.LENGTH_SHORT).show();
                                                  if (item.getName().equals("Accounts")) {
                                                      startActivity(new Intent(ActivitySettings.this, AccountSettings.class));
                                                  }
                                                  if (item.getName().equals("Notification")) {
                                                      startActivity(new Intent(ActivitySettings.this, NotificationSettings.class));
                                                  }
                                                  if (item.getName().equals("Chats")) {
                                                      startActivity(new Intent(ActivitySettings.this, ChatsSettings.class));
                                                  }
                                              }
                                          }
        );

    }


}