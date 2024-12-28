package com.example.opengym.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

public class SignUpController {

    public SignUpController() {
    }

    public void signUp(String name, String password, Context context) {

        OpenGymDbHelper dbHelper;
        dbHelper = OpenGymDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.Users.COLUMN_NAME, name);
        values.put(OpenGymDbContract.Users.COLUMN_PASSWORD, password);
        db.insertOrThrow(OpenGymDbContract.Users.TABLE_NAME, null, values);
    }
}
