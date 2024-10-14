package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project.R;

public class Chat_partner_profile extends AppCompatActivity {

    private Toolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_partner_profile);

        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        getSupportActionBar().setTitle("Full Name");

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView emailAddress = findViewById(R.id.emailAddress);
        emailAddress.setText("Emai Address");

        TextView statusMessage = findViewById(R.id.statusMessage);
        statusMessage.setText("Hey! I am using ChitChat.");

        TextView groupOneText = findViewById(R.id.groupOneText);
        groupOneText.setText("Friends");

        TextView groupTwoText = findViewById(R.id.groupTwoText);
        groupTwoText.setText("Family");
    }
}
