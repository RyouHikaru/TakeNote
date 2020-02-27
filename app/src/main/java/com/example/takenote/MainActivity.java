package com.example.takenote;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.app.AlertDialog;
import androidx.core.view.GravityCompat;

import com.example.takenote.ui.archive.ArchiveFragment;
import com.example.takenote.ui.notes.NotesFragment;
import com.example.takenote.ui.reminder.RemindersFragment;
import com.example.takenote.ui.user_profile.UserProfileFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int clickedNavItem;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DialogInterface.OnShowListener dialogListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_note);
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.notes);
        }

        dialogListener = new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Window view = ((android.app.AlertDialog)dialog).getWindow();
                view.setBackgroundDrawableResource(R.color.colorPrimary);

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
        };

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {}
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                switch (clickedNavItem) {
                    case R.id.nav_note:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
                                replace(R.id.fragment_container, new NotesFragment()).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.notes);

                        break;
                    case R.id.nav_user_profile:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
                                replace(R.id.fragment_container, new UserProfileFragment()).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_user_profile);
                        break;
                    case R.id.nav_reminder:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
                                replace(R.id.fragment_container, new RemindersFragment()).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.reminders);
                        break;
                    case R.id.nav_archive:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
                                replace(R.id.fragment_container, new ArchiveFragment()).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.archive);
                        break;
                    case R.id.nav_share:
                        showShareDialog();
//                        Toast.makeText(MainActivity.this, "Sharing is caring", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_logout:
                        showLogoutDialog();
                        break;
                }
                clickedNavItem = 0;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                clickedNavItem = R.id.nav_logout;
                break;
            case R.id.nav_note:
                clickedNavItem = R.id.nav_note;
                break;
            case R.id.nav_user_profile:
                clickedNavItem = R.id.nav_user_profile;
                break;
            case R.id.nav_reminder:
                clickedNavItem = R.id.nav_reminder;
                break;
            case R.id.nav_archive:
                clickedNavItem = R.id.nav_archive;
                break;
            case R.id.nav_share:
                clickedNavItem = R.id.nav_share;
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.DialogStyle);
        alertDialogBuilder.setTitle("Confirm Logout?");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"No", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(dialogListener);
        alertDialog.show();
    }
    public void showShareDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.DialogStyle);
        alertDialogBuilder.setTitle("Share mo lang?");
        alertDialogBuilder.setMessage("Are you sure you want to share?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this,"Shared", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"Not shared", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(dialogListener);
        alertDialog.show();
    }
}
