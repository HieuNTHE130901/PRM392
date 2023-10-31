package com.example.groceryapplication.ui.cart;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.NavCategoryAdapter;
import com.example.groceryapplication.models.NavCategory;
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

    TextView totalAmount;
    RecyclerView recyclerView;
    Button buy;
    List<NavCategory> categoryList;
    NavCategoryAdapter navCategoryAdapter;
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
        navCategoryAdapter = new NavCategoryAdapter(getActivity(), categoryList);
        recyclerView.setAdapter(navCategoryAdapter);
        totalAmount = root.findViewById(R.id.cart_total_price);
        buy = root.findViewById(R.id.buy_now);

        db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        NavCategory cart = documentSnapshot.toObject(NavCategory.class);
                        categoryList.add(cart);
                        navCategoryAdapter.notifyDataSetChanged();
                    }
                    calculateTotal(categoryList);
                }
            }
        });




        return root;
    }

    private void calculateTotal(List<NavCategory> categoryList) {
        double totalAmountt = 0.0;
        for (NavCategory model : categoryList) {
            double totalPrice = Double.parseDouble(model.getTotalPrice());
            totalAmountt += totalPrice;
        }

        // Format the totalAmountt value to display up to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        String formattedTotal = decimalFormat.format(totalAmountt);

        // Set the formatted total to the TextView
        totalAmount.setText(formattedTotal);
    }


}