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
    public long create(Routine entity, String parentName) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.RoutinesTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION, entity.getDescription());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_USERID, getUserId(parentName));

        return db.insertOrThrow(OpenGymDbContract.RoutinesTable.TABLE_NAME, null, values);
    }

    @Override
    public int delete(String name, String parentName) {

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " LIKE ? AND " +
                           OpenGymDbContract.RoutinesTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getUserId(parentName))};

        return db.delete(OpenGymDbContract.RoutinesTable.TABLE_NAME, selection, selectionArgs);
    }

    @SuppressLint("Range")
    @Override
    public Routine read(String name, String parentName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " LIKE ? AND " +
                           OpenGymDbContract.RoutinesTable.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getUserId(parentName))};

        Cursor cursor = db.query(
                OpenGymDbContract.RoutinesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String description;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_NAME));
            description = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION));
        }
        else {
            return null;
        }

        cursor.close();

        return new Routine(name, description, null);
    }

    @Override
    public int update(Routine entity, String name, String parentName) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION, entity.getDescription());

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " LIKE ? AND " +
                           OpenGymDbContract.RoutinesTable.COLUMN_USERID + " LIKE ?";

        String[] selectionArgs = {name, String.valueOf(getUserId(parentName))};

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
    public ArrayList<Routine> getAll(String parentName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_USERID + " = ?";
        String[] selectionArgs = {String.valueOf(getUserId(parentName))};

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
            RoutineList.add(new Routine(name, description, null));
        }

        cursor.close();

        return RoutineList;
    }

    @SuppressLint("Range")
    private int getUserId(String userName) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.UsersTable.COLUMN_ID
        };

        String selection = OpenGymDbContract.UsersTable.COLUMN_NAME + " LIKE ?";

        String[] selectionArgs = {userName};

        Cursor cursor = db.query(
                OpenGymDbContract.UsersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_ID));
            cursor.close();
            return id;
        }
        else {
            return -1;
        }
    }
}
