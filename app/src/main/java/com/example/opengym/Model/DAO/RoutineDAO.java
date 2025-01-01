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

    OpenGymDbHelper dbHelper;
    SQLiteDatabase db;

    public RoutineDAO(Context context) {
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    @Override
    public long create(Routine entity, String parentId) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.RoutinesTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_USERNAME, parentId);
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION, entity.getDescription());

        return db.insertOrThrow(OpenGymDbContract.RoutinesTable.TABLE_NAME, null, values);
    }

    @Override
    public int delete(String id, String parentId) {

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " LIKE ? AND " +
                           OpenGymDbContract.RoutinesTable.COLUMN_USERNAME + " LIKE ?";

        String[] selectionArgs = {id, parentId};

        return db.delete(OpenGymDbContract.RoutinesTable.TABLE_NAME, selection, selectionArgs);
    }

    @SuppressLint("Range")
    @Override
    public Routine read(String id, String parentId) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " LIKE ? AND " +
                           OpenGymDbContract.RoutinesTable.COLUMN_USERNAME + " LIKE ?";

        String[] selectionArgs = {id, parentId};

        Cursor cursor = db.query(
                OpenGymDbContract.RoutinesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String name, description;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_NAME));
            description = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_USERNAME));
        }
        else {
            return null;
        }

        cursor.close();

        return new Routine(name, description, null);
    }

    @Override
    public int update(Routine entity, String id, String parentId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_USERNAME, parentId);
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION, entity.getDescription());

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " LIKE ? AND " +
                           OpenGymDbContract.RoutinesTable.COLUMN_USERNAME + " LIKE ?";

        String[] selectionArgs = {id, parentId};

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

    @SuppressLint("Range")
    public ArrayList<Routine> getAll(String parentId) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {parentId};

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
            String description = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_USERNAME));
            RoutineList.add(new Routine(name, description, null));
        }

        cursor.close();

        return RoutineList;
    }
}
