package com.example.groceryapplication.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groceryapplication.databinding.FragmentChatBinding;


public class ChatFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = FragmentChatBinding.inflate(inflater, container, false).getRoot();

        return root;
    }



}