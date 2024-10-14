package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;


public class ChatsSetting extends BaseSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_setting);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Chats");
        actionbar.setDisplayHomeAsUpEnabled(true);
    }
}