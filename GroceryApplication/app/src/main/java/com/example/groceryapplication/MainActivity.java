package com.example.groceryapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.groceryapplication.activities.LoginActivity;
import com.example.groceryapplication.activities.RegistrationActivity;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize FirebaseApp
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.signup_button);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE); // Initially, hide the progress bar

        // Set click listeners for buttons
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); // Show progress bar on login button click
                goToLoginActivity();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); // Show progress bar on sign-up button click
                goToSignUpActivity();
            }
        });
    }

    // Method to navigate to the login activity
    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Method to navigate to the sign-up activity
    private void goToSignUpActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
