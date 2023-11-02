package com.example.groceryapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        double discount=5, shipping = 30000, finalPrice = ;

        double subtotal = getIntent().getDoubleExtra("totalAmount", 0.0);
        // Find the TextView in the layout
        TextView txtSubtotal = findViewById(R.id.payment_subtotal);

        String subtotalSting = String.valueOf(subtotal);

        // Find the TextView in the layout
        TextView txtname = findViewById(R.id.payment_userName);
        TextView txtaddress = findViewById(R.id.payment_address);
        TextView txtDiscount = findViewById(R.id.payment_discount);
        TextView txtShipping = findViewById(R.id.payment_shipping);
        TextView txtFinalPrice = findViewById(R.id.payment_finalPrice);

        // Set values to the TextView
        txtSubtotal.setText(subtotalSting);



        Button buy = findViewById(R.id.pay_btn);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePaymentToFirestore(finalPrice);
            }
        });


    }
    private void savePaymentToFirestore(double finalPrice) {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("finalPrice", finalPrice);
        paymentMap.put("date", saveCurrentDate);
        paymentMap.put("time", saveCurrentTime);

        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Payments")
                .add(paymentMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PaymentActivity.this, "Payment information saved", Toast.LENGTH_SHORT).show();
                            // Add any further actions you want to take upon success.
                            // For example, you can navigate to a different activity or perform other actions.
                        } else {
                            Toast.makeText(PaymentActivity.this, "Failed to save payment information", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}