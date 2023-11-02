package com.example.groceryapplication.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    TextView txtTotalAmount;
    RecyclerView recyclerView;
    Button buy;
    List<Cart> categoryList;
    CategoryAdapter categoryAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryList, this); // Pass a reference to the CartFragment
        recyclerView.setAdapter(categoryAdapter);
        txtTotalAmount = root.findViewById(R.id.cart_total_price);
        buy = root.findViewById(R.id.buy_now);

        db.collection("users").document(auth.getCurrentUser().getUid()).collection("cart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            categoryList.clear(); // Clear the list before adding items
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                String documentId = documentSnapshot.getId();
                                Cart cart = documentSnapshot.toObject(Cart.class);
                                cart.setDocummentId(documentId);
                                categoryList.add(cart);
                                categoryAdapter.notifyDataSetChanged();
                            }

                            // Calculate the total and check if the cart has products
                            calculateTotal(categoryList);
                            if (categoryList.isEmpty()) {
                                // The cart is empty
                                Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                                // Disable the "buy" button
                                buy.setEnabled(false);
                                updateTotalPrice();
                            } else {
                                // The cart has products
                                // You can enable the "buy" button or take any other action
                                buy.setEnabled(true);
                                updateTotalPrice();
                            }
                        }
                    }
                });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalAmount = calculateTotal(categoryList);
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra("totalAmount", totalAmount);
                startActivity(intent);
            }
        });

        return root;
    }

    public void updateTotalPrice() {
        // Calculate and update the total price
        double totalAmount = calculateTotal(categoryList);
        // Set the formatted total to the TextView
        txtTotalAmount.setText(String.format("%.2f", totalAmount));
    }

    private double calculateTotal(List<Cart> categoryList) {
        double totalAmount = 0.0;
        for (Cart model : categoryList) {
            double totalPrice = Double.parseDouble(model.getTotalPrice());
            totalAmount += totalPrice;
        }

        return totalAmount;
    }
}
