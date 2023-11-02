package com.example.groceryapplication.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.ItemAdapter;
import com.example.groceryapplication.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Item> fruitList;
    List<Item> vegetableList;
    RecyclerView fruitRecycle;
    RecyclerView vegetableRecycle;

    ItemAdapter itemAdapter1;
    ItemAdapter itemAdapter2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        vegetableRecycle = root.findViewById(R.id.rec_vegetable);
        vegetableRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        vegetableList = new ArrayList<>();
        itemAdapter1 = new ItemAdapter(getActivity(), vegetableList);
        vegetableRecycle.setAdapter(itemAdapter1);

        db.collection("vegetable")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                vegetableList.add(item);
                                itemAdapter1.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),"Error "+ task.getException(), Toast.LENGTH_SHORT);
                        }
                    }
                });

        fruitRecycle = root.findViewById(R.id.rec_fruit);
        fruitRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        fruitList = new ArrayList<>();
        itemAdapter2 = new ItemAdapter(getActivity(), fruitList);
        fruitRecycle.setAdapter(itemAdapter2);
        db.collection("fruit")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                fruitList.add(item);
                                itemAdapter2.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),"Error "+ task.getException(), Toast.LENGTH_SHORT);
                        }
                    }
                });




        return root;
    }


}