package com.example.takenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TakeNoteDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TakeNote.db";
    private static final String TABLE_1 = "users";
    private static final String TABLE_2 = "notes";
    private static final String TABLE_3 = "reminders";

    private static final String TB1_COL_1 = "username";
    private static final String TB1_COL_2 = "password";
    private static final String TB1_COL_3 = "first_name";
    private static final String TB1_COL_4 = "last_name";
    private static final String TB1_COL_5 = "location";
    private static final String TB1_COL_6 = "email";

    private static final String TB2_COL_1 = "note_no";
    private static final String TB2_COL_2 = "username";
    private static final String TB2_COL_3 = "note_title";
    private static final String TB2_COL_4 = "note_content";

    private static final String TB3_COL_1 = "reminder_no";
    private static final String TB3_COL_2 = "username";
    private static final String TB3_COL_3 = "reminder_title";
    private static final String TB3_COL_4 = "reminder_content";
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
        String createNotesTable = "CREATE TABLE " + TABLE_2 + " (" + TB2_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB2_COL_2 + " TEXT NOT NULL, " + TB2_COL_3 + " TEXT, " +
                TB2_COL_4 + " TEXT)";
        String createRemindersTable = "CREATE TABLE " + TABLE_3 + " (" + TB3_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB3_COL_2 + " TEXT NOT NULL, " + TB3_COL_3 + " TEXT, " +
                TB3_COL_4 + " TEXT)";

        db.execSQL(createUsersTable);
        System.out.println("Users Table Created");
        db.execSQL(createNotesTable);
        System.out.println("Notes Table Created");
//        db.execSQL(createRemindersTable);
//        System.out.println("Reminders Table Created");

        // DEFAULT SETTINGS
        db.execSQL("CREATE TABLE settings (dark_mode INTEGER, notifications INTEGER)");
        System.out.println("Settings Table Created");

        try {
            ContentValues cv = new ContentValues();
            cv.put("dark_mode", 0);
            cv.put("notifications", 0);
            long results = db.insert("settings", null, cv);
            if (results == -1)
                System.out.println("Not set");
            else
                System.out.println("Set");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void editSettings(int d, int n) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("dark_mode", d);
        cv.put("notifications", n);

        db.update("settings", cv, null, null);
        System.out.println("Settings updated in DB");
    }
    public int[] getSettings() {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM settings", null);
        cursor.moveToNext();
        int d = cursor.getInt(0);
        int n = cursor.getInt(1);

        return new int[] {d, n};
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
    public boolean insertNote(String username, String title, String content) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB2_COL_2, username);
        contentValues.put(TB2_COL_3, title);
        contentValues.put(TB2_COL_4, content);

        long insertResult = db.insert(TABLE_2, null, contentValues);
        System.out.println(insertResult);
        if (insertResult == -1) {
            System.out.println("Note not inserted");
            return false;
        }
        else {
            System.out.println("Note inserted");
            return true;
        }
    }
    public Cursor getNotes(String username) {
        db = this.getWritableDatabase();
        String sqlSelect = "SELECT " + TB2_COL_1 + ", " + TB2_COL_3 + ", " + TB2_COL_4 +
                " FROM " + TABLE_2 + " WHERE username = ?";
        Cursor userCursor = db.rawQuery(sqlSelect, new String[] {username});
        return userCursor;
    }
    public boolean deleteNote(int noteNo, String username) {
        db = this.getWritableDatabase();
        long results = db.delete(TABLE_2, TB2_COL_1 + " = ? AND " + TB3_COL_2 + " = ?", new String[] {Integer.toString(noteNo), username});

        if (results == -1) {
            System.out.println("NOT DELETED IN DB");
            return false;
        }
        else {
            System.out.println("DELETED IN DB");
            return true;
        }
    }
    public int getLastRowId() {
        db = this.getWritableDatabase();
        String sql = "SELECT MAX(" + TB2_COL_1 + ") FROM " + TABLE_2;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        int max = cursor.getInt(0);
        return max;
    }
    public boolean editNote(int noteNo, String username, String newTitle, String newContent) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB2_COL_3, newTitle);
        contentValues.put(TB2_COL_4, newContent);

        long results = db.update(TABLE_2, contentValues, TB2_COL_1 + " = ? AND " + TB2_COL_2 + " = ?", new String[] {Integer.toString(noteNo), username});

        if (results == -1) {
            System.out.println("Mission failed");
            return  false;
        }
        else {
            System.out.println("Mission success");
            return true;
        }
    }
}
