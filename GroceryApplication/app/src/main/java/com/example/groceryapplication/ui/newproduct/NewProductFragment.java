package com.example.groceryapplication.ui.newproduct;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.groceryapplication.databinding.FragmentNewProductBinding;


public class NewProductFragment extends Fragment {

    private FragmentNewProductBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewProductViewModel homeViewModel =
                new ViewModelProvider(this).get(NewProductViewModel.class);

        binding = FragmentNewProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNewProduct;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}