package com.example.opengym;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

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
    UserDAO dbUsers;
    private final String[] routineInfo = {
            "Epic Routine",
            "This epic routine will get you pumping those muscles in no time!",
            "Juan"
    };

    //------------ Start of tests

    @Before
    public void initDbData() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Juan = new User("Juan", "1234");
    }

    @Test
    public void userAdd() throws SQLException {

        dbUsers = new UserDAO(appContext);

        dbUsers.create(Juan);
        User dbUser = dbUsers.read(Juan.getName());

        Assert.assertEquals(Juan.getName(), dbUser.getName());
        Assert.assertEquals(Juan.getPassword(), dbUser.getPassword());
        Assert.assertFalse(dbUser.getPremium());

        dbUsers.closeConnection();
    }

    @Test
    public void pkUsers() throws Exception {

        dbUsers = new UserDAO(appContext);

        dbUsers.create(Juan);

        Assert.assertThrows(SQLiteConstraintException.class, () -> dbUsers.create(Juan));
    }

    /*
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
    */

    @Test (expected = android.database.sqlite.SQLiteException.class)
    public void testEmptyDb() {

        dbUsers = new UserDAO(appContext);

        dbUsers.create(Juan);

        OpenGymDbHelper dbHelper = OpenGymDbHelper.getInstance(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(OpenGymDbContract.SQL_DELETE_ENTRIES);

        dbUsers.read(Juan.getName());
    }

    @Test
    public void PersistenceTest() {

        try (ActivityScenario<Main> scenario = ActivityScenario.launch(Main.class)) {
            scenario.onActivity(activity -> {

                dbUsers = new UserDAO(activity.getApplicationContext());

                dbUsers.create(Juan);
                dbUsers.closeConnection();
            });

            scenario.close();

            try (ActivityScenario<Main> restartedScenario = ActivityScenario.launch(Main.class)) {
                restartedScenario.onActivity(activity -> {

                    UserDAO newBridge = new UserDAO(activity.getApplicationContext());

                    User dbUser = newBridge.read(Juan.getName());
                    newBridge.closeConnection();

                    Assert.assertEquals(Juan.getName(), dbUser.getName());
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Test failed", e);
        }
    }

    @After
    public void removeDbData() {
        OpenGymDbHelper dbHelper = OpenGymDbHelper.getInstance(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(OpenGymDbContract.SQL_DELETE_ENTRIES);
        dbHelper.onCreate(db);
    }
}
