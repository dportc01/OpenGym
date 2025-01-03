package com.example.opengym.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.Entities.Session;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SessionDAO implements GenericDAO<Session> {

    private static final String NullDate = "NoDate";

    private final OpenGymDbHelper dbHelper;
    private SQLiteDatabase db;

    public SessionDAO(Context context) {
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    /**
     * Create a session (independent of date)
     * @param entity new entity to add to the database
     * @param parentName name of the parent entity that contains the entity to be updated
     * can be <code>null</code> if it doesn't have it
     * @return row ID or -1 if an error curred
     */
    @Override
    public long create(Session entity, String parentName) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.SessionsTable.COLUMN_NAME, entity.getName());
        if (entity.getDate() == null) {
            values.put(OpenGymDbContract.SessionsTable.COLUMN_DATE, NullDate);
        }
        else {
            values.put(OpenGymDbContract.SessionsTable.COLUMN_DATE, entity.getDate().toString());
        }
        values.put(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION, entity.getRestDuration());
        values.put(OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID, getRoutineId(parentName));

        return db.insertOrThrow(OpenGymDbContract.SessionsTable.TABLE_NAME, null, values);
    }

    /**
     * Deletes all the entries for a specific session name (independent of date)
     * @param name primary key of the entry
     * @param parentName Foreign key that is used in the primary key,
     * can be <code>null</code> if it doesn't have it
     * @return numbers of rows affected by the operation
     */
    @Override
    public int delete(String name, String parentName) {

        String selection = OpenGymDbContract.SessionsTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getRoutineId(parentName))};

        return db.delete(OpenGymDbContract.SessionsTable.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Return the session for the no date entry
     * @param name primary key of the entry
     * @param parentName Foreign key that is used in the primary key,
     * can be null if it doesn't have it
     * @return the entry as a Session object or <code>null</code> if the entry doesn't exist
     */
    @SuppressLint("Range")
    @Override
    public Session read(String name, String parentName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.SessionsTable.COLUMN_NAME,
                OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION
        };

        String selection = OpenGymDbContract.SessionsTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_DATE + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getRoutineId(parentName)), NullDate};

        Cursor cursor = db.query(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int restDuration;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_NAME));
            restDuration = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION));
        }
        else {
            return null;
        }

        cursor.close();

        return new Session(name, null, restDuration, null);
    }

    /**
     * Update all the entries
     * @param entity new entity with modified values
     * @param parentName Foreign key that is used in the primary key,
     * can be null if it doesn't have it
     * @return number of rows affected by the operation
     */
    @Override
    public int update(Session entity, String name, String parentName) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.SessionsTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION, entity.getRestDuration());

        String selection = OpenGymDbContract.SessionsTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getRoutineId(parentName))};

        return db.update(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    @Override
    public void closeConnection() {
        dbHelper.close();
    }

    /**
     * @param parentName
     * @return Return all the no date sessions
     */
    @SuppressLint("Range")
    public ArrayList<Session> getAll(String parentName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.SessionsTable.COLUMN_NAME,
                OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION
        };

        String selection = OpenGymDbContract.SessionsTable.COLUMN_DATE + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ?";

        String[] selectionArgs = {NullDate ,String.valueOf(getRoutineId(parentName))};

        Cursor cursor = db.query(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<Session> SessionList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_NAME));
            int restDuration = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION));
            SessionList.add(new Session(name, null, restDuration, null));
        }

        cursor.close();

        return SessionList;
    }

    /**
     * @param parentName name of the parent routine
     * @param name name of the session
     * @return Return all the date sessions for a session name
     */
    @SuppressLint("Range")
    public ArrayList<Session> getAllPast(String parentName, String name) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.SessionsTable.COLUMN_NAME,
                OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION,
                OpenGymDbContract.SessionsTable.COLUMN_DATE
        };

        String selection = OpenGymDbContract.SessionsTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_DATE + " NOT LIKE ?";


        String[] selectionArgs = {name ,String.valueOf(getRoutineId(parentName)), NullDate};

        Cursor cursor = db.query(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<Session> SessionList = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_NAME));
                int restDuration = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION));
                Date date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_DATE)));
                SessionList.add(new Session(name, date, restDuration, null));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        cursor.close();

        return SessionList;
    }

    @SuppressLint("Range")
    private int getRoutineId(String routineName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_ID
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " LIKE ?";

        String[] selectionArgs = {routineName};

        Cursor cursor = db.query(
                OpenGymDbContract.RoutinesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_ID));
            cursor.close();
            return id;
        }
        else {
            return -1;
        }
    }

}
