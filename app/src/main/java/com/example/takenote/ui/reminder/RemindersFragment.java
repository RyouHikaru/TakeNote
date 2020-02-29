package com.example.takenote.ui.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.takenote.R;

import java.util.Calendar;

public class RemindersFragment extends Fragment {
    private Button button;
    private TextView textView;
    private int mYear, mMonth, mDay, hour, min;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reminders, container, false);

        button = root.findViewById(R.id.timePickerButton);
        textView = root.findViewById(R.id.text_reminder);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("CLICKED");
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                min = c.get(Calendar.MINUTE);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mYear = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                textView.setText(month + "/" + dayOfMonth + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                try {
                    if (!datePickerDialog.isShowing()) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        textView.setText(hourOfDay + ":" + minute);
                                    }
                                }, hour, min, false);

                        timePickerDialog.show();
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
        return root;
    }
}