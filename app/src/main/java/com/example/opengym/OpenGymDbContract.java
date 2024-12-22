package com.example.opengym;

import android.provider.BaseColumns;

public final class OpenGymDbContract {
    private OpenGymDbContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Users.TABLE_NAME + " (" +
                    Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Users.COLUMN_NAME + " TEXT," +
                    Users.COLUMN_PASSWORD + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Users.TABLE_NAME;

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PASSWORD = "password";

    }
}
