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

import com.example.takenote.InputNoteDialog;
import com.example.takenote.R;
import com.example.takenote.TakeNoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {
    private TakeNoteDatabase myDb;
    private View root;
    private Bundle bundle;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams layout;
    private LayoutInflater localInflater;
    private FloatingActionButton addButton;
    private Context contextThemeWrapper;
    private String noteTitle, noteContent, un;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        System.out.println("ON CREATE");
        // region Mapping of Objects
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

        retrieveNotesFromDatabase();

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
            noteTitle = cursor.getString(0);
            System.out.println("TITLE: " + noteTitle);
            noteContent = cursor.getString(1);
            System.out.println("CONTENT: " + noteContent);
            addTextView(noteTitle, noteContent);
        }
    }
    private void addTextView(final String title, final String content) {
        TextView anotherTextView;
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
        registerForContextMenu(anotherTextView);
    }   // For the retrieved items
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
                    addTextView(noteTitle, noteContent);
                }
                break;
        }

    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.notes_context_menu, menu);
        menu.setHeaderTitle("What to do?");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.editContextItem:
                Toast.makeText(getActivity().getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteContextItem:
                Toast.makeText(getActivity().getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
                deleteNote();
                break;
            default:
                return false;
        }
        return true;
    }
    public void deleteNote() {

    }
}

