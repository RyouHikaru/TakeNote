package com.example.takenote.ui.archive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArchiveViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ArchiveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Archived items go here");
    }

    LiveData<String> getText() {
        return mText;
    }
}