package com.example.takenote.ui.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.takenote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {
    private View root;
    private NotesViewModel notesViewModel;
    private View.OnClickListener notesListener;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams layout;
    private LayoutInflater localInflater;
    private FloatingActionButton addButton;
    private Context contextThemeWrapper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // region Mapping of Objects
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoteFragmentStyle);
        localInflater = inflater.cloneInContext(contextThemeWrapper);
        root = localInflater.inflate(R.layout.fragment_notes, container, false);
        addButton = root.findViewById(R.id.notes_floatingActionButton);
        linearLayout = root.findViewById(R.id.notes_linear_layout);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 140);
        // endregion

        notesListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemDialog(((TextView) v).getText().toString());
            }
        };

        layout.setMargins(40, 40, 40, 40);

        // region Programmatically added TextViews
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSingleTextView();
            }
        });
        // endregion
        return root;
    }
    public void showItemDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage("This will display the content from your note");

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "This will go to EditActivity", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        AlertDialog itemDialog = builder.create();
        itemDialog.show();
    }
    public void addSingleTextView() {
        TextView anotherTextView;
        anotherTextView = new TextView(getActivity());
        anotherTextView.setPaddingRelative(50, 40, 50, 40);
        anotherTextView.setBackgroundResource(R.drawable.notes_selector);
        anotherTextView.setText("Another one");
        anotherTextView.setTextSize(20);
        anotherTextView.setVisibility(View.VISIBLE);
        anotherTextView.setLayoutParams(layout);
        anotherTextView.setOnClickListener(notesListener);
        linearLayout.addView(anotherTextView);
    }
    public void addMultipleTextView() {
        TextView[] items = new TextView[5];
        for (int i = 0; i < 5; i++) {
            items[i] = new TextView(getActivity());
            items[i].setPaddingRelative(50, 40, 50, 40);
            items[i].setBackgroundResource(R.drawable.notes_selector);
            items[i].setText("Another one");
            items[i].setTextSize(20);
            items[i].setVisibility(View.VISIBLE);
            items[i].setLayoutParams(layout);
            items[i].setOnClickListener(notesListener);
            linearLayout.addView(items[i]);
        }
    }
}
