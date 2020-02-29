package com.example.takenote.ui.user_profile;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.takenote.R;
import com.example.takenote.TakeNoteDatabase;

public class UserProfileFragment extends Fragment {
    private TakeNoteDatabase myDb;
    private View root;
    private Cursor userCursor;
    private TextView fnTV, lnTV, aTV, eTV;
    private String username;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        fnTV = root.findViewById(R.id.firstnameTextView);
        lnTV = root.findViewById(R.id.lastnameTextView);
        aTV = root.findViewById(R.id.addressTextView);
        eTV = root.findViewById(R.id.emailTextView);
        myDb = new TakeNoteDatabase(getActivity());

        int[] set = myDb.getSettings();
        if (set[0] == 1) {
            fnTV.setTextColor(getResources().getColor(R.color.default_whitish_color));
            lnTV.setTextColor(getResources().getColor(R.color.default_whitish_color));
            aTV.setTextColor(getResources().getColor(R.color.default_whitish_color));
            eTV.setTextColor(getResources().getColor(R.color.default_whitish_color));
        }

        username = getActivity().getIntent().getStringExtra("UN");
        System.out.println(username);

        userCursor = myDb.getUserDetails(username);
        if (userCursor == null) {
            System.out.println("userCursor is null");
        }
        while(userCursor.moveToNext()) {
            fnTV.setText(fnTV.getText().toString() + ": " + userCursor.getString(0));
            lnTV.setText(lnTV.getText().toString() + ": " + userCursor.getString(1));
            aTV.setText(aTV.getText().toString() + ": " + userCursor.getString(2));
            eTV.setText(eTV.getText().toString() + ": " + userCursor.getString(3));
        }
        return root;
    }
}