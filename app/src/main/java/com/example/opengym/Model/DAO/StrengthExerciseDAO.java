package com.example.opengym.Model.DAO;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.Entities.ExerciseFactory;
import com.example.opengym.Model.Entities.StrengthExercise;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

import java.util.ArrayList;

public class StrengthExerciseDAO implements GenericDAO<StrengthExercise> {

    private final OpenGymDbHelper dbHelper;
    private SQLiteDatabase db;

    public StrengthExerciseDAO(Context context){
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    @Override
    public long create(StrengthExercise entity, long parentId) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS, entity.getNumOfReps());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS, entity.getNumOfSets());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT, entity.getWeight());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID, parentId);

        long id = db.insertOrThrow(OpenGymDbContract.StrengthExerciseTable.TABLE_NAME, null, values);
        entity.setId(id);
        return id;
    }

    @Override
    public int delete(long id) {

        db = dbHelper.getWritableDatabase();

        String selection = OpenGymDbContract.StrengthExerciseTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(OpenGymDbContract.StrengthExerciseTable.TABLE_NAME, selection, selectionArgs);
    }

    @SuppressLint("Range")
    @Override
    public ArrayList<StrengthExercise> readAll(long parentId) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.StrengthExerciseTable.COLUMN_ID,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS,
                OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT
        };

        String selection = OpenGymDbContract.StrengthExerciseTable.COLUMN_SESSIONID + " = ?";
        String[] selectionArgs = {String.valueOf(parentId)};

        Cursor cursor = db.query(
                OpenGymDbContract.StrengthExerciseTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<StrengthExercise> ExerciseList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME));
            int reps = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS));
            int sets = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS));
            float weight = cursor.getFloat(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT));
            long id = cursor.getLong(cursor.getColumnIndex(OpenGymDbContract.StrengthExerciseTable.COLUMN_ID));
            ExerciseList.add((StrengthExercise) ExerciseFactory.createExercise(name, sets, reps, weight, id));
        }

        cursor.close();

        return ExerciseList;
    }

    @Override
    public int update(StrengthExercise entity, long id) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_REPETITIONS, entity.getNumOfReps());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_SETS, entity.getNumOfSets());
        values.put(OpenGymDbContract.StrengthExerciseTable.COLUMN_WEIGHT, entity.getWeight());

        String selection = OpenGymDbContract.StrengthExerciseTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        entity.setId(id);

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
}
