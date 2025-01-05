package com.example.opengym.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.Entities.User;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

import java.util.ArrayList;

public class UserDAO implements GenericDAO<User>  {

    private final OpenGymDbHelper dbHelper;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        dbHelper = OpenGymDbHelper.getInstance(context);
    }

    /**
     * Create an new entry on the database
     * @param entity new entity to add to the database
     * @param parentId value doesn't matter since Users don't have a parent
     * can be <code>null</code> if it doesn't have it
     * @return row ID or -1 if an error curred
     */
    @Override
    public long create(User entity, long parentId) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.UsersTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.UsersTable.COLUMN_PASSWORD, entity.getPassword());

        long id = db.insertOrThrow(OpenGymDbContract.UsersTable.TABLE_NAME, null, values);

        createLastLogin(entity.getName());
        entity.setId(id);

        return id;
    }

    private void createLastLogin(String id) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        return db.insertOrThrow(OpenGymDbContract.UsersTable.TABLE_NAME, null, values);
    }

    @Override
    public int delete(long id) {

        String selection = OpenGymDbContract.UsersTable.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(OpenGymDbContract.UsersTable.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Does nothing since Users doesn't implement any foreign key
     */
    @Override
    public ArrayList<User> readAll(long parentId) {

        return null;
    }

    @Override
    public int update(User entity, long id) {

        db = dbHelper.getWritableDatabase();

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

        String selection = OpenGymDbContract.UsersTable.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

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

    @SuppressLint("Range")
    public User read(String name) {

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.UsersTable.COLUMN_NAME,
                OpenGymDbContract.UsersTable.COLUMN_PASSWORD,
                OpenGymDbContract.UsersTable.COLUMN_PREMIUM,
                OpenGymDbContract.UsersTable.COLUMN_ID
        };

        String selection = OpenGymDbContract.UsersTable.COLUMN_NAME + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = db.query(
                OpenGymDbContract.UsersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String password;
        int premium, id;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_NAME));
            password = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_PASSWORD));
            premium = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_PREMIUM));
            id = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_ID));
        }
        else {
            return null;
        }

        cursor.close();

        return new User(name, password, (premium!=0), id);
    }
}
