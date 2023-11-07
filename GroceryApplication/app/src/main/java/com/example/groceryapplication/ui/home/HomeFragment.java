package com.example.groceryapplication.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.ItemAdapter;
import com.example.groceryapplication.models.Item;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Item> fruitList;
    List<Item> vegetableList;

    List<Item> meatList;
    RecyclerView fruitRecycle;
    RecyclerView vegetableRecycle;
    RecyclerView meatRec;
    ItemAdapter vegetableAdapter;
    ItemAdapter fruitAdapter;
    ItemAdapter meatAdapter;



    ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Load fruit data

        //set up Fruit recycle
        fruitRecycle = root.findViewById(R.id.rec_fruit);
        fruitRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        //Take fruit list from Firestore
        fruitList = new ArrayList<>();
        // Retrieve fruit data from Firestore using FirebaseUtil
        FirebaseUtil.fruitCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                fruitList.add(item);
                                fruitAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT);
                        }
                    }
                });

        //Use adapter to set recyclerView
        fruitAdapter = new ItemAdapter(getActivity(), fruitList);
        fruitRecycle.setAdapter(fruitAdapter);



        // Load vegetable data
        vegetableRecycle = root.findViewById(R.id.rec_vegetable);
        vegetableRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        vegetableList = new ArrayList<>();
        vegetableAdapter = new ItemAdapter(getActivity(), vegetableList);
        vegetableRecycle.setAdapter(vegetableAdapter);

        // Retrieve vegetable data from Firestore using FirebaseUtil
        FirebaseUtil.vegetableCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                vegetableList.add(item);
                                vegetableAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT);
                        }
                    }
                });

        // Load meat data
        meatRec = root.findViewById(R.id.rec_meat);
        meatRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        meatList = new ArrayList<>();
        meatAdapter = new ItemAdapter(getActivity(), meatList);
        meatRec.setAdapter(meatAdapter);

        // Retrieve vegetable data from Firestore using FirebaseUtil
        FirebaseUtil.meatCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                meatList.add(item);
                                meatAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT);
                        }
                    }
                });


        return root;
    }
}
