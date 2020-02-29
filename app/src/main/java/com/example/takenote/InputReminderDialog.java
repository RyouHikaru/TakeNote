package com.example.takenote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;
import java.util.Date;

public class InputReminderDialog extends AppCompatDialogFragment {
    private EditText titleEditText, contentEditText, dateEditText, timeEditText;
    private Button dateButton;
    private ContextThemeWrapper contextThemeWrapper;
    private View view;
    private int mYear, mMonth, mDay, mHour, mMin;
    private Calendar c;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoteFragmentStyle);
        LayoutInflater inflater = getActivity().getLayoutInflater().cloneInContext(contextThemeWrapper);
        view = inflater.inflate(R.layout.input_reminder_layout, null);

        titleEditText = view.findViewById(R.id.r_titleEditText);
        contentEditText = view.findViewById(R.id.r_contentEditText);
        dateEditText = view.findViewById(R.id.r_dateEditText);
        timeEditText = view.findViewById(R.id.r_timeEditText);
        dateButton = view.findViewById(R.id.dateButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateEditText.setText(year + "/" + month + "/" + dayOfMonth);

                                c = Calendar.getInstance();
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMin = c.get(Calendar.MINUTE);

                                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                if (minute < 10)
                                                    timeEditText.setText(hourOfDay + " : 0" + minute);
                                                else
                                                    timeEditText.setText(hourOfDay + " : " + minute);
                                            }
                                        }, mHour, mMin, false);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

//        timeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                c = Calendar.getInstance();
//                mHour = c.get(Calendar.HOUR_OF_DAY);
//                mMin = c.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme,
//                        new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                if (minute < 10)
//                                    timeTextView.setText(hourOfDay + " : 0" + minute);
//                                else
//                                    timeTextView.setText(hourOfDay + " : " + minute);
//                            }
//                        }, mHour, mMin, false);
//                timePickerDialog.show();
//            }
//        });

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleEditText.getText().toString();
                        String content = contentEditText.getText().toString();
//                        System.out.println(title + " " + content);
                        Intent i = new Intent().putExtra("TITLE", title).putExtra("CONTENT", content);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                        dismiss();
                    }

                });

        AlertDialog thisDialog = builder.create();

        thisDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                // Add or create your own background drawable for AlertDialog window
                Window view = ((AlertDialog)dialog).getWindow();
                view.setBackgroundDrawableResource(R.color.colorPrimary);

                // Customize POSITIVE and NEUTRAL buttons.
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.default_whitish_color));
                positiveButton.invalidate();

                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.default_whitish_color));
                negativeButton.invalidate();
            }
        });

        return thisDialog;
    }
}
