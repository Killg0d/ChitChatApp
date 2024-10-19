package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainChat extends AppCompatActivity {
LinearLayout chat1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainchat);
        chat1=findViewById(R.id.main);
        chat1.setOnClickListener(v->{
            Intent intent=new Intent(this,personal_chat.class);
            startActivity(intent);
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.newgrp)
        {
            startActivity(new Intent(MainChat.this,GroupActivity.class));
            return true;
        }
        else if(id==R.id.settings)
        {
            Intent i = new Intent(MainChat.this,SettingActivity.class);
            startActivity(i);
            return true;
        } else if (id==R.id.signout) {
            SharedPreferences p =getSharedPreferences("Login",MODE_PRIVATE);
            p.edit().putBoolean("isLogin?",false).apply();
            startActivity(new Intent(MainChat.this, Login.class));
        }
        return super.onOptionsItemSelected(item);
    }
}