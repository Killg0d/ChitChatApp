package com.example.project;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;


public class Chats extends BaseActivity {
    RadioGroup theme,font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_setting);
        float fontSize = FontSizeManager.getFontSize(this);

        // Set your activity's theme or layout here


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

        if (fontSize == 18f) {
            largeRadioButton.setChecked(true);
        } else if (fontSize == 16f) {
            mediumRadioButton.setChecked(true);
        } else {
            smallRadioButton.setChecked(true);
        }

        theme = findViewById(R.id.theme);
        theme.setOnCheckedChangeListener((radioGroup, i) -> {

        });
        font = findViewById(R.id.font);
        font.setOnCheckedChangeListener((fontGroup,checkedId)->{
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
    }
}