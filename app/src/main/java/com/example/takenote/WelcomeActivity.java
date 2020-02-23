package com.example.takenote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.takenote.R;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private TakeNoteDatabase myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        myDb = new TakeNoteDatabase(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
