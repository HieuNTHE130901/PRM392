package com.example.groceryapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.groceryapplication.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);

        // Find the TextView in the layout
        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);

        // Set the totalAmount to the TextView
        totalAmountTextView.setText("Total Amount: " + totalAmount);

    }
}