package com.example.takenote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {
    private View root;
    private Switch darkModeToggle, notificationsToggle;
    private TakeNoteDatabase myDb;
    private Button saveButton;
    private TextView generalTextView, accountTextView;
    private EditText pwET, fnET, lnET, aET,eET;
    private static String username;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            root = inflater.inflate(R.layout.fragment_setting, container, false);
            myDb = new TakeNoteDatabase(getActivity());
            darkModeToggle = root.findViewById(R.id.darkModeSwitch);
            notificationsToggle = root.findViewById(R.id.notificationSwitch);
            saveButton = root.findViewById(R.id.save);
            generalTextView = root.findViewById(R.id.generalTextView);
            accountTextView = root.findViewById(R.id.accountTextView);
            pwET = root.findViewById(R.id.s_pw);
            fnET = root.findViewById(R.id.s_firstname);
            lnET = root.findViewById(R.id.s_lastname);
            aET = root.findViewById(R.id.s_address);
            eET = root.findViewById(R.id.s_email);
            username = getActivity().getIntent().getStringExtra("UN");

            int[] set = myDb.getUserSettings(username);
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
                switchTextColors();
            }
            else {
                darkModeToggle.setChecked(true);
                notificationsToggle.setChecked(true);
                switchTextColors();
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final String newPw = pwET.getText().toString();
                        final String newFn = fnET.getText().toString();
                        final String newLn = lnET.getText().toString();
                        final String newAdd = aET.getText().toString();
                        final String newEmail = eET.getText().toString();

                        if (!newPw.isEmpty() || !newFn.isEmpty() || !newLn.isEmpty() || !newAdd.isEmpty() || !newEmail.isEmpty()) {
                            if (validPassword(newPw) == false) {
                                pwET.requestFocus();
                                return;
                            }
                            if (validName(newFn) == false) {
                                fnET.requestFocus();
                                return;
                            }
                            if (validName(newLn) == false) {
                                lnET.requestFocus();
                                return;
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
                            builder.setTitle("User details will be updated")
                                    .setMessage("Only non-empty fields will be updated. Proceed?")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            myDb.editUser(username, newPw, newFn, newLn, newAdd, newEmail);
                                            Snackbar snackbar = Snackbar.make(root, "User details updated", Snackbar.LENGTH_LONG);
                                            snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                                            snackbar.show();

                                            switchSettings();

                                            Toast.makeText(getActivity().getApplicationContext(), "Settings applied. App will restart", Toast.LENGTH_LONG).show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    System.exit(0);
                                                }
                                            }, 2000);
                                            return;
                                        }
                                    })
                                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    Window view = ((AlertDialog)dialog).getWindow();
                                    view.setBackgroundDrawableResource(R.color.colorPrimary);

                                    Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                                    positiveButton.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.default_whitish_color));
                                    positiveButton.invalidate();

                                    Button neutralButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                                    neutralButton.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.default_whitish_color));
                                    neutralButton.invalidate();
                                }
                            });
                            dialog.show();
                        }
                        else {
                            switchSettings();

                            Toast.makeText(getActivity().getApplicationContext(), "Settings applied. App will restart", Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.exit(0);
                                }
                            }, 3000);
                        }
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
    public boolean validPassword(String password) {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;
        boolean hasNumber = false;
        boolean valid = false;

        if (password.isEmpty()) {
            return true;
        }
        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                if (password.charAt(i) >= 65 && password.charAt(i) <= 90) {
                    hasUpper = true;
                }
                else if (password.charAt(i) >= 97 && password.charAt(i) <= 122) {
                    hasLower = true;
                }
                else if ((password.charAt(i) >= 33 && password.charAt(i) <= 47) ||
                        (password.charAt(i) >= 58 && password.charAt(i) <= 64) ||
                        (password.charAt(i) >= 91 && password.charAt(i) <= 96) ||
                        (password.charAt(i) >= 123 && password.charAt(i) <= 126)) {
                    hasSpecial = true;
                }
                else if (password.charAt(i) >= 48 && password.charAt(i) <= 57) {
                    hasNumber = true;
                }
                else {
                    break;
                }
            }
        }
        else {
            Toast.makeText(getActivity(), "Password should have a minimum of 8 characters", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }

        if (hasUpper == true && hasLower == true && hasSpecial == true && hasNumber == true) {
            valid = true;
        }
        else {
            Toast.makeText(getActivity(), "Password does not meet the criteria", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
    public boolean validName(String name) {
        boolean hasSpecial = false;
        boolean hasNumber = false;
        boolean valid = true;

        if (name.length() == 0) {
            return true;
        }
        for (int i = 0; i < name.length(); i++) {
            if ((name.charAt(i) >= 33 && name.charAt(i) <= 47) ||
                    (name.charAt(i) >= 58 && name.charAt(i) <= 64) ||
                    (name.charAt(i) >= 91 && name.charAt(i) <= 96) ||
                    (name.charAt(i) >= 123 && name.charAt(i) <= 126)) {
                hasSpecial = true;
                valid = false;
                break;
            } else if (name.charAt(i) >= 48 && name.charAt(i) <= 57) {
                hasNumber = true;
                valid = false;
                break;
            }
        }

        if (hasNumber == true || hasSpecial == true) {
            Toast.makeText(getActivity(), "Names shouldn't have numbers or special characters", Toast.LENGTH_SHORT).show();
            return valid;
        }
        else {
            return valid;
        }
    }
    private void switchTextColors() {
        darkModeToggle.setTextColor(getResources().getColor(R.color.white));
        notificationsToggle.setTextColor(getResources().getColor(R.color.white));
        generalTextView.setTextColor(getResources().getColor(R.color.white));
        accountTextView.setTextColor(getResources().getColor(R.color.white));
        pwET.setTextColor(getResources().getColor(R.color.white));
        pwET.setHintTextColor(getResources().getColor(R.color.default_whitish_color));
        pwET.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        fnET.setTextColor(getResources().getColor(R.color.white));
        fnET.setHintTextColor(getResources().getColor(R.color.default_whitish_color));
        fnET.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        lnET.setTextColor(getResources().getColor(R.color.white));
        lnET.setHintTextColor(getResources().getColor(R.color.default_whitish_color));
        lnET.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        aET.setTextColor(getResources().getColor(R.color.white));
        aET.setHintTextColor(getResources().getColor(R.color.default_whitish_color));
        aET.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        eET.setTextColor(getResources().getColor(R.color.white));
        eET.setHintTextColor(getResources().getColor(R.color.default_whitish_color));
        eET.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    }
    private void switchSettings() {
        if (darkModeToggle.isChecked() && notificationsToggle.isChecked()) {
            myDb.editSettings(username, 1, 1);
        } else if (darkModeToggle.isChecked() && !notificationsToggle.isChecked()) {
            myDb.editSettings(username,1, 0);
        } else if (!darkModeToggle.isChecked() && notificationsToggle.isChecked()) {
            myDb.editSettings(username, 0, 1);
        } else {
            myDb.editSettings(username, 0, 0);
        }
    }
}