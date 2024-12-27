package com.example.opengym.Model;

import android.provider.BaseColumns;

public final class OpenGymDbContract {
    private OpenGymDbContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Users.TABLE_NAME + " (" +
                    Users.COLUMN_NAME + " TEXT PRIMARY KEY," +
                    Users.COLUMN_PASSWORD + " TEXT," +
                    Users.COLUMN_EMAIL + " TEXT," +
                    Users.COLUMN_PREMIUM + " INTEGER DEFAULT 0)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Users.TABLE_NAME;

    public static class Users {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PREMIUM = "premium";
    }
}
