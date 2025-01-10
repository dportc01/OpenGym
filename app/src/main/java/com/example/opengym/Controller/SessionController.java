package com.example.opengym.Controller;

import android.content.Context;

import com.example.opengym.Model.Entities.IExercise;
import com.example.opengym.Model.Entities.Session;
import com.example.opengym.Model.Entities.ExerciseFactory;
import com.example.opengym.Model.Entities.StrengthExercise;
import com.example.opengym.Model.Entities.TimedExercise;
import com.example.opengym.Model.DAO.SessionDAO;

import java.util.ArrayList;
import java.util.Date;


public class SessionController {
    private final Session controlledSession;
    private Session previousSession;
    private Session newSession;

    public SessionController(Context context , long id) {
        SessionDAO sessionDAO = new SessionDAO(context);
        controlledSession = sessionDAO.read(id);
        loadExercises(context);
    }

    public void loadExercises(Context context){
        controlledSession.setInfoDB(context, controlledSession.getId());
    }

    public long addStrengthExercise(Context context, String exerciseName, int sets, int reps, float weight) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, sets, reps, weight);
        return controlledSession.addStrengthExercise(exercise, controlledSession.getId(), context);
    }

    public void addTimedExercise(Context context, String exerciseName, int duration) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, duration);
        controlledSession.addTimedExercise(exercise, controlledSession.getId(), context);
    }

    /**
     * Creates new session, if the session alredy exists does nothing
     * @param context
     * @param parentId
     */
    public void createNewSession(Context context, long parentId) {
        if(newSession == null) {
            newSession = new Session(controlledSession.getName(), new Date(System.currentTimeMillis()), controlledSession.getRestDuration(), new ArrayList<IExercise>());
            SessionDAO sessionDAO = new SessionDAO(context);
            sessionDAO.create(newSession, parentId);
        }
    }

    public void addStrengthExerciseToNewSession(Context context, String exerciseName, int sets, int reps, float weight) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, sets, reps, weight);
        newSession.addStrengthExercise(exercise, newSession.getId(), context);
    }

    public void addTimedExerciseToNewSession(Context context, String exerciseName, int duration) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, duration);
        newSession.addTimedExercise(exercise, newSession.getId(), context);
    }

    public void removeExercises(Context context) {

        controlledSession.removeAllExercises(context);
    }

    /**
     *
     * @param context
     * @return false if it doesn't have a previous recorded session and true otherwise
     */
    public boolean loadNewestSession(Context context) {

        SessionDAO sessionsTable = new SessionDAO(context);
        ArrayList<Session> pastSessions = sessionsTable.getAllPast(controlledSession.getId());

        if (pastSessions.isEmpty()) {
            return false;
        }

        Date sessionDate = new Date(0);

        for (Session session : pastSessions) {
            if (sessionDate.getTime()  < session.getDate().getTime()) {
                previousSession = session;
            }
        }

        previousSession.setInfoDB(context, previousSession.getId());

        return true;
    }

    public ArrayList<ArrayList<String>> returnPreviousExerciseData() {

        ArrayList<ArrayList<String>> exercisesArray = new ArrayList<>();
        ArrayList<String> exerciseFields;

        for (int i = 0; i < previousSession.getExercisesList().size(); i++) {
            IExercise exercise = previousSession.getExerciseAt(i);
            if (exercise.getType().equals("Strength")) {
                StrengthExercise strExercise = (StrengthExercise) exercise;
                exerciseFields = new ArrayList<>();

                exerciseFields.add(strExercise.getType());
                exerciseFields.add(String.valueOf(strExercise.getNumOfReps()));
                exerciseFields.add(String.valueOf(strExercise.getWeight()));
            } else {
                TimedExercise timedExercise = (TimedExercise) exercise;
                exerciseFields = new ArrayList<>();

                exerciseFields.add(timedExercise.getType());
                exerciseFields.add(String.valueOf(timedExercise.getTime()));
            }

            exercisesArray.add(exerciseFields);
        }

        return exercisesArray;
    }

    public ArrayList<ArrayList<String>> returnExercises() {

        ArrayList<ArrayList<String>> exercisesArray = new ArrayList<>();
        ArrayList<String> exerciseFields;

        for (int i = 0; i < controlledSession.getExercisesList().size(); i++) {
            IExercise exercise = controlledSession.getExerciseAt(i);
            if (exercise.getType().equals("Strength")) {
                StrengthExercise strExercise = (StrengthExercise) exercise;
                exerciseFields = new ArrayList<>();

                exerciseFields.add(strExercise.getType());
                exerciseFields.add(strExercise.getName());
                exerciseFields.add(String.valueOf(strExercise.getNumOfReps()));
                exerciseFields.add(String.valueOf(strExercise.getNumOfSets()));
                exerciseFields.add(String.valueOf(strExercise.getWeight()));
            } else {
                TimedExercise timedExercise = (TimedExercise) exercise;
                exerciseFields = new ArrayList<>();

                exerciseFields.add(timedExercise.getType());
                exerciseFields.add(timedExercise.getName());
                exerciseFields.add(String.valueOf(timedExercise.getTime()));
            }

            exercisesArray.add(exerciseFields);
        }

        return exercisesArray;
    }
}