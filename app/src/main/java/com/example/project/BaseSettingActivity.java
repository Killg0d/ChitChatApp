package com.example.project;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class BaseSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void applyFontSizeToViews(View root, float fontSize) {
        if (root instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) root;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyFontSizeToViews(viewGroup.getChildAt(i), fontSize);
            }
        } else if (root instanceof TextView) {
            // Check if the TextView has the "exclude_font_change" tag
            Object tag = root.getTag();
            if (tag == null || !tag.equals("exclude_font_change")) {
                ((TextView) root).setTextSize(fontSize);
            }
        }
    }

}