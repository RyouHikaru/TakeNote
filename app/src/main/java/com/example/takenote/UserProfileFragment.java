package com.example.takenote;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class UserProfileFragment extends Fragment {
    private static TakeNoteDatabase myDb;
    private View root;
    private static Cursor userCursor;
    private TextView fnTV, lnTV, aTV, eTV;
    private static String username;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        fnTV = root.findViewById(R.id.firstnameTextView);
        lnTV = root.findViewById(R.id.lastnameTextView);
        aTV = root.findViewById(R.id.addressTextView);
        eTV = root.findViewById(R.id.emailTextView);
        myDb = new TakeNoteDatabase(getActivity());
        username = getActivity().getIntent().getStringExtra("UN");

        int[] set = myDb.getUserSettings(username);
        if (set[0] == 1) {
            fnTV.setTextColor(getResources().getColor(R.color.white));
            lnTV.setTextColor(getResources().getColor(R.color.white));
            aTV.setTextColor(getResources().getColor(R.color.white));
            eTV.setTextColor(getResources().getColor(R.color.white));
        }


        userCursor = myDb.getUserDetails(username);
        if (userCursor == null) {
//            System.out.println("userCursor is null");
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