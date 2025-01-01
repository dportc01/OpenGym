package com.example.opengym.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenGymDbHelper extends SQLiteOpenHelper {
    private static OpenGymDbHelper instance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OpenGym.db";

    private OpenGymDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized OpenGymDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new OpenGymDbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql_create_entries(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sql_delete_entries(db);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    public void sql_create_entries(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        db.execSQL(OpenGymDbContract.SQL_CREATE_USERS_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_CREATE_ROUTINES_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_CREATE_SESSIONS_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_CREATE_TIMEDEXERCISE_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_CREATE_STRENGTHEXERCISE_ENTRIES);
    }

    public void sql_delete_entries(SQLiteDatabase db) {
        db.execSQL(OpenGymDbContract.SQL_DELETE_STRENGTHEXERCISE_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_DELETE_TIMEDEXERCISE_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_DELETE_SESSIONS_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_DELETE_ROUTINES_ENTRIES);
        db.execSQL(OpenGymDbContract.SQL_DELETE_USERS_ENTRIES);
    }
}
