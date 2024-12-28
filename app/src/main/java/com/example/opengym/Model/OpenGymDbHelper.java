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
            instance = new OpenGymDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OpenGymDbContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(OpenGymDbContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
