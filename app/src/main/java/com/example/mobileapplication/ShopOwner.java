package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShopOwner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_owner);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_shop);
        bottomNavigationView.setSelectedItemId(R.id.action_settings);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return;

                    case R.id.action_user:
                        startActivity(new Intent(getApplicationContext(), UserProfileEdit.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return;

                    case R.id.action_settings:

                }
            }
        });
    }
}