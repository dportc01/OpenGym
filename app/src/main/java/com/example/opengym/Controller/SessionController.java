package com.example.opengym.Controller;

import android.content.Context;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.IExercise;
import com.example.opengym.Model.Entities.Session;
import com.example.opengym.Model.Entities.ExerciseFactory;
import com.example.opengym.Model.Entities.StrengthExercise;
import com.example.opengym.Model.Entities.TimedExercise;
import com.example.opengym.Model.DAO.SessionDAO;



import java.util.ArrayList;


public class SessionController {
    private final Session controlledSession;

    public SessionController(Context context , long id) {
        SessionDAO sessionDAO = new SessionDAO(context);
        controlledSession = sessionDAO.read(id);
    }

    public void loadExercises(Context context){
        controlledSession.setInfoDB(context, controlledSession.getId());
    }

    public String getExerciseName(int position){
        try{
            return controlledSession.getExercisesList().get(position).getName();
        } catch (Exception e) {
            return null;
        }
    }

    public String getExerciseType(int position){
        try{
            return controlledSession.getExercisesList().get(position).getType();
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<String> getPreviousStrengthValues(int position) {
        try {
            IExercise exercise = controlledSession.getExercisesList().get(position);
            if (exercise instanceof StrengthExercise) {
                StrengthExercise strengthExercise = (StrengthExercise) exercise;
                ArrayList<String> pastValues = new ArrayList<>();
                pastValues.add(String.valueOf(strengthExercise.getNumOfReps()));
                pastValues.add(String.valueOf(strengthExercise.getNumOfSets()));
                pastValues.add(String.valueOf(strengthExercise.getWeight()));
                return pastValues;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getPreviousDurationValues(int position) {
        try {
            IExercise exercise = controlledSession.getExercisesList().get(position);
            if (exercise instanceof TimedExercise) {
                TimedExercise timedExercise = (TimedExercise) exercise;
                return String.valueOf(timedExercise.getTime());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void addStrenghExercise(Context context, String exerciseName, int sets, int reps, float weight) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, sets, reps, weight);
        controlledSession.addStrengthExercise(exercise, context);
    }

    public void addTimedExercise(Context context, String exerciseName, int duration) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, duration);
        controlledSession.addTimedExercise(exercise, context);
    }
    
    public ArrayList<String> getSessionExercises() {
        ArrayList<IExercise> exerciseList = controlledSession.getExercisesList();
        ArrayList<String> exerciseNames = new ArrayList<>();
        for (IExercise exercise : exerciseList) {
            exerciseNames.add(exercise.getName());
        }
        return exerciseNames;
    }
}