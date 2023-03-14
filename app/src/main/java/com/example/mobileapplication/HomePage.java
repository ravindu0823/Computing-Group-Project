package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_home);
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:

                    case R.id.action_user:
                        startActivity(new Intent(getApplicationContext(), UserProfileEdit.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return;

                    case R.id.action_settings:
                        startActivity(new Intent(getApplicationContext(), ShopOwner.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return;
                }
            }
        });

        /*button = findViewById(R.id.btnUserProfile);
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
        });*/
    }

    /*public void openProfilePage() {
        Intent intent = new Intent(this, UserProfileEdit.class);
        startActivity(intent);
    }

    public void openViewUserProfilePage() {
        Intent intent = new Intent(this, ViewUserProfile.class);
        startActivity(intent);
    }*/


}