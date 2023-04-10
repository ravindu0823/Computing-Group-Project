package com.dsstudio.farmy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://plant-diseases-classifier-default-rtdb.firebaseio.com/");
    TextInputEditText txtEmail, txtPassword, txtUsername, txtPhone, txtConfirmPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView txtLogin;

    @Override
    public void onStart() {
        super.onStart();

        SessionManager sessionManager = new SessionManager(Register.this);

        if (sessionManager.getUsername() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        txtUsername = findViewById(R.id.username);
        txtEmail = findViewById(R.id.regEmail);
        txtPassword = findViewById(R.id.regPassword);
        txtPhone = findViewById(R.id.phone);
        txtConfirmPassword = findViewById(R.id.regConfirmPassword);

        progressBar = findViewById(R.id.progressBar);
        txtLogin = findViewById(R.id.loginNow);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // progressBar.setVisibility(View.VISIBLE);

                String username = txtUsername.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String phone = txtPhone.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || confirmPassword.isEmpty()) {
                    txtUsername.setError("Please enter your username");
                    txtEmail.setError("Please enter your email");
                    txtPassword.setError("Please enter your password");
                    txtPhone.setError("Please enter your phone number");
                    txtConfirmPassword.setError("Please enter your password");

                } else {
                    if (password.equals(confirmPassword)) {
                        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(username)) {
                                    txtUsername.setError("Username already exists");
                                } else {
                                    // register user
                                    databaseReference.child("Users").child(username).child("email").setValue(email);
                                    databaseReference.child("Users").child(username).child("phone").setValue(phone);
                                    databaseReference.child("Users").child(username).child("password").setValue(password);

                                    User user = new User(username, email, phone, password);
                                    SessionManager sessionManager = new SessionManager(Register.this);
                                    sessionManager.saveSession(user);

                                    // Make a Toast
                                    Toast.makeText(Register.this, "User Register successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        txtConfirmPassword.setError("Password does not match");
                    }
                }

                /*mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });*/

            }
        });

    }
}