package com.example.opengym;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;

public class DatabaseTest {

    private OpenGymDbHelper dbHelper;
    private final String[] juanInfo = {
            "Juan",
            "1234",
            "Juan@snakemail.com"
    };

    @Before
    public void initDbData() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbHelper = new OpenGymDbHelper(appContext);
    }

    private void addJuan() throws android.database.SQLException {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OpenGymDbContract.Users.COLUMN_NAME, juanInfo[0]);
        values.put(OpenGymDbContract.Users.COLUMN_PASSWORD, juanInfo[1]);
        values.put(OpenGymDbContract.Users.COLUMN_EMAIL, juanInfo[2]);

        db.insertOrThrow(OpenGymDbContract.Users.TABLE_NAME, null, values);
    }

    @Test
    public void userAdd() throws android.database.SQLException {

        addJuan();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.Users.COLUMN_NAME,
                OpenGymDbContract.Users.COLUMN_PASSWORD,
                OpenGymDbContract.Users.COLUMN_EMAIL,
                OpenGymDbContract.Users.COLUMN_PREMIUM
        };

        String selection = OpenGymDbContract.Users.COLUMN_NAME + " = ?";
        String[] selectionArgs = {juanInfo[0]};

        Cursor cursor = db.query(
                OpenGymDbContract.Users.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String name, password, email;
        int premium;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.Users.COLUMN_NAME));
            password = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.Users.COLUMN_PASSWORD));
            email = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.Users.COLUMN_EMAIL));
            premium = cursor.getInt(cursor.getColumnIndex(OpenGymDbContract.Users.COLUMN_PREMIUM));
        }
        else {
            throw new AssertionError("El cursor no encontro nada");
        }


        Assert.assertEquals(juanInfo[0], name);
        Assert.assertEquals(juanInfo[1], password);
        Assert.assertEquals(juanInfo[2], email);
        Assert.assertEquals(0, premium);
    }

    @Test
    public void testPKUsers() throws Exception {
        addJuan();
        Assert.assertThrows(android.database.sqlite.SQLiteConstraintException.class, this::addJuan);
    }

    @Test (expected = android.database.sqlite.SQLiteException.class)
    public void testEmptyDb() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        addJuan();

        db.execSQL(OpenGymDbContract.SQL_DELETE_ENTRIES);

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.Users.COLUMN_NAME,
                OpenGymDbContract.Users.COLUMN_PASSWORD,
                OpenGymDbContract.Users.COLUMN_EMAIL,
                OpenGymDbContract.Users.COLUMN_PREMIUM
        };

        String selection = OpenGymDbContract.Users.COLUMN_NAME + " = ?";
        String[] selectionArgs = {juanInfo[0]};

        Cursor cursor = db.query(
                OpenGymDbContract.Users.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    @After
    public void removeDbData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(OpenGymDbContract.SQL_DELETE_ENTRIES);
        dbHelper.onCreate(db);
    }
}
