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
     * @param parentId id of the parent entity that contains the entity to be updated
     * can be <code>null</code> if it doesn't have it
     * @return row ID or -1 if an error curred
     */
    @Override
    public long create(Session entity, long parentId) {

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
        values.put(OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID, parentId);

        long entityId = db.insertOrThrow(OpenGymDbContract.SessionsTable.TABLE_NAME, null, values);
        entity.setId(entityId);

        return entityId;
    }

    /**
     * Deletes all the entries for a specific session name (independent of date)
     * @param id of the entry to delete
     * @return numbers of rows affected by the operation
     */
    @Override
    public int delete(long id) {
        db = dbHelper.getWritableDatabase();

        String selection = OpenGymDbContract.SessionsTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(OpenGymDbContract.SessionsTable.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Read all the no date entries that have the id as a foreign key
     * @param parentId id that is used as a foreign key in the entries of another table
     * @return a list of entries
     */
    @Override
    @SuppressLint("Range")
    public ArrayList<Session> readAll(long parentId) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.SessionsTable.COLUMN_NAME,
                OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION,
                OpenGymDbContract.SessionsTable.COLUMN_ID
        };

        String selection = OpenGymDbContract.SessionsTable.COLUMN_DATE + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ?";

        String[] selectionArgs = {NullDate ,String.valueOf(parentId)};

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
            int id = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_ID));
            SessionList.add(new Session(name, null, restDuration, id));
        }

        cursor.close();

        return SessionList;
    }


    /**
     * Update all the entries with the data from entity
     * and assign the new entity the id
     * @param entity new entity with modified values
     * @param id of the entry to be updated
     * @return number of rows affected by the operation
     */
    @Override
    public int update(Session entity, long id) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.SessionsTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION, entity.getRestDuration());

        String selection = OpenGymDbContract.SessionsTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        entity.setId(id);

        return  db.update(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                values,
                selection,
                selectionArgs)
                + updateDateEntries(entity, id);
    }

    /**
     *
     * @param id of the no date entry
     * @return that contains the entry name on 0, and the parentId on 1
     */
    @SuppressLint("Range")
    private ArrayList<String> getInfoForDateEntries(long id) {

        //Get the name and routineId to identify the group of sessions

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.SessionsTable.COLUMN_NAME,
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID
        };

        String selection = OpenGymDbContract.SessionsTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<String> strings = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            strings.add(cursor.getString(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_NAME)));
            strings.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID))));
        }
        else {
            return null;
        }

        cursor.close();

        return strings;
    }

    private int updateDateEntries(Session entity, long id) {

        ArrayList<String> info = getInfoForDateEntries(id);

        if (info == null) {
            return 0;
        }

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.SessionsTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION, entity.getRestDuration());

        String selectionUpdate = OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_DATE + " NOT LIKE ?";

        String[] selectionArgsUpdate = {info.get(1), info.get(0), NullDate};

        return  db.update(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                values,
                selectionUpdate,
                selectionArgsUpdate);
    }

    @Override
    public void closeConnection() {
        dbHelper.close();
    }

    /**
     * @param id of the session with NoDate
     * @return Return all the date sessions for a session name or <code>null<code/> if they don't exist
     */
    @SuppressLint("Range")
    public ArrayList<Session> getAllPast(long id) {

        ArrayList<String> info = getInfoForDateEntries(id);

        if (info == null) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.SessionsTable.COLUMN_NAME,
                OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION,
                OpenGymDbContract.SessionsTable.COLUMN_DATE,
                OpenGymDbContract.SessionsTable.COLUMN_ID
        };

        String selection = OpenGymDbContract.SessionsTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_ROUTINEID + " LIKE ? AND " +
                OpenGymDbContract.SessionsTable.COLUMN_DATE + " NOT LIKE ?";


        String[] selectionArgs = {info.get(0) ,info.get(1), NullDate};

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
                String name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_NAME));
                int restDuration = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_RESTDURATION));
                Date date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_DATE)));
                id = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_ID));
                SessionList.add(new Session(name, date, restDuration, id));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        cursor.close();

        return SessionList;
    }

}
