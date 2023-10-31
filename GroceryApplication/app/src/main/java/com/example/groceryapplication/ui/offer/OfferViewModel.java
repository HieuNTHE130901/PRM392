package com.example.groceryapplication.ui.offer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OfferViewModel extends ViewModel {
    // TODO: Implement the ViewModel


    private final MutableLiveData<String> mText;

    public OfferViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is offer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}