package com.example.takenote.ui.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.takenote.EditNoteDialog;
import com.example.takenote.InputNoteDialog;
import com.example.takenote.R;
import com.example.takenote.TakeNoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class NotesFragment extends Fragment {
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
    private String noteTitle, noteContent, un;
    private View.OnLongClickListener longClickListener;
    private HashMap hm;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        System.out.println("ON CREATE");
        // region Mapping of Objects
        hm = new HashMap();
        textViewId = 1;
        contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoteFragmentStyle);
        localInflater = inflater.cloneInContext(contextThemeWrapper);
        root = localInflater.inflate(R.layout.fragment_notes, container, false);
        addButton = root.findViewById(R.id.notes_floatingActionButton);
        linearLayout = root.findViewById(R.id.notes_linear_layout);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 140);
        bundle = getActivity().getIntent().getExtras();
        myDb = new TakeNoteDatabase(getActivity().getApplicationContext());
        un = bundle.getString("UN");
//        System.out.println(un);
        // endregion

        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        };

        try {
            retrieveNotesFromDatabase();    // retrieve the notes stored in database
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
        InputNoteDialog dialog = new InputNoteDialog();
        dialog.setTargetFragment(this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "Note input");
    }
    public void openEditDialog(int textViewId) {
        try {
//            System.out.println("HashMap: " + hm.get(textViewId)); CLEAR
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
    public void retrieveNotesFromDatabase() {
        Cursor cursor = myDb.getNotes(un);

        if (cursor == null) {
            System.out.println("userCursor is null");
        }
        while (cursor.moveToNext()) {
            int noteNumber = cursor.getInt(0);
//            System.out.println(noteNumber);
            noteTitle = cursor.getString(1);
//            System.out.println(cursor.getString(1));
            noteContent = cursor.getString(2);
//            System.out.println(cursor.getString(2));
            System.out.println("ID: " + textViewId + " noteNumber: " + noteNumber);
            hm.put(textViewId, noteNumber);
            System.out.println("HMAP: " + hm);
            addTextView(noteTitle, noteContent);
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
//        System.out.println("TV ID: " + anotherTextView.getId());
//        System.out.println(linearLayout.findViewById(textViewId));

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
                                                boolean isDeleted = myDb.deleteNote(Integer.parseInt(hm.get(anotherTextView.getId()).toString()), un);
                                                linearLayout.removeView(anotherTextView);
                                                if (isDeleted == true) {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Note not Deleted", Toast.LENGTH_SHORT).show();
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
                    noteTitle = bundle.getString("TITLE");
                    noteContent = bundle.getString("CONTENT");

                    boolean isInserted = myDb.insertNote(un, noteTitle, noteContent);
                    if (isInserted == true) {
                        Toast.makeText(getActivity().getApplicationContext(), "Note Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Note Not Added", Toast.LENGTH_SHORT).show();
                    }
                    int noteNo = myDb.getLastRowId();
                    hm.put(textViewId, noteNo);
                    addTextView(noteTitle, noteContent);
                }
                break;
            case 1:
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        Bundle bundle = data.getExtras();
                        forEditTextViewId = bundle.getInt("TVID");
                        noteTitle = bundle.getString("TITLE");
                        noteContent = bundle.getString("CONTENT");
                        int id = Integer.parseInt(hm.get(forEditTextViewId).toString());

//                        System.out.println("TVID: " + forEditTextViewId
//                        + "\nNEWTITLE: " + noteTitle
//                        + "\nNEWCONTENT: " + noteContent);    CLEAR

                        boolean isEdited = myDb.editNote(id, un, noteTitle, noteContent);
                        if (isEdited == true) {
                            Toast.makeText(getActivity().getApplicationContext(), "Note Edited", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Note Not Added", Toast.LENGTH_SHORT).show();
                        }
                        TextView thisTextView = linearLayout.findViewById(forEditTextViewId);
                        thisTextView.setText(noteTitle);
                        thisTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showItemDialog(noteTitle, noteContent);
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

