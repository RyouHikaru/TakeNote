package com.example.takenote.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Add notes");
    }

    LiveData<String> getText() {
        return mText;
    }
}