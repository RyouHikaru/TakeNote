package com.example.takenote.ui.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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

public class NotesFragment extends Fragment {
    private View root;
    private TextView item1TextView, item2TextView, item3TextView;
    private NotesViewModel notesViewModel;
    private View.OnClickListener notesListener;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams layout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
//        root = inflater.inflate(R.layout.fragment_notes, container, false);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoteFragmentStyle);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        root = localInflater.inflate(R.layout.fragment_notes, container, false);
        item1TextView = root.findViewById(R.id.item1textView);
        item2TextView = root.findViewById(R.id.item2TextView);
        item3TextView = root.findViewById(R.id.item3TextView);

        notesListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemDialog(((TextView) v).getText().toString());
            }
        };

        item1TextView.setOnClickListener(notesListener);
        item2TextView.setOnClickListener(notesListener);
        item3TextView.setOnClickListener(notesListener);

        // region Programmatically added TextViews
        linearLayout = root.findViewById(R.id.notes_linear_layout);
        layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 140);
        layout.setMargins(40, 40, 40, 40);

        addTextView();
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
    public void addTextView() {
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
