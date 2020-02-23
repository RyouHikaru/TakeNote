package com.example.navigationdrawerdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TakeNoteDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TakeNote.db";
    public static final String TABLE_1 = "users";
    public static final String TABLE_2 = "notes";
    public static final String TABLE_3 = "reminders";

    public static final String TB1_COL_1 = "username";
    public static final String TB1_COL_2 = "password";
    public static final String TB1_COL_3 = "first_name";
    public static final String TB1_COL_4 = "last_name";
    public static final String TB1_COL_5 = "address";
    public static final String TB1_COL_6 = "email";

    public static final String TB2_COL_1 = "note_no";
    public static final String TB2_COL_2 = "note_title";
    public static final String TB2_COL_3 = "note_content";

    public static final String TB3_COL_1 = "reminder_no";
    public static final String TB3_COL_2 = "reminder_title";
    public static final String TB3_COL_3 = "reminder_content";
    private SQLiteDatabase db;

    public TakeNoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_1 + " (" + TB1_COL_1 + " TEXT PRIMARY KEY, " +
                TB1_COL_2 + " TEXT, " + TB1_COL_3 + " TEXT, " + TB1_COL_4 + " TEXT, " +
                TB1_COL_5 + " TEXT, " + TB1_COL_6 + " TEXT)";
        String createNotesTable = "CREATE TABLE " + TABLE_2 + " (" + TB2_COL_1 + " NUMBER PRIMARY KEY, " +
                TB2_COL_2 + " TEXT, " + TB2_COL_3 + " TEXT)";
        String createRemindersTable = "CREATE TABLE " + TABLE_3 + " (" + TB3_COL_1 + " NUMBER PRIMARY KEY, " +
                TB3_COL_2 + " TEXT, " + TB3_COL_3 + " TEXT)";

        db.execSQL(createUsersTable);
        System.out.println("Users Table Created");
        db.execSQL(createNotesTable);
        System.out.println("Notes Table Created");
        db.execSQL(createRemindersTable);
        System.out.println("Reminders Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropUsersTable = "DROP TABLE IF EXISTS " + TABLE_1;
        String dropNotesTable = "DROP TABLE IF EXISTS " + TABLE_2;
        String dropRemindersTable = "DROP TABLE IF EXISTS " + TABLE_3;

        db.execSQL(dropUsersTable);
        db.execSQL(dropNotesTable);
        db.execSQL(dropRemindersTable);
    }
    public boolean signUp(String username, String password, String first_name, String last_name, String address, String email) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TB1_COL_1, username);
        contentValues.put(TB1_COL_2, password);
        contentValues.put(TB1_COL_3, first_name);
        contentValues.put(TB1_COL_4, last_name);
        contentValues.put(TB1_COL_5, address);
        contentValues.put(TB1_COL_6, email);

        System.out.println("UN: " + username + "\nPW: " + password +
                "\nFN: " + first_name + "\nLN: " + last_name + "\nADD: " +
                address + "\nEMAIL: " + email);

        long insertResult = db.insert(TABLE_1, null, contentValues);
        if (insertResult == -1) {
            System.out.println("Failed to insert");
            return false;
        }
        else {
            System.out.println("Successfully inserted");
            return true;
        }
    }
    public boolean isExistingUser(String username) {
        db = this.getWritableDatabase();
        String sqlSelect = "SELECT password FROM " + TABLE_1 + " WHERE username = ?";
        Cursor usernameCursor = db.rawQuery(sqlSelect, new String[] {username});

        if (usernameCursor.getCount() == 0) {
            System.out.println("New user");
            return false;
        }
        else {
            System.out.println("Existing user");
            return true;
        }
    }
    public String getUserPassword(String username) {
        db = this.getWritableDatabase();
        String pw = null;
        String sqlSelect = "SELECT password FROM " + TABLE_1 + " WHERE username = ?";
        Cursor userCursor = db.rawQuery(sqlSelect, new String[] {username});

        if (userCursor.getCount() == 0) {
            System.out.println("User not found");
            return pw;
        }
        else {
            while (userCursor.moveToNext()) {
                pw = userCursor.getString(0);
                System.out.println(pw);
            }
        }
        return pw;
    }
    public Cursor getUserDetails(String username) {
        db = this.getWritableDatabase();
        String sqlSelect = "SELECT " + TB1_COL_3 + ", " + TB1_COL_4 + ", " + TB1_COL_5 + ", " + TB1_COL_6 +
                " FROM " + TABLE_1 + " WHERE username = ?";
        Cursor userCursor = db.rawQuery(sqlSelect, new String[] {username});
        return userCursor;
    }
}
