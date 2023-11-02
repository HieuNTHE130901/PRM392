package com.example.groceryapplication.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groceryapplication.databinding.FragmentProfileBinding;

import java.util.zip.Inflater;

public class ProfileFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = FragmentProfileBinding.inflate(inflater, container, false).getRoot();

        return root;
    }


}
