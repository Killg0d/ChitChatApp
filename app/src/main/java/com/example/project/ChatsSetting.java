package com.example.project;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;


public class ChatsSetting extends BaseSettingActivity {
    RadioGroup theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_setting);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle("Chats");
        actionbar.setDisplayHomeAsUpEnabled(true);
        theme = findViewById(R.id.theme);
        theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton b = findViewById(i);
                if(b.isChecked())
                {
                    Toast.makeText(ChatsSetting.this, "b is checked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}