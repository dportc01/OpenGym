package com.example.opengym;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.OpenGymDbContract;
import com.example.opengym.Model.OpenGymDbHelper;
import com.example.opengym.Model.Entities.User;

public class DatabaseTest {

    Context appContext;
    User Juan;
    private final String[] routineInfo = {
            "Epic Routine",
            "This epic routine will get you pumping those muscles in no time!",
            "Juan"
    };

    private void addEpicRoutine() throws android.database.SQLException {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OpenGymDbContract.RoutinesTable.COLUMN_NAME, routineInfo[0]);
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION, routineInfo[1]);
        values.put(OpenGymDbContract.RoutinesTable.COLUMN_USERNAME, routineInfo[2]);

        db.insertOrThrow(OpenGymDbContract.RoutinesTable.TABLE_NAME, null, values);
    }

    //Start of test

    @Before
    public void initDbData() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Juan = new User("Juan", "1234");
    }

    @Test
    public void userAdd() throws android.database.SQLException {

        UserDAO dbBridge = new UserDAO(appContext);

        dbBridge.create(Juan);
        User dbUser = dbBridge.read(Juan.getName());

        Assert.assertEquals(Juan.getName(), dbUser.getName());
        Assert.assertEquals(Juan.getPassword(), dbUser.getPassword());
        Assert.assertFalse(dbUser.getPremium());

        dbBridge.closeConection();
    }

    @Test
    public void pkUsers() throws Exception {

        addJuan(appContext);
        Assert.assertThrows(android.database.sqlite.SQLiteConstraintException.class, () -> {
            addJuan(appContext);
        });
    }

    @Test
    public void addRoutine() {

        addJuan(appContext);
        addEpicRoutine();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.RoutinesTable.COLUMN_NAME,
                OpenGymDbContract.RoutinesTable.COLUMN_DESCRIPTION,
                OpenGymDbContract.RoutinesTable.COLUMN_USERNAME
        };

        String selection = OpenGymDbContract.RoutinesTable.COLUMN_NAME + " = ?";
        String[] selectionArgs = {juanInfo[2]};

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
            throw new AssertionError("El cursor no encontro nada");
        }


        Assert.assertEquals(juanInfo[0], name);
        Assert.assertEquals(juanInfo[1], password);
        Assert.assertEquals(0, premium);
    }

    @Test (expected = android.database.sqlite.SQLiteException.class)
    public void testEmptyDb() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        addJuan(appContext);

        db.execSQL(OpenGymDbContract.SQL_DELETE_ENTRIES);

        db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.UsersTable.COLUMN_NAME
        };

        String selection = OpenGymDbContract.UsersTable.COLUMN_NAME + " = ?";
        String[] selectionArgs = {juanInfo[0]};

        Cursor cursor = db.query(
                OpenGymDbContract.UsersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    @Test
    public void PersistencyTest() {

        addJuan(appContext);

        ActivityScenario.launch(Main.class).close();
        ActivityScenario.launch(Main.class);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                OpenGymDbContract.UsersTable.COLUMN_NAME
        };

        String selection = OpenGymDbContract.UsersTable.COLUMN_NAME + " = ?";
        String[] selectionArgs = {juanInfo[0]};

        Cursor cursor = db.query(
                OpenGymDbContract.UsersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String name;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(OpenGymDbContract.UsersTable.COLUMN_NAME));
        }
        else {
            throw new AssertionError("El cursor no encontro nada");
        }


        Assert.assertEquals(juanInfo[0], name);
    }

    @After
    public void removeDbData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(OpenGymDbContract.SQL_DELETE_ENTRIES);
        dbHelper.onCreate(db);
    }
}
