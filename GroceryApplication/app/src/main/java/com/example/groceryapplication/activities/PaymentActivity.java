package com.example.groceryapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapplication.R;
import com.example.groceryapplication.models.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextView txtName;
    TextView txtSubtotal;
    TextView txtDiscount;
    TextView paymentAddress;
    TextView txtShipping;
    TextView txtFinalPrice;
    EditText paymentAddressNew;
    EditText edtVoucher;
    CheckBox addressCheckbox;
    Button payButton;
    Button applyVoucher;
    List<Voucher> voucherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize Firebase components
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI components
        txtName = findViewById(R.id.payment_userName);
        txtSubtotal = findViewById(R.id.payment_subtotal);
        txtDiscount = findViewById(R.id.payment_discount);
        paymentAddress = findViewById(R.id.payment_address);
        txtShipping = findViewById(R.id.payment_shipping);
        txtFinalPrice = findViewById(R.id.payment_finalPrice);
        paymentAddressNew = findViewById(R.id.payment_address_new);
        edtVoucher = findViewById(R.id.payment_edt_voucher);
        addressCheckbox = findViewById(R.id.address_checkbox);
        payButton = findViewById(R.id.payment_pay_btn);
        applyVoucher = findViewById(R.id.payment_bnt_voucher);
        voucherList = new ArrayList<>();
        //Handle check button
        addressCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Use existing address
                    paymentAddressNew.setVisibility(View.GONE);
                    paymentAddress.setVisibility(View.VISIBLE);
                } else {
                    // Use new address
                    paymentAddress.setVisibility(View.GONE);
                    paymentAddressNew.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set values to the TextViews

        // Get "user name" and "address" from FirestoreDB
        String userId = auth.getCurrentUser().getUid();
        // Reference to the user's "information" document in the "information" subcollection
        DocumentReference informationRef = firestore.collection("users")
                .document(userId)
                .collection("information")
                .document("information");
        // Retrieve the user's "information" document
        informationRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the name field from the document
                    String userName = document.getString("name");
                    String address = document.getString("address");
                    if (userName != null) {
                        txtName.setText("Your name: " + userName);
                        TextView txtAddress = findViewById(R.id.payment_address);
                        txtAddress.setText(address);
                    }
                }
            }
        });


        // Set value to "Subtotal" get data from Cart
        double subtotal = getIntent().getDoubleExtra("totalAmount", 0.0);
        String subtotalString = String.format("%.2f", subtotal);
        txtSubtotal.setText(subtotalString);

        //Set value to "shipping fee" load from FirestoreDB
        firestore.collection("system_setting").document("fee").get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Get the shipping fee
                                Long shippingFee = document.getLong("shipping");
                                if (shippingFee != null) {
                                    double shipping = shippingFee.doubleValue();
                                    txtShipping.setText(String.valueOf(shipping));
                                    // Call the method to calculate and update the final price
                                    updateFinalPrice();
                                }
                            }
                        }
                    }
                });


        // Load voucher from FirestoreDB
        firestore.collection("system_setting").document("voucher")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Get the voucher data as a Map
                                Map<String, Object> voucherData = document.getData();
                                if (voucherData != null) {
                                    // Iterate through the voucher data map and add vouchers to the list
                                    for (Map.Entry<String, Object> entry : voucherData.entrySet()) {
                                        String name = entry.getKey();
                                        int discount = Integer.parseInt(entry.getValue().toString());
                                        Voucher voucher = new Voucher(name, discount);
                                        voucherList.add(voucher);
                                    }
                                }
                            }
                        }
                    }
                });

        //handle button "apply voucher"
        applyVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedVoucher = edtVoucher.getText().toString();
                int discount = 0;

                // Search for the selected voucher in the voucher list
                for (Voucher voucher : voucherList) {
                    if (voucher.getName().equals(selectedVoucher)) {
                        discount = voucher.getDiscount();
                        break; // Stop searching once the voucher is found
                    }
                }
                updateFinalPrice();
            }
        });

        // Handle pay button
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the selected address (existing or new)
                String selectedAddress;
                if (addressCheckbox.isChecked()) {
                    selectedAddress = paymentAddress.getText().toString();
                } else {
                    selectedAddress = paymentAddressNew.getText().toString();
                }

                // Get the final price
                String finalPrice = txtFinalPrice.getText().toString();
                // get date and time
                String saveCurrentDate, saveCurrentTime;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                // Create a new document to store payment information
                Map<String, Object> paymentInfo = new HashMap<>();
                paymentInfo.put("ship_address", selectedAddress);
                paymentInfo.put("final_price", finalPrice);
                paymentInfo.put("time", saveCurrentDate + " " + saveCurrentTime);
                paymentInfo.put("status", "Created");


                // Reference to the "payment" subcollection under the user's document
                firestore.collection("users").document(userId).collection("payment").document().set(paymentInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Payment information saved successfully
                                // Clear the cart by deleting the cart data in Firestore (assuming you have a 'cart' collection)
                                firestore.collection("users").document(userId).collection("cart")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        document.getReference().delete();
                                                    }
                                                    Toast.makeText(PaymentActivity.this, "Payment successful, check your order!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(PaymentActivity.this, "Failed to clear the cart!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error if the payment information couldn't be saved
                                Toast.makeText(PaymentActivity.this, "Payment failed, try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void updateFinalPrice() {
        String selectedVoucher = edtVoucher.getText().toString();
        int discount = 0;

        // Search for the selected voucher in the voucher list
        for (Voucher voucher : voucherList) {
            if (voucher.getName().equals(selectedVoucher)) {
                discount = voucher.getDiscount();
                break; // Stop searching once the voucher is found
            }
        }

        // Calculate the final price
        double subtotal = Double.parseDouble(txtSubtotal.getText().toString().replace(",", "."));
        double shipping = Double.parseDouble(txtShipping.getText().toString().replace(",", "."));
        double finalPrice = (subtotal - (subtotal * discount / 100)) + shipping;

        // Update the "Discount" TextView
        txtDiscount.setText(discount + "%");

        // Update the "Final Price" TextView
        txtFinalPrice.setText(String.format("%.2f", finalPrice));
    }

}