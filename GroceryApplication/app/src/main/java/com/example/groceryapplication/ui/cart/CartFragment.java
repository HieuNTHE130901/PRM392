package com.example.groceryapplication.ui.cart;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.CategoryAdapter;
import com.example.groceryapplication.activities.PaymentActivity;
import com.example.groceryapplication.models.Cart;
import com.example.groceryapplication.utils.AndroidUtil;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    TextView txtTotalAmount;
    RecyclerView recyclerView;
    Button buy;
    List<Cart> cartList;
    CategoryAdapter categoryAdapter;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize the ProgressBar and views
        progressBar = root.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        cartList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), cartList, this); // Pass a reference to the CartFragment
        recyclerView.setAdapter(categoryAdapter);
        txtTotalAmount = root.findViewById(R.id.cart_total_price);
        buy = root.findViewById(R.id.buy_now);

        // Retrieve cart items from Firestore
        FirebaseUtil.userCartCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    cartList.clear(); // Clear the list before adding items
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String documentId = documentSnapshot.getId();
                        Cart cart = documentSnapshot.toObject(Cart.class);
                        cart.setDocummentId(documentId);
                        cartList.add(cart);
                        categoryAdapter.notifyDataSetChanged();
                    }
                    // Calculate the total and check if the cart has products
                    calculateTotal(cartList);
                    if (cartList.isEmpty()) {
                        // The cart is empty
                        Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                        // Disable the "buy" button
                        buy.setEnabled(false);
                        updateTotalPrice();
                    } else {
                        // The cart has products
                        buy.setEnabled(true);
                        updateTotalPrice();
                    }
                }
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = calculateTotal(cartList);
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra("totalAmount", totalAmount);
                startActivity(intent);
            }
        });
        return root;
    }

    public void updateTotalPrice() {
        // Calculate and update the total price
        double totalAmount = calculateTotal(cartList);
        // Set the formatted total to the TextView
        txtTotalAmount.setText(AndroidUtil.formatPrice(totalAmount));
    }

    private double calculateTotal(List<Cart> cartList) {
        double totalAmount = 0.0;
        for (Cart cart : cartList) {
            totalAmount += cart.getTotalPrice();
        }
        return totalAmount;
    }

}
