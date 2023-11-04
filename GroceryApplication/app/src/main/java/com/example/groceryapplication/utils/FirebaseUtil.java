package com.example.groceryapplication.utils;

import androidx.annotation.NonNull;

import com.example.groceryapplication.models.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUtil {
    // Get the current user's ID
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    // Get a reference to the current user's information document
    public static DocumentReference currentUserInfoDocument() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId());
    }

    // Get a reference to the "fruit" collection in Firestore
    public static CollectionReference fruitCollection(){
        return FirebaseFirestore.getInstance().collection("fruit");
    }

    // Get a reference to the "vegetable" collection in Firestore
    public static CollectionReference vegetableCollection(){
        return FirebaseFirestore.getInstance().collection("vegetable");
    }

    // Get a reference to the "vegetable" collection in Firestore
    public static CollectionReference meatCollection(){
        return FirebaseFirestore.getInstance().collection("meat");
    }


    // Get a reference to the current user's cart collection
    public static CollectionReference userCartCollection() {
        return FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId()).collection("cart");
    }

    // Get a reference to the current user's order collection
    public static CollectionReference userOrdersCollection() {
        return FirebaseFirestore.getInstance().collection("orders")
                .document(currentUserId()).collection("cart");
    }


    // Get a reference to the "voucher" document in the "system_setting" collection
    public static DocumentReference systemVoucherDocument() {
        return FirebaseFirestore.getInstance().collection("system_setting")
                .document("voucher");
    }

    // Get a reference to the "fee" document in the "system_setting" collection
    public static DocumentReference systemFeeDocument() {
        return FirebaseFirestore.getInstance().collection("system_setting")
                .document("fee");
    }

    // Get a reference to a chatroom based on its ID
    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    // Get a reference to the messages within a chatroom based on its ID
    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static void updateCartItem(Cart cartItem) {
        userCartCollection().document(cartItem.getDocummentId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("totalQuantity", cartItem.getTotalQuantity());
        updates.put("totalPrice", cartItem.getTotalPrice());

        // Update the document with the new data
        userCartCollection().document(cartItem.getDocummentId()).update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document updated successfully
                        // You can handle any success actions here if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error if the document update fails
                        // You can add logging or display an error message
                    }
                });
    }
}
