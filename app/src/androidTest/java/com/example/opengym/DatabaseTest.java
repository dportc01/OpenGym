package com.example.opengym;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import com.example.opengym.Model.DAO.RoutineDAO;
//import com.example.opengym.Model.DAO.SessionDAO;
//import com.example.opengym.Model.DAO.StrengthExerciseDao;
import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.ExerciseFactory;
import com.example.opengym.Model.Entities.IExercise;
import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.Entities.Session;
import com.example.opengym.Model.Entities.StrengthExercise;
import com.example.opengym.Model.OpenGymDbHelper;
import com.example.opengym.Model.Entities.User;

import java.util.ArrayList;
import java.util.Date;

public class DatabaseTest {

    Context appContext;
    User Juan;
    UserDAO dbUsers;

    Routine EpicRoutine;
    RoutineDAO dbRoutines;

    Session MondaySession;
//    SessionDAO dbSessions;

    StrengthExercise Lifting;
//    StrengthExerciseDao dbSExcersice;

    //------------ Start of tests

    @Before
    public void initDbData() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Juan = new User("Juan", "1234");
        EpicRoutine = new Routine("Epic Routine", "Amazing routine to get ripped in 3 months", null);
        MondaySession = new Session("Monday", null, 2, null);
        Lifting = (StrengthExercise) ExerciseFactory.createExercise("Dumbell lifting", 2, 1, (float)16.5);
    }

    @Test
    public void userAdd() throws SQLException {

        dbUsers = new UserDAO(appContext);

        Juan.setId(dbUsers.create(Juan, -1));

        User newUser = dbUsers.read(Juan.getName());

        Assert.assertEquals(Juan.getName(), newUser.getName());
        Assert.assertEquals(Juan.getPassword(), newUser.getPassword());
        Assert.assertFalse(newUser.getPremium());
        Assert.assertEquals(Juan.getId(), newUser.getId());

        dbUsers.closeConnection();
    }

    @Test (expected = SQLException.class)
    public void UniqueUsers() {

        dbUsers = new UserDAO(appContext);
        dbUsers.create(Juan, -1);

        Assert.assertEquals(-1, dbUsers.create(Juan, -1));
    }


    @Test
    public void removeUser() {

        dbUsers = new UserDAO(appContext);
        Juan.setId(dbUsers.create(Juan, -1));

        Assert.assertEquals(1, dbUsers.delete(Juan.getId()));
        Assert.assertEquals(0, dbUsers.delete(Juan.getId()));

        Assert.assertNull(dbUsers.read(Juan.getName()));
    }

    //We do not plan to update user
    /*
    @Test
    public void updateUser() {

        dbUsers = new UserDAO(appContext);
        Juan.setId(dbUsers.create(Juan, -1));

        User JuanAlberto = new User("Juan Alberto", "4321", true, null);
        Assert.assertEquals(1, dbUsers.update(JuanAlberto, Juan.getId()));

        User newUser = dbUsers.read(JuanAlberto.getName());

        Assert.assertEquals(JuanAlberto.getName(), newUser.getName());
        Assert.assertEquals(JuanAlberto.getPassword(), newUser.getPassword());
        Assert.assertTrue(newUser.getPremium());
    }

     */


    @Test (expected = android.database.sqlite.SQLiteException.class)
    public void testEmptyDb() {

        dbUsers = new UserDAO(appContext);

        dbUsers.create(Juan, -1);

        OpenGymDbHelper dbHelper = OpenGymDbHelper.getInstance(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dbHelper.sql_delete_entries(db);

        dbUsers.read(Juan.getName());
    }


    @Test (expected = android.database.sqlite.SQLiteConstraintException.class)
    public void addRoutineNoUsers() {

        dbUsers = new UserDAO(appContext);
        Assert.assertNull(dbUsers.read(Juan.getName()));

        dbRoutines = new RoutineDAO(appContext);
        dbRoutines.create(EpicRoutine, 1);

        dbRoutines.closeConnection();
    }

    @Test
    public void addRoutine() {

        dbUsers = new UserDAO(appContext);
        Assert.assertEquals(1, dbUsers.create(Juan, -1));

        dbUsers.closeConnection();

        dbRoutines = new RoutineDAO(appContext);
        Assert.assertEquals(1, dbRoutines.create(EpicRoutine, 1));

        dbRoutines.closeConnection();
    }

    @Test
    public void removeRoutine() {

        dbUsers = new UserDAO(appContext);
        Juan.setId(dbUsers.create(Juan, -1));

        dbRoutines = new RoutineDAO(appContext);
        EpicRoutine.setId(dbRoutines.create(EpicRoutine, Juan.getId()));

        Assert.assertEquals(1, dbRoutines.delete(EpicRoutine.getId()));
        Assert.assertEquals(0, dbRoutines.delete(EpicRoutine.getId()));
        dbRoutines.closeConnection();
    }

    @Test
    public  void readAllRoutines() {

        dbUsers = new UserDAO(appContext);
        Juan.setId(dbUsers.create(Juan, -1));
        dbUsers.closeConnection();

        Routine NewbieRoutine = new Routine("Newbie routine", "Routine to start training from zero", null);
        Routine MisteriousRoutine = new Routine("Mysterious Routine", "Mysterious routine with unexpected results", null);

        dbRoutines = new RoutineDAO(appContext);
        EpicRoutine.setId(dbRoutines.create(EpicRoutine, Juan.getId()));
        NewbieRoutine.setId(dbRoutines.create(NewbieRoutine, Juan.getId()));
        MisteriousRoutine.setId(dbRoutines.create(MisteriousRoutine, Juan.getId()));

        ArrayList<Routine> Routines = dbRoutines.readAll(Juan.getId());

        Assert.assertEquals(Routines.get(0).getName(), EpicRoutine.getName());
        Assert.assertEquals(Routines.get(0).getId(), EpicRoutine.getId());
        Assert.assertEquals(Routines.get(1).getName(), NewbieRoutine.getName());
        Assert.assertEquals(Routines.get(1).getId(), NewbieRoutine.getId());
        Assert.assertEquals(Routines.get(2).getName(), MisteriousRoutine.getName());
        Assert.assertEquals(Routines.get(2).getId(), MisteriousRoutine.getId());
    }

    @Test
    public void updateRoutine() {

        dbUsers = new UserDAO(appContext);
        Juan.setId(dbUsers.create(Juan, -1));
        dbUsers.closeConnection();

        dbRoutines = new RoutineDAO(appContext);
        EpicRoutine.setId(dbRoutines.create(EpicRoutine, Juan.getId()));

        Routine MoreEpicRoutine = new Routine("EPIC ROUTINE", "Routine to start training from zero", null);

        Assert.assertEquals(1, dbRoutines.update(MoreEpicRoutine, EpicRoutine.getId()));
        Routine newRoutine =  dbRoutines.readAll(Juan.getId()).get(0);

        Assert.assertEquals(MoreEpicRoutine.getDescription(), newRoutine.getDescription());

        dbRoutines.closeConnection();
    }


    /*
    private void prepareSession() {

        dbUsers = new UserDAO(appContext);
        dbUsers.create(Juan, null);

        dbRoutines = new RoutineDAO(appContext);
        dbRoutines.create(EpicRoutine, Juan.getName());
    }

    @Test
    public void addSession() {

        prepareSession();

        dbSessions = new SessionDAO(appContext);
        Assert.assertEquals(1, dbSessions.create(MondaySession, EpicRoutine.getName()));
    }

    @Test
    public void deleteSession() {

        prepareSession();

        dbSessions = new SessionDAO(appContext);
        dbSessions.create(MondaySession, EpicRoutine.getName());
        Assert.assertEquals(1, dbSessions.delete(MondaySession.getName(), EpicRoutine.getName()));
    }

    @Test
    public void readSession() {

        prepareSession();

        dbSessions = new SessionDAO(appContext);
        dbSessions.create(MondaySession, EpicRoutine.getName());
        Session ReadSession = dbSessions.read(MondaySession.getName(), EpicRoutine.getName());

        Assert.assertEquals(MondaySession.getName(), ReadSession.getName());
        Assert.assertEquals(MondaySession.getDate(), ReadSession.getDate());
        Assert.assertEquals(MondaySession.getRestDuration(), ReadSession.getRestDuration());
    }

    @Test
    public void updateSession() {

        prepareSession();

        dbSessions = new SessionDAO(appContext);
        dbSessions.create(MondaySession, EpicRoutine.getName());

        Session ThursdaySession = new Session("Thursday session", null, 1, null);
        Assert.assertEquals(1, dbSessions.update(ThursdaySession, MondaySession.getName(), EpicRoutine.getName()));
    }

    @Test
    public void getAllSessions() {

        prepareSession();

        Session MondaySessionPast = new Session(MondaySession.getName(), new Date(System.currentTimeMillis()), MondaySession.getRestDuration(), MondaySession.getExercisesList());
        Session FridaySession = new Session("Friday session", null, 1, null);
        Session FridaySessionPast = new Session(FridaySession.getName(), new Date(System.currentTimeMillis()), FridaySession.getRestDuration(), FridaySession.getExercisesList());

        dbSessions = new SessionDAO(appContext);
        dbSessions.create(MondaySession, EpicRoutine.getName());
        dbSessions.create(MondaySessionPast, EpicRoutine.getName());
        dbSessions.create(FridaySession, EpicRoutine.getName());
        dbSessions.create(FridaySessionPast, EpicRoutine.getName());

        ArrayList<Session> Sessions = dbSessions.getAll(EpicRoutine.getName());
        Assert.assertEquals(MondaySession.getName(), Sessions.get(0).getName());
        Assert.assertEquals(MondaySession.getDate(), Sessions.get(0).getDate());
        Assert.assertEquals(FridaySession.getName(), Sessions.get(1).getName());
        Assert.assertEquals(FridaySession.getDate(), Sessions.get(1).getDate());

        Session MondaySessionPast1 = new Session(MondaySession.getName(), new Date(System.currentTimeMillis() - 20000), MondaySession.getRestDuration(), MondaySession.getExercisesList());
        dbSessions.create(MondaySessionPast1, EpicRoutine.getName());

        ArrayList<Session> PastSessions = dbSessions.getAllPast(EpicRoutine.getName(), MondaySession.getName());
        Assert.assertEquals(MondaySessionPast.getName(), PastSessions.get(0).getName());
        //Milliseconds error so I compare the Strings for approximate results
        Assert.assertEquals(MondaySessionPast.getDate().toString(), PastSessions.get(0).getDate().toString());
        Assert.assertEquals(MondaySessionPast1.getName(), PastSessions.get(1).getName());
        //Milliseconds error so I compare the Strings for approximate results
        Assert.assertEquals(MondaySessionPast1.getDate().toString(), PastSessions.get(1).getDate().toString());
    }

    private void prepareExercise() {

        prepareSession();

        dbSessions = new SessionDAO(appContext);
        dbSessions.create(MondaySession, EpicRoutine.getName());
    }

    @Test
    public void addStrengthExercise() {

        prepareExercise();

        dbSExcersice = new StrengthExerciseDao(appContext);
        Assert.assertEquals(1, dbSExcersice.create(Lifting, MondaySession.getName()));
    }

    @Test
    public void deleteStrengthExercise() {

        prepareExercise();

        dbSExcersice = new StrengthExerciseDao(appContext);
        dbSExcersice.create(Lifting, MondaySession.getName());
        Assert.assertEquals(1, dbSExcersice.delete(Lifting.getName(), MondaySession.getName()));
    }

    @Test
    public void readStrengthExercise() {

        prepareExercise();

        dbSExcersice = new StrengthExerciseDao(appContext);
        dbSExcersice.create(Lifting, MondaySession.getName());

        StrengthExercise obtainedExercise = dbSExcersice.read(Lifting.getName(), MondaySession.getName());

        Assert.assertEquals(Lifting.getName(), obtainedExercise.getName());
        Assert.assertEquals(Lifting.getNumOfReps(), obtainedExercise.getNumOfReps());
        Assert.assertEquals(Lifting.getNumOfSets(), obtainedExercise.getNumOfSets());
        Assert.assertEquals(Lifting.getWeight(), obtainedExercise.getWeight(), 0);
    }

    @Test
    public void updateStrengthExercise() {

        prepareExercise();

        dbSExcersice = new StrengthExerciseDao(appContext);
        dbSExcersice.create(Lifting, MondaySession.getName());

        StrengthExercise Squating =  (StrengthExercise) ExerciseFactory.createExercise("Squatting", 3, 10, (float)0);

        Assert.assertEquals(1, dbSExcersice.update(Squating, Lifting.getName(), MondaySession.getName()));

        StrengthExercise obtainedExercise = dbSExcersice.read(Squating.getName(), MondaySession.getName());

        Assert.assertEquals(Squating.getName(), obtainedExercise.getName());
        Assert.assertEquals(Squating.getNumOfReps(), obtainedExercise.getNumOfReps());
        Assert.assertEquals(Squating.getNumOfSets(), obtainedExercise.getNumOfSets());
        Assert.assertEquals(Squating.getWeight(), obtainedExercise.getWeight(), 0);
    }

    public void getAllStrengthExercises() {

        prepareExercise();

        StrengthExercise Squating =  (StrengthExercise) ExerciseFactory.createExercise("Squatting", 3, 10, (float)0);
        StrengthExercise Squating =  (StrengthExercise) ExerciseFactory.createExercise("Squatting", 3, 10, (float)0);

    }

    //Ignore this test it needs to be updated
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

     */

    @After
    public void removeDbData() {
        OpenGymDbHelper dbHelper = OpenGymDbHelper.getInstance(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.sql_delete_entries(db);
        dbHelper.onCreate(db);
    }
}
