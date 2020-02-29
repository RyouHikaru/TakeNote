package com.example.takenote;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    private View root;
    private Switch darkModeToggle, notificationsToggle;
    private TakeNoteDatabase myDb;
    private Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            root = inflater.inflate(R.layout.fragment_settings, container, false);
            myDb = new TakeNoteDatabase(getActivity());
            darkModeToggle = root.findViewById(R.id.darkModeToggle);
            notificationsToggle = root.findViewById(R.id.notificationsToggle);
            saveButton = root.findViewById(R.id.saveButton);

            int[] set = myDb.getSettings();
            if (set[0] == 0 && set[1] == 0) {
                darkModeToggle.setChecked(false);
                notificationsToggle.setChecked(false);
            }
            else if (set[0] == 0 && set[1] == 1) {
                darkModeToggle.setChecked(false);
                notificationsToggle.setChecked(true);
            }
            else if (set[0] == 1 && set[1] == 0) {
                darkModeToggle.setChecked(true);
                notificationsToggle.setChecked(false);
                darkModeToggle.setTextColor(getResources().getColor(R.color.white));
                notificationsToggle.setTextColor(getResources().getColor(R.color.white));
            }
            else {
                darkModeToggle.setChecked(true);
                notificationsToggle.setChecked(true);
                darkModeToggle.setTextColor(getResources().getColor(R.color.white));
                notificationsToggle.setTextColor(getResources().getColor(R.color.white));
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (darkModeToggle.isChecked() && notificationsToggle.isChecked()) {
                            myDb.editSettings(1, 1);
                        } else if (darkModeToggle.isChecked() && !notificationsToggle.isChecked()) {
                            myDb.editSettings(1, 0);
                        } else if (!darkModeToggle.isChecked() && notificationsToggle.isChecked()) {
                            myDb.editSettings(0, 1);
                        } else {
                            myDb.editSettings(0, 0);
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "Settings applied. App will restart", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.exit(0);
                            }
                        }, 2000);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return root;
    }
}