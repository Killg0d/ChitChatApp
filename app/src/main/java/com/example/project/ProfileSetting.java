package com.example.project;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;

import java.util.ArrayList;

public class ProfileSetting extends BaseSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        getIntent();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ArrayList<MessageList> mitem = new ArrayList<>();
        mitem.add(new MessageList("Name","Full Name",R.drawable.person));
        mitem.add(new MessageList("About","Hi! Using ChitChat",R.drawable.baseline_info_outline_24));
        ListView l = findViewById(R.id.list1);
        MessageAdapter item = new MessageAdapter(this,mitem,1);
        l.setAdapter(item);

    }
}