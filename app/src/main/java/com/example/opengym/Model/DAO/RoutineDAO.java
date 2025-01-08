package com.example.opengym.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

import java.util.ArrayList;

public class RoutineDAO implements GenericDAO<Routine> {

    private final OpenGymDbHelper dbHelper;
    private SQLiteDatabase db;

    public RoutineDAO(Context context) {
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    @Override
    public long create(Routine entity, long parentId) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.RoutinesTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION, entity.getDescription());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_USERID, parentId);

        long id = db.insertOrThrow(OpenGymDbContract.RoutinesTable.TABLE_NAME, null, values);
        entity.setId(id);
        return id;
    }

    @Override
    public int delete(long id) {

        db = dbHelper.getWritableDatabase();

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(OpenGymDbContract.RoutinesTable.TABLE_NAME, selection, selectionArgs);
    }

    @SuppressLint("Range")
    @Override
    public ArrayList<Routine> readAll(long parentId) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION,
                OpenGymDbContract.RoutinesTable.COLUMN_ID
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_USERID + " = ?";
        String[] selectionArgs = {String.valueOf(parentId)};

        Cursor cursor = db.query(
                OpenGymDbContract.RoutinesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<Routine> RoutineList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION));
            int id = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_ID));
            RoutineList.add(new Routine(name, description, id));
        }

        cursor.close();

        return RoutineList;
    }
    @SuppressLint("Range")
    public Routine read(long id) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                OpenGymDbContract.RoutinesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION));
        Routine routine = new Routine(name, description, id);

        cursor.close();

        return routine;
    }
    @Override
    public int update(Routine entity, long id) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION, entity.getDescription());

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};

        return db.update(
                OpenGymDbContract.RoutinesTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    @Override
    public void closeConnection() {
        dbHelper.close();
    }
}
