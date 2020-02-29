package com.example.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    // region Object and Variables
    private static int TIME_DELAY = 750;
    private Button loginButton, cancelButton;
    private TextView signUpView;
    private EditText unEditText, pwEditText;
    private static Intent intent;
    private static TakeNoteDatabase myDb;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            System.exit(0);
        }

        // region XML mapping
        loginButton = findViewById(R.id.loginButton);
        cancelButton = findViewById(R.id.cancelButton);
        signUpView = findViewById(R.id.signUpView);
        unEditText = findViewById(R.id.unEditText);
        pwEditText = findViewById(R.id.pwEditText);
        myDb = new TakeNoteDatabase(this);
        // endregion

        // region loginButton listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String un = unEditText.getText().toString();
                String pw = pwEditText.getText().toString();

                if (un.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    resetField();
                    unEditText.requestFocus();
                    return;
                }
                if (myDb.isExistingUser(un) == false) {
                    Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    pwEditText.setText("");
                    unEditText.requestFocus();
                    return;
                }
                else {
                    String pwKey = myDb.getUserPassword(un);

                    if (pw.equals(pwKey)) {
                        Toast.makeText(LoginActivity.this, "Logging in", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("UN", un);
                                startActivity(intent);
                            }
                        }, TIME_DELAY);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        pwEditText.setText("");
                        pwEditText.requestFocus();
                        return;
                    }
                }
            }
        });
        // endregion

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Closing", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        signUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    public void resetField() {
        unEditText.setText("");
        pwEditText.setText("");
    }
}
