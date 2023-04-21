package com.dsstudio.farmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TreatmentsActivity extends AppCompatActivity {
    Button btnGoHome;
    TextView txtControlProcedure;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://plant-diseases-classifier-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatments);

        btnGoHome = findViewById(R.id.btnHome);
        txtControlProcedure = findViewById(R.id.treatmentDetails);

        SessionManager sessionManager = new SessionManager(TreatmentsActivity.this);
        String diseaseName = sessionManager.getDiseaseName();

        databaseReference.child("Treatments").child(diseaseName).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                String controlProcedure = snapshot.getValue(String.class);
                txtControlProcedure.setText(controlProcedure);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TreatmentsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}