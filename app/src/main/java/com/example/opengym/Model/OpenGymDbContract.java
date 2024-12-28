package com.example.opengym.Model;

public final class OpenGymDbContract {
    private OpenGymDbContract() {}

    public static final String SQL_CREATE_USERS_ENTRIES =
            "CREATE TABLE " + Users.TABLE_NAME + " (" +
                    Users.COLUMN_NAME + " TEXT PRIMARY KEY," +
                    Users.COLUMN_PASSWORD + " TEXT," +
                    Users.COLUMN_PREMIUM + " INTEGER DEFAULT 0);";

    public static final String SQL_CREATE_ROUTINES_ENTRIES =
            "CREATE TABLE " + Routines.TABLE_NAME + " (" +
                    Routines.COLUMN_NAME + " TEXT, " +
                    Routines.COLUMN_DESCRIPTION + " TEXT, " +
                    Routines.COLUMN_USERNAME + " TEXT, " +
                    "PRIMARY KEY (" + Routines.COLUMN_USERNAME + ", " + Routines.COLUMN_NAME + "), " +
                    "FOREIGN KEY (" + Routines.COLUMN_USERNAME + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_NAME + "));";

    public static final String SQL_CREATE_SESSIONS_ENTRIES =
            "CREATE TABLE " + Sessions.TABLE_NAME + " (" +
                    Sessions.COLUMN_NAME + " TEXT, " +
                    Sessions.COLUMN_RESTDURATION + " INTEGER, " +
                    Sessions.COLUMN_ROUTINENAME + " TEXT, " +
                    Sessions.COLUMN_DATE + " TEXT, " +
                    "PRIMARY KEY (" + Sessions.COLUMN_ROUTINENAME + ", " + Sessions.COLUMN_NAME + "), " +
                    "FOREIGN KEY (" + Sessions.COLUMN_ROUTINENAME + ") REFERENCES " + Routines.TABLE_NAME + "(" + Routines.COLUMN_NAME + "));";

    public static final String SQL_CREATE_ENTRIES =
            SQL_CREATE_USERS_ENTRIES + " " +
            SQL_CREATE_ROUTINES_ENTRIES + " " +
            SQL_CREATE_SESSIONS_ENTRIES;

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Users.TABLE_NAME + "; " +
            "DROP TABLE IF EXISTS " + Routines.TABLE_NAME + "; " +
            "DROP TABLE IF EXISTS " + Sessions.TABLE_NAME + ";";

    public static class Users {

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PREMIUM = "premium";
    }

    public static class Routines {

        public static final String TABLE_NAME = "routines";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_USERNAME = "userName";
    }

    public static class Sessions {

        public static final String TABLE_NAME = "sessions";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RESTDURATION = "restDuration";
        public static final String COLUMN_ROUTINENAME = "routineName";
        public static final String COLUMN_DATE = "date";
    }

    public static class RepExercise {

        public static final String TABLE_NAME = "repExercise";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_REPETITIONS = "repetitions";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_SESSIONNAME = "sessionName";
    }
}
