package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

public class NotificationSetting extends BaseSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Notification");
        actionbar.setDisplayHomeAsUpEnabled(true);
    }
}