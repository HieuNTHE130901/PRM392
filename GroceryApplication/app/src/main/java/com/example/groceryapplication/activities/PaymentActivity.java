package com.example.groceryapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.groceryapplication.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        // Find the TextView in the layout
        TextView totalAmountTextView = findViewById(R.id.payment_sub_total);

        // Set the totalAmount to the TextView
        totalAmountTextView.setText("Total Amount: " + totalAmount);

        String totalAmountString = String.valueOf(totalAmount);
        // Find the TextView in the layout
        TextView subtotal = findViewById(R.id.payment_sub_total);
        TextView discount = findViewById(R.id.payment_discount);
        TextView shipping = findViewById(R.id.payment_shipping);
        TextView finalPrice = findViewById(R.id.payment_finalPrice);
        //TextView addressTextView = findViewById(R.id.pay);

        // Set the totalAmount to the TextView
        subtotal.setText(totalAmountString);

        Button buy = findViewById(R.id.pay_btn);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}