package com.example.opengym.Model;

public final class OpenGymDbContract {
    private OpenGymDbContract() {}

    public static final String SQL_CREATE_USERS_ENTRIES =
            "CREATE TABLE " + UsersTable.TABLE_NAME + " (" +
                    UsersTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UsersTable.COLUMN_NAME + " TEXT UNIQUE, " +
                    UsersTable.COLUMN_PASSWORD + " TEXT, " +
                    UsersTable.COLUMN_PREMIUM + " INTEGER DEFAULT 0);";

    public static final String SQL_CREATE_LAST_LOGIN_ENTRIE =
            "CREATE TABLE " + LoginTable.TABLE_NAME + " (" +
                    LoginTable.COLUMN_NAME + " TEXT PRIMARY KEY, " +
                    "FOREIGN KEY (" + LoginTable.COLUMN_NAME + ") REFERENCES " + UsersTable.TABLE_NAME + "(" + UsersTable.COLUMN_NAME + ") ON DELETE CASCADE);";

    public static final String SQL_CREATE_ROUTINES_ENTRIES =
            "CREATE TABLE " + RoutinesTable.TABLE_NAME + " (" +
                    RoutinesTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RoutinesTable.COLUMN_NAME + " TEXT, " +
                    RoutinesTable.COLUMN_DESCRIPTION + " TEXT, " +
                    RoutinesTable.COLUMN_USERID + " INTEGER, " +
                    "FOREIGN KEY (" + RoutinesTable.COLUMN_USERID + ") REFERENCES " + UsersTable.TABLE_NAME + "(" + UsersTable.COLUMN_ID + ") ON DELETE CASCADE, " +
                    "UNIQUE (" + RoutinesTable.COLUMN_NAME + ", " + RoutinesTable.COLUMN_USERID + "));";

    public static final String SQL_CREATE_SESSIONS_ENTRIES =
            "CREATE TABLE " + SessionsTable.TABLE_NAME + " (" +
                    SessionsTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SessionsTable.COLUMN_NAME + " TEXT, " +
                    SessionsTable.COLUMN_RESTDURATION + " INTEGER, " +
                    SessionsTable.COLUMN_DATE + " TEXT, " +
                    SessionsTable.COLUMN_ROUTINEID + " INTEGER, " +
                    "FOREIGN KEY (" + SessionsTable.COLUMN_ROUTINEID + ") REFERENCES " + RoutinesTable.TABLE_NAME + "(" + RoutinesTable.COLUMN_ID + ") ON DELETE CASCADE, " +
                    "UNIQUE (" + SessionsTable.COLUMN_NAME + ", " + SessionsTable.COLUMN_DATE + ", " + SessionsTable.COLUMN_ROUTINEID + "));";

    public static final String SQL_CREATE_STRENGTHEXERCISE_ENTRIES =
            "CREATE TABLE " + StrengthExerciseTable.TABLE_NAME + " (" +
                    StrengthExerciseTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    StrengthExerciseTable.COLUMN_NAME + " TEXT, " +
                    StrengthExerciseTable.COLUMN_SETS + " INTEGER, " +
                    StrengthExerciseTable.COLUMN_REPETITIONS + " INTEGER, " +
                    StrengthExerciseTable.COLUMN_WEIGHT + " REAL, " +
                    StrengthExerciseTable.COLUMN_SESSIONID + " INTEGER, " +
                    "FOREIGN KEY (" + StrengthExerciseTable.COLUMN_SESSIONID + ") REFERENCES " + SessionsTable.TABLE_NAME + "(" + SessionsTable.COLUMN_ID + ") ON DELETE CASCADE, " +
                    "UNIQUE (" + StrengthExerciseTable.COLUMN_NAME + ", " + StrengthExerciseTable.COLUMN_SESSIONID + "));";

    public static final String SQL_CREATE_TIMEDEXERCISE_ENTRIES =
            "CREATE TABLE " + TimedExerciseTable.TABLE_NAME + " (" +
                    TimedExerciseTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TimedExerciseTable.COLUMN_NAME + " TEXT, " +
                    TimedExerciseTable.COLUMN_DURATION + " INTEGER, " +
                    TimedExerciseTable.COLUMN_SESSIONID + " INTEGER, " +
                    "FOREIGN KEY (" + TimedExerciseTable.COLUMN_SESSIONID + ") REFERENCES " + SessionsTable.TABLE_NAME + "(" + SessionsTable.COLUMN_ID + ") ON DELETE CASCADE, " +
                    "UNIQUE (" + TimedExerciseTable.COLUMN_NAME + ", " + TimedExerciseTable.COLUMN_SESSIONID + "));";

    public static final String SQL_DELETE_USERS_ENTRIES =
            "DROP TABLE IF EXISTS " + UsersTable.TABLE_NAME + ";";

    public static final String SQL_DELETE_LAST_LOGIN_ENTRIE=
            "DROP TABLE IF EXISTS " + LoginTable.TABLE_NAME + ";";

    public static final String SQL_DELETE_ROUTINES_ENTRIES =
            "DROP TABLE IF EXISTS " + RoutinesTable.TABLE_NAME + ";";

    public static final String SQL_DELETE_SESSIONS_ENTRIES =
            "DROP TABLE IF EXISTS " + SessionsTable.TABLE_NAME + ";";

    public static final String SQL_DELETE_STRENGTHEXERCISE_ENTRIES =
            "DROP TABLE IF EXISTS " + StrengthExerciseTable.TABLE_NAME + ";";

    public static final String SQL_DELETE_TIMEDEXERCISE_ENTRIES =
            "DROP TABLE IF EXISTS " + TimedExerciseTable.TABLE_NAME + ";";


    public static class UsersTable {

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PREMIUM = "premium";
    }

    public static class RoutinesTable {

        public static final String TABLE_NAME = "routines";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_USERID = "userId";
    }

    public static class SessionsTable {

        public static final String TABLE_NAME = "sessions";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RESTDURATION = "restDuration";
        public static final String COLUMN_ROUTINEID = "routineID";
        public static final String COLUMN_DATE = "date";
    }

    public static class StrengthExerciseTable {

        public static final String TABLE_NAME = "strengthExercise";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_REPETITIONS = "repetitions";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_SESSIONID = "sessionId";
    }

    public static class TimedExerciseTable {

        public static final String TABLE_NAME = "timedExercise";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_SESSIONID = "sessionId";
    }

    public static class LoginTable {

        public static final String TABLE_NAME = "lastLogin";

        public static final String COLUMN_NAME = "userName";
    }
}