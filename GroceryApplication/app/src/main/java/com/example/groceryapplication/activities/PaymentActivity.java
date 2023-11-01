package com.example.groceryapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.groceryapplication.R;

import java.security.MessageDigest;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);

        String totalAmountString = String.valueOf(totalAmount);

        // Find the TextView in the layout
        TextView totalAmountTextView = findViewById(R.id.total_amt);

        // Set the totalAmount to the TextView
        totalAmountTextView.setText(totalAmountString);

        Button buy = findViewById(R.id.pay_btn);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });



    }
}