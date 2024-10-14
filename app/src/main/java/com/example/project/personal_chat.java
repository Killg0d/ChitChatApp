package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class personal_chat extends AppCompatActivity {
    LinearLayout clicktoname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        // Set up the custom toolbar
        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        clicktoname=findViewById(R.id.clicktoname);
        clicktoname.setOnClickListener(v->{
            Intent intent=new Intent(this,Chat_partner_profile.class);
            startActivity(intent);
        });
        // Handle the back button click
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish activity or implement custom back functionality
                onBackPressed();
                finish();
            }
        });
    }
}