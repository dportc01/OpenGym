package com.example.opengym.Model;

public final class OpenGymDbContract {
    private OpenGymDbContract() {}

    public static final String SQL_CREATE_USERS_ENTRIES =
            "CREATE TABLE " + UsersTable.TABLE_NAME + " (" +
                    UsersTable.COLUMN_NAME + " TEXT PRIMARY KEY," +
                    UsersTable.COLUMN_PASSWORD + " TEXT," +
                    UsersTable.COLUMN_PREMIUM + " INTEGER DEFAULT 0);";

    public static final String SQL_CREATE_ROUTINES_ENTRIES =
            "CREATE TABLE " + RoutinesTable.TABLE_NAME + " (" +
                    RoutinesTable.COLUMN_NAME + " TEXT, " +
                    RoutinesTable.COLUMN_DESCRIPTION + " TEXT, " +
                    RoutinesTable.COLUMN_USERNAME + " TEXT, " +
                    "PRIMARY KEY (" + RoutinesTable.COLUMN_USERNAME + ", " + RoutinesTable.COLUMN_NAME + "), " +
                    "FOREIGN KEY (" + RoutinesTable.COLUMN_USERNAME + ") REFERENCES " + UsersTable.TABLE_NAME + "(" + UsersTable.COLUMN_NAME + "));";

    public static final String SQL_CREATE_SESSIONS_ENTRIES =
            "CREATE TABLE " + SessionsTable.TABLE_NAME + " (" +
                    SessionsTable.COLUMN_NAME + " TEXT, " +
                    SessionsTable.COLUMN_RESTDURATION + " INTEGER, " +
                    SessionsTable.COLUMN_ROUTINENAME + " TEXT, " +
                    SessionsTable.COLUMN_DATE + " TEXT, " +
                    "PRIMARY KEY (" + SessionsTable.COLUMN_ROUTINENAME + ", " + SessionsTable.COLUMN_NAME + "), " +
                    "FOREIGN KEY (" + SessionsTable.COLUMN_ROUTINENAME + ") REFERENCES " + RoutinesTable.TABLE_NAME + "(" + RoutinesTable.COLUMN_NAME + "));";

    public static final String SQL_CREATE_STRENGTHEXERCISE_ENTRIES =
            "CREATE TABLE " + StrengthExerciseTable.TABLE_NAME + " (" +
                    StrengthExerciseTable.COLUMN_NAME + " TEXT, " +
                    StrengthExerciseTable.COLUMN_SETS + " INTEGER, " +
                    StrengthExerciseTable.COLUMN_REPETITIONS + " INTEGER, " +
                    StrengthExerciseTable.COLUMN_WEIGHT + " INTEGER, " +
                    StrengthExerciseTable.COLUMN_SESSIONNAME + " TEXT, " +
                    "PRIMARY KEY (" + StrengthExerciseTable.COLUMN_SESSIONNAME + ", " + StrengthExerciseTable.COLUMN_NAME + "), " +
                    "FOREIGN KEY (" + StrengthExerciseTable.COLUMN_SESSIONNAME + ") REFERENCES " + SessionsTable.TABLE_NAME + "(" + SessionsTable.COLUMN_NAME + "));";

    public static final String SQL_CREATE_TIMEDEXERCISE_ENTRIES =
            "CREATE TABLE " + TimedExerciseTable.TABLE_NAME + " (" +
                    TimedExerciseTable.COLUMN_NAME + " TEXT, " +
                    TimedExerciseTable.COLUMN_DURATION + " INTEGER, " + // Assuming duration is in seconds or minutes, represented as INTEGER
                    TimedExerciseTable.COLUMN_SESSIONNAME + " TEXT, " +
                    "PRIMARY KEY (" + TimedExerciseTable.COLUMN_SESSIONNAME + ", " + TimedExerciseTable.COLUMN_NAME + "), " +
                    "FOREIGN KEY (" + TimedExerciseTable.COLUMN_SESSIONNAME + ") REFERENCES " + SessionsTable.TABLE_NAME + "(" + SessionsTable.COLUMN_NAME + "));";

    public static final String SQL_DELETE_USERS_ENTRIES =
            "DROP TABLE IF EXISTS " + UsersTable.TABLE_NAME + ";";

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

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_PREMIUM = "premium";
    }

    public static class RoutinesTable {

        public static final String TABLE_NAME = "routines";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_USERNAME = "userName";
    }

    public static class SessionsTable {

        public static final String TABLE_NAME = "sessions";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RESTDURATION = "restDuration";
        public static final String COLUMN_ROUTINENAME = "routineName";
        public static final String COLUMN_DATE = "date";
    }

    public static class StrengthExerciseTable {

        public static final String TABLE_NAME = "strengthExercise";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_REPETITIONS = "repetitions";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_SESSIONNAME = "sessionName";
    }

    public static class TimedExerciseTable {

        public static final String TABLE_NAME = "timedExercise";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_SESSIONNAME = "sessionName";
    }
}
