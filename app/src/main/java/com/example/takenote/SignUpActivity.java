package com.example.takenote;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class SignUpActivity extends AppCompatActivity {
    private static int TIME_DELAY = 2000;
    private Button signupButton, cancelButton;
    private EditText unEditText, pwEditText, fnEditText, lnEditText, addEditText, emailEditText;
    private TakeNoteDatabase myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        unEditText = findViewById(R.id.su_unEditText);
        pwEditText = findViewById(R.id.su_pwEditText);
        fnEditText = findViewById(R.id.firstNameEditText);
        lnEditText = findViewById(R.id.lastnameEditText);
        addEditText = findViewById(R.id.addressEditText);
        emailEditText = findViewById(R.id.emailEditText);
        myDb = new TakeNoteDatabase(this);

        signupButton = (Button) findViewById(R.id.signUpButton);
        cancelButton = (Button) findViewById(R.id.signUpCancelButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = unEditText.getText().toString();
                String pw = pwEditText.getText().toString();
                String fn = fnEditText.getText().toString();
                String ln = lnEditText.getText().toString();
                String a = addEditText.getText().toString();
                String em = emailEditText.getText().toString();

                if (validUsername(un) == false) {
                    unEditText.requestFocus();
                    return;
                }
                if (myDb.isExistingUser(un) == true) {
                    Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (validPassword(pw) == false) {
                    pwEditText.requestFocus();
                    return;
                }
                if (validName(fn) == false) {
                    fnEditText.requestFocus();
                    return;
                }
                if (validName(ln) == false) {
                    lnEditText.requestFocus();
                    return;
                }
                if (a.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
                    addEditText.requestFocus();
                    return;
                }
                if (em.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter e-mail", Toast.LENGTH_SHORT).show();
                    emailEditText.requestFocus();
                    return;
                }

                signupAlert(un, pw, fn, ln, a, em);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean validUsername(String username) {
        boolean hasSpecial = false;
        boolean valid = false;

//        System.out.println(username.length());
        if (username.length() <= 10 && username.length() >= 5) {
            for (int i = 0; i < username.length(); i++) {
//                System.out.println(username.charAt(i));
                if ((username.charAt(i) >= 33 && username.charAt(i) <= 47) ||
                        (username.charAt(i) >= 58 && username.charAt(i) <= 64) ||
                        (username.charAt(i) >= 91 && username.charAt(i) <= 96) ||
                        (username.charAt(i) >= 123 && username.charAt(i) <= 126)) {
                    hasSpecial = true;
                    break;
                }
            }
//            System.out.println("UN hasSpecial: " + hasSpecial);
        }
        else {
            Toast.makeText(this, "Username should contain 5 - 10 characters only", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }

        if (hasSpecial == false) {
            valid = true;
        }
        else {
            Toast.makeText(this, "Username cannot have special characters", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
    public boolean validPassword(String password) {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;
        boolean hasNumber = false;
        boolean valid = false;

//        System.out.println(password.length());
        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
//                System.out.println(password.charAt(i));
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
//            System.out.println("PW hasUpper: " + hasUpper + "\nhasLower: " + hasLower + "\nhasSpecial: " + hasSpecial + "\nhasNumber: " + hasNumber);
        }
        else {
            Toast.makeText(this, "Password should have a minimum of 8 characters", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }

        if (hasUpper == true && hasLower == true && hasSpecial == true && hasNumber == true) {
            valid = true;
        }
        else {
            Toast.makeText(this, "Password does not meet the criteria", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
    public boolean validName(String name) {
        boolean hasSpecial = false;
        boolean hasNumber = false;
        boolean valid = true;

        if (name.length() == 0) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
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
            Toast.makeText(this, "Names shouldn't have numbers or special characters", Toast.LENGTH_SHORT).show();
            return valid;
        }
        else {
            return valid;
        }
    }
    public void signupAlert(final String un, final String pw, final String fn, final String ln, final String a, final String em) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.DialogStyle);
        alertDialogBuilder.setTitle("Signing up");
        alertDialogBuilder.setMessage("Confirm to signup?");

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isInserted = myDb.signUp(un, pw, fn, ln, a, em);

                if (isInserted == true) {
                    Toast.makeText(SignUpActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, TIME_DELAY);
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                }
               return;
            }
        });
        alertDialogBuilder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                // Add or create your own background drawable for AlertDialog window
                Window view = ((android.app.AlertDialog)dialog).getWindow();
                view.setBackgroundDrawableResource(R.color.colorPrimary);

                // Customize POSITIVE and NEUTRAL buttons.
                Button positiveButton = ((android.app.AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.default_whitish_color));
                positiveButton.invalidate();

                Button neutralButton = ((android.app.AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                neutralButton.setTextColor(getResources().getColor(R.color.default_whitish_color));
                neutralButton.invalidate();

                Button negativeButton = ((android.app.AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getResources().getColor(R.color.default_whitish_color));
                negativeButton.invalidate();
            }
        });
        alertDialog.show();
    }
}
