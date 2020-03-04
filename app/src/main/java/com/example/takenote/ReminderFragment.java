package com.example.takenote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class ReminderFragment extends Fragment {
    // region Object and Variables
    private int textViewId;
    private int forEditTextViewId;
    private TakeNoteDatabase myDb;
    private View root;
    private Bundle bundle;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams layout;
    private LayoutInflater localInflater;
    private FloatingActionButton addButton;
    private Context contextThemeWrapper;
    private String reminderTitle, reminderContent, un;
    private HashMap hm;
    // endregion

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // region Mapping of Objects
        hm = new HashMap();
        textViewId = 1;
        contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoteFragmentStyle);
        localInflater = inflater.cloneInContext(contextThemeWrapper);
        root = localInflater.inflate(R.layout.fragment_reminder, container, false);
        addButton = root.findViewById(R.id.reminder_floatingActionButton);
        linearLayout = root.findViewById(R.id.reminder_linear_layout);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bundle = getActivity().getIntent().getExtras();
        myDb = new TakeNoteDatabase(getActivity().getApplicationContext());
        un = bundle.getString("UN");
        // endregion

        try {
            retrieveRemindersFromDatabase();    // retrieve the reminders stored in database
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        layout.setMargins(40, 40, 40, 40);

        // region Programmatically added TextViews
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInputDialog();
            }
        });
        // endregion
        return root;
    }
    public void openInputDialog() {
        InputReminderDialog dialog = new InputReminderDialog();
        dialog.setTargetFragment(this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "Note input");
    }
    public void openEditDialog(int textViewId) {
        try {
            EditNoteDialog dialog = new EditNoteDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("ID", textViewId);
            dialog.setArguments(bundle);

            dialog.setTargetFragment(this, 1);
            dialog.show(getActivity().getSupportFragmentManager(), "Note edit");

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void showItemDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(true);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        AlertDialog itemDialog = builder.create();

        itemDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                // Add or create your own background drawable for AlertDialog window
                Window view = ((AlertDialog)dialog).getWindow();
                view.setBackgroundDrawableResource(R.color.colorPrimary);

                // Customize POSITIVE and NEUTRAL buttons.
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.default_whitish_color));
                positiveButton.invalidate();
            }
        });

        itemDialog.show();
    }   // For the retrieved items
    public void retrieveRemindersFromDatabase() {
        Cursor cursor = myDb.getReminders(un);

        if (cursor == null) {
            System.out.println("userCursor is null");
        }
        while (cursor.moveToNext()) {
            int reminderNumber = cursor.getInt(0);
            reminderTitle = cursor.getString(1);
            reminderContent = cursor.getString(2);
            System.out.println("ID: " + textViewId + " reminderNumber: " + reminderNumber);
            hm.put(textViewId, reminderNumber);
            System.out.println("HMAP: " + hm);
            addTextView(reminderTitle, reminderContent);
        }
    }
    private void addTextView(final String title, final String content) {
        final TextView anotherTextView;
        anotherTextView = new TextView(getActivity());
        anotherTextView.setPaddingRelative(50, 40, 50, 40);
        anotherTextView.setBackgroundResource(R.drawable.ripple_effect);
        anotherTextView.setText(title);
        anotherTextView.setTextSize(18);
        anotherTextView.setVisibility(View.VISIBLE);
        anotherTextView.setLayoutParams(layout);
        anotherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemDialog(title, content);
            }
        });
        linearLayout.addView(anotherTextView);
        anotherTextView.setId(textViewId);

        anotherTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String[] options = getResources().getStringArray(R.array.noteOptions);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
                builder.setTitle("What to do?")
                        .setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(getActivity().getApplicationContext(), options[which], Toast.LENGTH_SHORT).show();
                                        switch (options[which]) {
                                            case "Edit":
                                                openEditDialog(anotherTextView.getId());
                                                break;
                                            case "Delete":
                                                boolean isDeleted = myDb.deleteReminder(Integer.parseInt(hm.get(anotherTextView.getId()).toString()), un);
                                                linearLayout.removeView(anotherTextView);
                                                if (isDeleted == true) {
                                                    Snackbar snackbar = Snackbar.make(root, "Reminder Deleted", Snackbar.LENGTH_LONG);
                                                    snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                                                    snackbar.show();
//                                                    Toast.makeText(getActivity().getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Snackbar snackbar = Snackbar.make(root, "Reminder Not Deleted", Snackbar.LENGTH_LONG);
                                                    snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                                                    snackbar.show();
//                                                    Toast.makeText(getActivity().getApplicationContext(), "Note not Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                                break;
                                        }
                                    }
                                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        textViewId++;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    reminderTitle = bundle.getString("TITLE");
                    reminderContent = bundle.getString("CONTENT");

                    boolean isInserted = myDb.insertReminder(un, reminderTitle, reminderContent);
                    if (isInserted == true) {
                        Snackbar snackbar = Snackbar.make(root, "Reminder Added", Snackbar.LENGTH_LONG);
                        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(root, "Reminder Not Added", Snackbar.LENGTH_LONG);
                        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                    }
                    int reminderNo = myDb.getRemindersLastRowId();
                    hm.put(textViewId, reminderNo);
                    addTextView(reminderTitle, reminderContent);
                }
                break;
            case 1:
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        Bundle bundle = data.getExtras();
                        forEditTextViewId = bundle.getInt("TVID");
                        reminderTitle = bundle.getString("TITLE");
                        reminderContent = bundle.getString("CONTENT");
                        int id = Integer.parseInt(hm.get(forEditTextViewId).toString());


                        boolean isEdited = myDb.editReminder(id, un, reminderTitle, reminderContent);
                        if (isEdited == true) {
                            Snackbar snackbar = Snackbar.make(root, "Reminder Edited", Snackbar.LENGTH_LONG);
                            snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(root, "Reminder Not Edited", Snackbar.LENGTH_LONG);
                            snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                            snackbar.show();
                        }
                        TextView thisTextView = linearLayout.findViewById(forEditTextViewId);
                        thisTextView.setText(reminderTitle);
                        thisTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showItemDialog(reminderTitle, reminderContent);
                            }
                        });
                    }
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
        }
    }
}
