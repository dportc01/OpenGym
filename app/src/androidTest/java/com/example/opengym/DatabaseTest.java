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

import com.example.opengym.Model.DAO.RoutineDAO;
import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.OpenGymDbHelper;
import com.example.opengym.Model.Entities.User;

public class DatabaseTest {

    Context appContext;
    User Juan;
    UserDAO dbUsers;
    Routine EpicRoutine;
    RoutineDAO dbRoutines;

    //------------ Start of tests

    @Before
    public void initDbData() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Juan = new User("Juan", "1234");
        EpicRoutine = new Routine("Epic Routine", "Amazing routine to get ripped in 3 months", null);
    }

    @Test
    public void userAdd() throws SQLException {

        dbUsers = new UserDAO(appContext);

        dbUsers.create(Juan, null);
        User newUser = dbUsers.read(Juan.getName(), null);

        Assert.assertEquals(Juan.getName(), newUser.getName());
        Assert.assertEquals(Juan.getPassword(), newUser.getPassword());
        Assert.assertFalse(newUser.getPremium());

        dbUsers.closeConnection();
    }

    @Test
    public void pkUsers() {

        dbUsers = new UserDAO(appContext);
        dbUsers.create(Juan, null);

        Assert.assertThrows(SQLiteConstraintException.class, () -> dbUsers.create(Juan, null));
    }

    @Test
    public void removeUser() {

        dbUsers = new UserDAO(appContext);
        dbUsers.create(Juan, null);

        Assert.assertEquals(1, dbUsers.delete(Juan.getName(), null));
        Assert.assertEquals(0, dbUsers.delete(Juan.getName(), null));

        Assert.assertNull(dbUsers.read(Juan.getName(), null));
    }

    @Test
    public void updateUser() {

        dbUsers = new UserDAO(appContext);
        dbUsers.create(Juan, null);

        User JuanPremium = new User(Juan.getName(), "4321", true, null);
        Assert.assertEquals(1, dbUsers.update(JuanPremium, Juan.getName(), null));

        User newUser = dbUsers.read(Juan.getName(), null);

        Assert.assertEquals(JuanPremium.getName(), newUser.getName());
        Assert.assertEquals(JuanPremium.getPassword(), newUser.getPassword());
        Assert.assertTrue(newUser.getPremium());
    }

    @Test (expected = android.database.sqlite.SQLiteException.class)
    public void testEmptyDb() {

        dbUsers = new UserDAO(appContext);

        dbUsers.create(Juan, null);

        OpenGymDbHelper dbHelper = OpenGymDbHelper.getInstance(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dbHelper.sql_delete_entries(db);

        dbUsers.read(Juan.getName(), null);
    }

    @Test (expected = android.database.sqlite.SQLiteConstraintException.class)
    public void addRoutineNoUsers() {

        dbUsers = new UserDAO(appContext);
        Assert.assertNull(dbUsers.read(Juan.getName(), null));

        dbRoutines = new RoutineDAO(appContext);
        dbRoutines.create(EpicRoutine, Juan.getName());
    }

    @Test
    public void addRoutine() {

        dbUsers = new UserDAO(appContext);
        Assert.assertEquals(1, dbUsers.create(Juan, null));

        dbUsers.closeConnection();

        dbRoutines = new RoutineDAO(appContext);
        Assert.assertEquals(1, dbRoutines.create(EpicRoutine, Juan.getName()));

        Routine obtainedRoutine = dbRoutines.read(EpicRoutine.getName(), Juan.getName());

        Assert.assertEquals(EpicRoutine.getName(), obtainedRoutine.getName());
        Assert.assertEquals(EpicRoutine.getDescription(), obtainedRoutine.getDescription());

        dbRoutines.closeConnection();
    }

    @Test
    public void PersistenceTest() {

        try (ActivityScenario<Main> scenario = ActivityScenario.launch(Main.class)) {
            scenario.onActivity(activity -> {

                dbUsers = new UserDAO(activity.getApplicationContext());

                dbUsers.create(Juan, null);
                dbUsers.closeConnection();
            });

            scenario.close();

            try (ActivityScenario<Main> restartedScenario = ActivityScenario.launch(Main.class)) {
                restartedScenario.onActivity(activity -> {

                    UserDAO newBridge = new UserDAO(activity.getApplicationContext());

                    User newUser = newBridge.read(Juan.getName(), null);
                    newBridge.closeConnection();

                    Assert.assertEquals(Juan.getName(), newUser.getName());
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
        dbHelper.sql_delete_entries(db);
        dbHelper.onCreate(db);
    }
}
