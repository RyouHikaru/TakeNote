package com.example.takenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TakeNoteDatabase extends SQLiteOpenHelper {
    // region Object and Variable
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
    private static final String TB1_COL_7 = "dark_mode";
    private static final String TB1_COL_8 = "notifications";

    private static final String TB2_COL_1 = "note_no";
    private static final String TB2_COL_2 = "username";
    private static final String TB2_COL_3 = "note_title";
    private static final String TB2_COL_4 = "note_content";
    private SQLiteDatabase db;
    // endregion

    public TakeNoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_1 + " (" + TB1_COL_1 + " TEXT PRIMARY KEY, " +
                TB1_COL_2 + " TEXT, " + TB1_COL_3 + " TEXT, " + TB1_COL_4 + " TEXT, " +
                TB1_COL_5 + " TEXT, " + TB1_COL_6 + " TEXT, " + TB1_COL_7 + " INTEGER, " +
                TB1_COL_8 + " INTEGER)";
        String createNotesTable = "CREATE TABLE " + TABLE_2 + " (" + TB2_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB2_COL_2 + " TEXT NOT NULL, " + TB2_COL_3 + " TEXT, " +
                TB2_COL_4 + " TEXT)";

        db.execSQL(createUsersTable);
//        System.out.println("Users Table Created");
        db.execSQL(createNotesTable);
//        System.out.println("Notes Table Created");
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
    public void editSettings(String un, int d, int n) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String whereClause = TB1_COL_1 + " = ?";
        cv.put(TB1_COL_7, d);
        cv.put(TB1_COL_8, n);

        db.update(TABLE_1, cv, whereClause, new String[] {un});
//        System.out.println("Settings updated in DB");
    }
    public int[] getUserSettings(String un) {
        db = this.getWritableDatabase();
        String sql = "SELECT " + TB1_COL_7 + ", " + TB1_COL_8 + " FROM " + TABLE_1 + " WHERE " + TB1_COL_1 + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {un});

        cursor.moveToNext();
        int d = cursor.getInt(0);
        int n = cursor.getInt(1);
        cursor.close();
        return new int[] {d, n};
    }
    public int getNotesLastRowId() {
        db = this.getWritableDatabase();
        String sql = "SELECT MAX(" + TB2_COL_1 + ") FROM " + TABLE_2;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        int max = cursor.getInt(0);
        cursor.close();
        return max;
    }
    public boolean signUp(String username, String password, String first_name, String last_name, String address, String email) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TB1_COL_1, username);
        contentValues.put(TB1_COL_2, password);
        contentValues.put(TB1_COL_3, first_name);
        contentValues.put(TB1_COL_4, last_name);
        contentValues.put(TB1_COL_5, address);
        contentValues.put(TB1_COL_6, email);
        contentValues.put(TB1_COL_7, 0);
        contentValues.put(TB1_COL_8, 1);

        long insertResult = db.insert(TABLE_1, null, contentValues);
        if (insertResult == -1) {
//            System.out.println("Failed to insert");
            return false;
        }
        else {
//            System.out.println("Successfully inserted");
            return true;
        }
    }
    public boolean isExistingUser(String username) {
        db = this.getWritableDatabase();
        String sqlSelect = "SELECT password FROM " + TABLE_1 + " WHERE username = ?";
        Cursor usernameCursor = db.rawQuery(sqlSelect, new String[] {username});

        if (usernameCursor.getCount() == 0) {
//            System.out.println("New user");
            usernameCursor.close();
            return false;
        }
        else {
//            System.out.println("Existing user");
            usernameCursor.close();
            return true;
        }
    }
    public boolean insertNote(String username, String title, String content) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB2_COL_2, username);
        contentValues.put(TB2_COL_3, title);
        contentValues.put(TB2_COL_4, content);

        long insertResult = db.insert(TABLE_2, null, contentValues);
//        System.out.println(insertResult);
        if (insertResult == -1) {
//            System.out.println("Note not inserted");
            return false;
        }
        else {
//            System.out.println("Note inserted");
            return true;
        }
    }
    public boolean deleteNote(int noteNo, String username) {
        db = this.getWritableDatabase();
        long results = db.delete(TABLE_2, TB2_COL_1 + " = ? AND " + TB2_COL_2 + " = ?", new String[] {Integer.toString(noteNo), username});

        if (results == -1) {
//            System.out.println("NOT DELETED IN DB");
            return false;
        }
        else {
//            System.out.println("DELETED IN DB");
            return true;
        }
    }
    public boolean editNote(int noteNo, String username, String newTitle, String newContent) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB2_COL_3, newTitle);
        contentValues.put(TB2_COL_4, newContent);

        long results = db.update(TABLE_2, contentValues, TB2_COL_1 + " = ? AND " + TB2_COL_2 + " = ?", new String[] {Integer.toString(noteNo), username});

        if (results == -1) {
//            System.out.println("Mission failed");
            return  false;
        }
        else {
//            System.out.println("Mission success");
            return true;
        }
    }
    public boolean editUser(String username, String newPw, String newFn, String newLn, String newAdd, String newEmail) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (!newPw.isEmpty())
            contentValues.put(TB1_COL_2, newPw);
        if (!newFn.isEmpty())
            contentValues.put(TB1_COL_3, newFn);
        if (!newLn.isEmpty())
            contentValues.put(TB1_COL_4, newLn);
        if (!newAdd.isEmpty())
            contentValues.put(TB1_COL_5, newAdd);
        if (!newEmail.isEmpty())
            contentValues.put(TB1_COL_6, newEmail);

        if (!(contentValues.size() == 0)) {
            long results = db.update(TABLE_1, contentValues, TB1_COL_1 + " = ?", new String[]{username});

            if (results == -1) {
//                System.out.println("User update failed");
                return false;
            } else {
//                System.out.println("User update success");
                return true;
            }
        }
        return true;
    }
    public String getUserPassword(String username) {
        db = this.getWritableDatabase();
        String pw = null;
        String sqlSelect = "SELECT password FROM " + TABLE_1 + " WHERE username = ?";
        Cursor userCursor = db.rawQuery(sqlSelect, new String[] {username});

        if (userCursor.getCount() == 0) {
            return pw;
        }
        else {
            while (userCursor.moveToNext()) {
                pw = userCursor.getString(0);
//                System.out.println(pw);
            }
        }
        userCursor.close();
        return pw;
    }
    public Cursor getUserDetails(String username) {
        db = this.getWritableDatabase();
        String sqlSelect = "SELECT " + TB1_COL_3 + ", " + TB1_COL_4 + ", " + TB1_COL_5 + ", " + TB1_COL_6 +
                " FROM " + TABLE_1 + " WHERE username = ?";
        Cursor userCursor = db.rawQuery(sqlSelect, new String[] {username});
        return userCursor;
    }
    public Cursor getNotes(String username) {
        db = this.getWritableDatabase();
        String sqlSelect = "SELECT " + TB2_COL_1 + ", " + TB2_COL_3 + ", " + TB2_COL_4 +
                " FROM " + TABLE_2 + " WHERE username = ?";
        Cursor userCursor = db.rawQuery(sqlSelect, new String[] {username});
        return userCursor;
    }
}
