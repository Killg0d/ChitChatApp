package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;


import java.util.ArrayList;

public class SettingActivity extends BaseSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionbar = getSupportActionBar();


        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Setting");
        ListView listView = findViewById(R.id.list_view);
        ListView optionView =findViewById(R.id.option_view);
        // Create a list of MessageItem objects
        ArrayList<MessageList> messageItems = new ArrayList<>();
        messageItems.add(new MessageList("Alice", "Hello!", R.drawable.download)); // Create an adapter and set it to the ListView
        MessageAdapter adapter = new MessageAdapter(this, messageItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageList item = (MessageList) listView.getItemAtPosition(i);
                if(!item.equals(null))
                {
                    startActivity(new Intent(SettingActivity.this,ProfileSetting.class));
                }
            }
        });
        ArrayList<SettingOption> options = new ArrayList<>();
        options.add(new SettingOption("Accounts",R.mipmap.ic_key));
        options.add(new SettingOption("Notification",R.mipmap.ic_bell));
        options.add(new SettingOption("Chats",R.mipmap.ic_chat));
        SettingOptionAdapter adapter1 = new SettingOptionAdapter(this,options);
        optionView.setAdapter(adapter1);
        optionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SettingOption item =(SettingOption) optionView.getItemAtPosition(i);
                Toast.makeText(SettingActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                if(item.getName().equals("Accounts"))
                {
                    startActivity(new Intent(SettingActivity.this,AccountSetting.class));
                }
                if(item.getName().equals("Notification"))
                {
                    startActivity(new Intent(SettingActivity.this,NotificationSetting.class));
                }
                if(item.getName().equals("Chats"))
                {
                    startActivity(new Intent(SettingActivity.this,ChatsSetting.class));
                }
            }
        }
        );
    }



}