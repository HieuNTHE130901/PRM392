package com.example.groceryapplication.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserInfoDocument() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId())
                .collection("information")
                .document("information");
    }

    public static CollectionReference userCartCollection() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId()).collection("cart");
    }
    public static CollectionReference userPaymentCollection() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId()).collection("payment");
    }
    public static DocumentReference systemVoucherDocument() {
        return FirebaseFirestore.getInstance().collection("system_setting")
                .document("voucher");
    }




}
