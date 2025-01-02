package com.example.opengym.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.Entities.User;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

public class UserDAO implements GenericDAO<User>  {

    OpenGymDbHelper dbHelper;
    SQLiteDatabase db;

    public UserDAO(Context context) {
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    @Override
    public long create(User entity, String parentId) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.UsersTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.UsersTable.COLUMN_PASSWORD, entity.getPassword());

        return db.insertOrThrow(OpenGymDbContract.UsersTable.TABLE_NAME, null, values);
    }

    @Override
    public int delete(String id, String parentId) {

        String selection = OpenGymDbContract.UsersTable.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {id};

        return db.delete(OpenGymDbContract.UsersTable.TABLE_NAME, selection, selectionArgs);
    }

    @SuppressLint("Range")
    @Override
    public User read(String id, String parentId) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.UsersTable.COLUMN_NAME,
                OpenGymDbContract.UsersTable.COLUMN_PASSWORD,
                OpenGymDbContract.UsersTable.COLUMN_PREMIUM
        };

        String selection = OpenGymDbContract.UsersTable.COLUMN_NAME + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                OpenGymDbContract.UsersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String name, password;
        int premium;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_NAME));
            password = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_PASSWORD));
            premium = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_PREMIUM));
        }
        else {
            return null;
        }

        cursor.close();

        return new User(name, password, (premium!=0), null);
    }

    @Override
    public int update(User entity, String id, String parentId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int premium;
        if (entity.getPremium()) {
            premium = 1;
        }
        else {
            premium = 0;
        }

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.UsersTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.UsersTable.COLUMN_PASSWORD, entity.getPassword());
        values.put(OpenGymDbContract.UsersTable.COLUMN_PREMIUM, premium);

        String selection = OpenGymDbContract.UsersTable.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {id};

        return db.update(
                OpenGymDbContract.UsersTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    @SuppressLint("Range")
    public User userExist() {
        db = dbHelper.getReadableDatabase();
        String[] projection = {
                OpenGymDbContract.UsersTable.COLUMN_NAME,
                OpenGymDbContract.UsersTable.COLUMN_PASSWORD,
                OpenGymDbContract.UsersTable.COLUMN_PREMIUM
        };
        Cursor cursor = db.query(
                OpenGymDbContract.UsersTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        String name, password;
        int premium;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_NAME));
            password = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_PASSWORD));
            premium = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_PREMIUM));
            cursor.close();
            return new User(name, password, (premium!=0), null);
        }
        return null;
    }
    @Override
    public void closeConnection() {
        dbHelper.close();
    }
}
