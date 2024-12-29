package com.example.opengym.Model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
    public void create(User entity) throws SQLException {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.UsersTable.COLUMN_NAME, entity.getName());
        values.put(OpenGymDbContract.UsersTable.COLUMN_PASSWORD, entity.getPassword());

        db.insertOrThrow(OpenGymDbContract.UsersTable.TABLE_NAME, null, values);
    }

    @Override
    public int delete(User entity) {
        return 0;
    }

    @SuppressLint("Range")
    @Override
    public User read(String id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION,
                OpenGymDbContract.RoutinesTable.COLUMN_USERNAME
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " = ?";
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

        return new User(name, password, (premium==0), null);
    }

    @Override
    public int update(User entity) {
        return 0;
    }

    @Override
    public void closeConection() {
        dbHelper.close();
    }

    public void readName() {

    }
}
