package com.example.takenote.ui.user_profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("User Profile Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
