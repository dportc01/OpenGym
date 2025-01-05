package com.example.opengym.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.Entities.StrengthExercise;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

import java.util.ArrayList;

/*
public class StrengthExerciseDao implements GenericDAO<StrengthExercise> {

    private final OpenGymDbHelper dbHelper;
    private SQLiteDatabase db;

    public StrengthExerciseDao(Context context){
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    @Override
    public long create(StrengthExercise entity, String parentName) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS, entity.getNumOfSets());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS, entity.getNumOfReps());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT, entity.getWeight());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID, getSessionId(parentName));

        return db.insertOrThrow(OpenGymDbContract.StrengthExerciseTable.TABLE_NAME, null, values);
    }

    @Override
    public int delete(String name, String parentName) {

        String selection = OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getSessionId(parentName))};

        return db.delete(OpenGymDbContract.StrengthExerciseTable.TABLE_NAME, selection, selectionArgs);
    }

    @SuppressLint("Range")
    @Override
    public StrengthExercise read(String name, String parentName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT
        };

        String selection = OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getSessionId(parentName))};

        Cursor cursor = db.query(
                OpenGymDbContract.StrengthExerciseTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int repetitions, sets;
        float weight;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME));
            sets = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS));
            repetitions = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS));
            weight = cursor.getFloat(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT));
        }
        else {
            return null;
        }

        cursor.close();

        return new StrengthExercise(name, repetitions, sets, weight);
    }

    @Override
    public int update(StrengthExercise entity, String name, String parentName) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS, entity.getNumOfSets());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS, entity.getNumOfReps());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT, entity.getWeight());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID, getSessionId(parentName));

        String selection = OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME + " LIKE ? AND " +
                OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getSessionId(parentName))};

        return db.update(
                OpenGymDbContract.StrengthExerciseTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    @Override
    public void closeConnection() {
        dbHelper.close();
    }

    @SuppressLint("Range")
    public ArrayList<StrengthExercise> getAll(String parentName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT
        };

        String selection = OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID + " = ?";
        String[] selectionArgs = {String.valueOf(getSessionId(parentName))};

        Cursor cursor = db.query(
                OpenGymDbContract.StrengthExerciseTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String name;
        int repetitions, sets;
        float weight;
        ArrayList<StrengthExercise> ExerciseList = new ArrayList<>();

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME));
            sets = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS));
            repetitions = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS));
            weight = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS));

            ExerciseList.add(new StrengthExercise(name, repetitions, sets, weight));
        }

        cursor.close();

        return ExerciseList;
    }

    @SuppressLint("Range")
    private int getSessionId(String sessionName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.SessionsTable.COLUMN_ID
        };

        String selection = OpenGymDbContract.SessionsTable.COLUMN_NAME + " LIKE ?";

        String[] selectionArgs = {sessionName};

        Cursor cursor = db.query(
                OpenGymDbContract.SessionsTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.SessionsTable.COLUMN_ID));
            cursor.close();
            return id;
        }
        else {
            return -1;
        }
    }
}


 */
