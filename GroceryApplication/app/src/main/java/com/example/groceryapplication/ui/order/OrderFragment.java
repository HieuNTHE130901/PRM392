package com.example.groceryapplication.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.OrderAdapter;
import com.example.groceryapplication.models.Order;
import com.example.groceryapplication.ui.chat.ChatFragment;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;



    public class OrderFragment extends Fragment {

        private OrderAdapter orderAdapter;
        private RecyclerView recyclerView;
        private Button chatButton;
        private NavController navController;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_order, container, false);

            chatButton = root.findViewById(R.id.chat_with_store);

            navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_home);

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.nav_chat);
                }
            });

            // Initialize Firestore and query for orders
            Query query = FirebaseUtil.userOrdersCollection().orderBy("date");

            // Configure the adapter
            FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                    .setQuery(query, Order.class)
                    .build();

            orderAdapter = new OrderAdapter(options);

            recyclerView = root.findViewById(R.id.order_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(orderAdapter);

            return root;
        }

        @Override
        public void onStart() {
            super.onStart();
            orderAdapter.startListening();
        }

        @Override
        public void onStop() {
            super.onStop();
            orderAdapter.stopListening();
        }
    }