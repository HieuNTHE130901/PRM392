package com.example.groceryapplication.ui.offer;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.databinding.FragmentOfferBinding;

public class OfferFragment extends Fragment {


    private FragmentOfferBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OfferViewModel homeViewModel =
                new ViewModelProvider(this).get(OfferViewModel.class);

        binding = FragmentOfferBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textOffer;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}