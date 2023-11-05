package com.example.groceryapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.groceryapplication.utils.AndroidUtil;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderActivity extends AppCompatActivity {
    double subtotal, shipping, finalprice, discount;
    TextView txtName;
    TextView txtAddress;
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
    Map<String, Object> paymentInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

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

        // Set values to the TextViews

        // Get "user name" and "address" from FirestoreDB
        // Retrieve the user's information
        FirebaseUtil.currentUserInfoDocument().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the name field from the document
                    String userName = document.getString("name");
                    String address = document.getString("address");
                    if (userName != null) {
                        txtName.setText("Your name: " + userName);
                        paymentInfo.put("customerName", userName);
                        paymentInfo.put("customerId", FirebaseUtil.currentUserId());

                        txtAddress = findViewById(R.id.payment_address);
                        txtAddress.setText(address);
                    }
                }
            }
        });

        // Handle check address button
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

        // Set value to "Subtotal" get data from Cart
        subtotal = getIntent().getDoubleExtra("totalAmount", 0.0);
        txtSubtotal.setText(AndroidUtil.formatPrice(subtotal));

        // Set value to "shipping fee" load from FirestoreDB
        FirebaseUtil.systemFeeDocument().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the shipping fee
                        Long shippingFee = document.getLong("shipping");
                        if (shippingFee != null) {
                            shipping = shippingFee.doubleValue();
                            txtShipping.setText(AndroidUtil.formatPrice(shipping));
                            // Call the method to calculate and update the final price
                            updateFinalPrice();
                        }
                    }
                }
            }
        });

        FirebaseUtil.systemVoucherDocument().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                discount = Double.parseDouble(entry.getValue().toString());
                                Voucher voucher = new Voucher(name, discount);
                                voucherList.add(voucher);
                            }
                        }
                    }
                } else {
                    Toast.makeText(OrderActivity.this, "Failed to load voucher data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle button "apply voucher"
        applyVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                paymentInfo.put("address", selectedAddress);

                // Get the final price
                double finalPrice = Double.parseDouble(txtFinalPrice.getText().toString().replaceAll("[^0-9]", ""));
                paymentInfo.put("orderValue", finalPrice);

                // get date and time
                String saveCurrentDate, saveCurrentTime;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                saveCurrentTime = currentTime.format(calForDate.getTime());
                paymentInfo.put("date", saveCurrentDate);
                paymentInfo.put("time", saveCurrentTime);
                paymentInfo.put("status", "Created");

                // Reference to the "orders" collection
                FirebaseUtil.userOrdersCollection().add(paymentInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Payment information saved successfully
                                // Clear the cart by deleting the cart data in Firestore
                                FirebaseUtil.userCartCollection()
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        document.getReference().delete();
                                                    }
                                                    // Cart cleared successfully, now navigate to HomeActivity
                                                    Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    Toast.makeText(OrderActivity.this, "Order successful, check your order!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(OrderActivity.this, "Failed to clear the cart!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error if the payment information couldn't be saved
                                Toast.makeText(OrderActivity.this, "Order failed, try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void updateFinalPrice() {
        String selectedVoucher = edtVoucher.getText().toString();
        double discount = 0;

        // Search for the selected voucher in the voucher list
        for (Voucher voucher : voucherList) {
            if (voucher.getCode().equals(selectedVoucher)) {
                discount = voucher.getValue();
                break; // Stop searching once the voucher is found
            }
        }

        // Calculate the final price
        subtotal = Double.parseDouble(txtSubtotal.getText().toString().replaceAll("[^0-9]", ""));
        shipping = Double.parseDouble(txtShipping.getText().toString().replaceAll("[^0-9]", ""));
        finalprice = (subtotal - (subtotal * discount / 100)) + shipping;

        // Update the "Discount" TextView
        txtDiscount.setText(discount + "%");

        // Update the "Final Price" TextView
        txtFinalPrice.setText(AndroidUtil.formatPrice(finalprice));
    }
}
