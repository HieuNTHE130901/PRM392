package com.example.groceryapplication.ui.newproduct;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewProductViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewProductViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is new product fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}