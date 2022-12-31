package com.example.mobileapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {
    private Button button;
    private Button button1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        button = findViewById(R.id.btnUserProfile);
        button1 = findViewById(R.id.btnViewUserProfile);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewUserProfilePage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfilePage();
            }
        });
    }

    public void openProfilePage() {
        Intent intent = new Intent(this, UserProfileEdit.class);
        startActivity(intent);
    }

    public void openViewUserProfilePage() {
        Intent intent = new Intent(this, ViewUserProfile.class);
        startActivity(intent);
    }


}