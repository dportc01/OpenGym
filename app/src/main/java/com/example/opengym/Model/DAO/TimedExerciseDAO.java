package com.example.opengym.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.Entities.ExerciseFactory;
import com.example.opengym.Model.Entities.TimedExercise;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

import java.util.ArrayList;

public class TimedExerciseDAO implements GenericDAO<TimedExercise> {

    private final OpenGymDbHelper dbHelper;
    private SQLiteDatabase db;

    public  TimedExerciseDAO(Context context) {
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    @Override
    public long create(TimedExercise entity, long parentId) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.TimedExerciseTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.TimedExerciseTable.COLUMN_DURATION, entity.getTime());
        values.put(OpenGymDbContract.TimedExerciseTable.COLUMN_SESSIONID, parentId);

        long id = db.insertOrThrow(OpenGymDbContract.TimedExerciseTable.TABLE_NAME, null, values);
        entity.setId(id);
        return id;
    }

    @Override
    public int delete(long id) {

        db = dbHelper.getWritableDatabase();

        String selection = OpenGymDbContract.TimedExerciseTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(OpenGymDbContract.TimedExerciseTable.TABLE_NAME, selection, selectionArgs);
    }

    @SuppressLint("Range")
    @Override
    public ArrayList<TimedExercise> readAll(long parentId) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.TimedExerciseTable.COLUMN_ID,
                OpenGymDbContract.TimedExerciseTable.COLUMN_NAME,
                OpenGymDbContract.TimedExerciseTable.COLUMN_DURATION
        };

        String selection = OpenGymDbContract.TimedExerciseTable.COLUMN_SESSIONID + " = ?";
        String[] selectionArgs = {String.valueOf(parentId)};

        Cursor cursor = db.query(
                OpenGymDbContract.TimedExerciseTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<TimedExercise> ExerciseList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.TimedExerciseTable.COLUMN_NAME));
            int duration = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.TimedExerciseTable.COLUMN_DURATION));
            long id = cursor.getLong(cursor.getColumnIndex(OpenGymDbContract.TimedExerciseTable.COLUMN_ID));
            ExerciseList.add((TimedExercise) ExerciseFactory.createExercise(name, duration, id));
        }

        cursor.close();

        return ExerciseList;
    }

    @Override
    public int update(TimedExercise entity, long id) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.TimedExerciseTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.TimedExerciseTable.COLUMN_DURATION, entity.getTime());

        String selection = OpenGymDbContract.TimedExerciseTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        entity.setId(id);

        return db.update(
                OpenGymDbContract.TimedExerciseTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    @Override
    public void closeConnection() {
        dbHelper.close();
    }

    /**
     * Removes all the exercises whose sessionID = <code>parentID</code>
     * @param parentId id of the session
     */
    public void deleteAll(long parentId) {

        db = dbHelper.getWritableDatabase();

        String selection = OpenGymDbContract.TimedExerciseTable.COLUMN_SESSIONID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(parentId)};

        db.delete(OpenGymDbContract.TimedExerciseTable.TABLE_NAME, selection, selectionArgs);
    }
}
