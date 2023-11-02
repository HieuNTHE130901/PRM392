package com.example.groceryapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapplication.R;
import com.example.groceryapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private TextView signinLink;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        // Initialize views
        // Initialize the ProgressBar
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signupButton = findViewById(R.id.signup_button);
        signinLink = findViewById(R.id.signin_link);

        // Set click listeners
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle registration using Firebase Authentication
                registerUser();
            }
        });

        signinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event to navigate to the Sign In activity
                goToSignInActivity();
            }
        });
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE); // Show the progress bar

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success
                            Toast.makeText(RegistrationActivity.this, "Registration success.", Toast.LENGTH_SHORT).show();
                            // You can perform additional actions here (e.g., navigate to the home screen)
                            User model = new User(name, email, password);
                            String id = task.getResult().getUser().getUid();
                            firebaseDatabase.getReference().child("Users").child(id).setValue(model);

                            goToSignInActivity();
                        } else {
                            // Registration failed
                            Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE); // Hide the progress bar after task completion
                    }
                });
    }
    private void goToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}