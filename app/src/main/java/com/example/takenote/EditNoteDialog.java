package com.example.takenote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class EditNoteDialog extends AppCompatDialogFragment {
    private AlertDialog.Builder builder;
    private EditText titleEditText, contentEditText;
    private ContextThemeWrapper contextThemeWrapper;
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt("ID");
//        System.out.println("EDialog recieved ID: " + id); CLEAR
        builder = new AlertDialog.Builder(getActivity());
        contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoteFragmentStyle);
        LayoutInflater inflater = getActivity().getLayoutInflater().cloneInContext(contextThemeWrapper);
        view = inflater.inflate(R.layout.input_dialog_layout, null);

        titleEditText = view.findViewById(R.id.n_titleEditText);
        contentEditText = view.findViewById(R.id.n_contentEditText);

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
                        Intent i = new Intent().putExtra("TVID", id).putExtra("TITLE", title).putExtra("CONTENT", content);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                        dismiss();
                    }

                    });

            AlertDialog thisDialog = builder.create();

            thisDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    // Add or create your own background drawable for AlertDialog window
                    Window view = ((AlertDialog) dialog).getWindow();
                    view.setBackgroundDrawableResource(R.color.colorPrimary);

                    // Customize POSITIVE and NEUTRAL buttons.
                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.default_whitish_color));
                    positiveButton.invalidate();

                    Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.default_whitish_color));
                    negativeButton.invalidate();
                }
            });

        return thisDialog;
    }
}
