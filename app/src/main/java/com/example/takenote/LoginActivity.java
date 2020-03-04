package com.example.takenote;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
    private static int TIME_DELAY = 600;
    private Button loginButton, cancelButton;
    private TextView signUpView;
    private EditText unEditText, pwEditText;
    private static Intent intent;
    private static TakeNoteDatabase myDb;
    private static int[] settings;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    settings = myDb.getUserSettings(un);
                    String pwKey = myDb.getUserPassword(un);

                    if (pw.equals(pwKey)) {
                        Toast.makeText(LoginActivity.this, "Logging in", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (settings[1] == 1) {
                                    Intent i = new Intent();
                                    PendingIntent pi = PendingIntent.getActivity(LoginActivity.this, 69, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                    Notification.Builder builder = new Notification.Builder(LoginActivity.this);
                                    builder.setSmallIcon(R.drawable.ic_notifications_white_24dp)
                                            .setContentTitle("Take Note")
                                            .setContentText("Hi there! What do you want to share?")
                                            .setWhen(System.currentTimeMillis())
                                            .setAutoCancel(true)
                                            .setContentIntent(pi)
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setDefaults(Notification.DEFAULT_ALL);
                                    NotificationManager nm = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                                    nm.notify(0, builder.build());
                                }
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

                if (settings[1] == 1) {
                    Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(LoginActivity.this, 69, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification.Builder builder = new Notification.Builder(LoginActivity.this);
                    builder.setSmallIcon(R.drawable.ic_notifications_white_24dp)
                            .setContentTitle("You can comeback anytime!")
                            .setContentText("Click to Login. Swipe to ignore.")
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true)
                            .setContentIntent(pi)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_ALL);
                    NotificationManager nm = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(0, builder.build());
                }
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
