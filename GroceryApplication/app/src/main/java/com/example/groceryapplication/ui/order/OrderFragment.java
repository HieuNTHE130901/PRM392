package com.example.groceryapplication.ui.order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.CartAdapter;
import com.example.groceryapplication.adapter.OrderAdapter;
import com.example.groceryapplication.models.Cart;
import com.example.groceryapplication.models.Order;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

   OrderAdapter orderAdapter;
    RecyclerView recyclerView;
    List<Order> orderList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView = root.findViewById(R.id.order_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getActivity(), orderList, this); // Pass a reference to the CartFragment
        recyclerView.setAdapter(orderAdapter);
        // Retrieve cart items from Firestore
        FirebaseUtil.userOrdersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    orderList.clear(); // Clear the list before adding items
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String documentId = documentSnapshot.getId();
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setDocummentId(documentId);
                        orderList.add(order);
                        orderAdapter.notifyDataSetChanged();
                    }
                    // Calculate the total and check if the cart has products
                    if (orderList.isEmpty()) {
                        // The cart is empty
                        Toast.makeText(getContext(), "Your order is empty", Toast.LENGTH_SHORT).show();


                    } else {
                        // The cart has products
                        Toast.makeText(getContext(), "Your order have items", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }

}
