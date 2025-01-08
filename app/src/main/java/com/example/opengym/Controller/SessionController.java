package com.example.opengym.Controller;

import android.content.Context;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.IExercise;
import com.example.opengym.Model.Entities.Session;
import com.example.opengym.Model.Entities.ExerciseFactory;
import com.example.opengym.Model.DAO.SessionDAO;



import java.util.ArrayList;


public class SessionController {
    private final Session controlledSession;

    public SessionController(Context context , long id) {
        SessionDAO sessionDAO = new SessionDAO(context);
        controlledSession = sessionDAO.read(id);
    }

    public long addStrenghExercise(Context context, String exerciseName, int sets, int reps, float weight) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, sets, reps, weight);
        return controlledSession.addStrengthExercise(exercise);
    }

    public long addTimedExercise(Context context, String exerciseName, int duration) {
        IExercise exercise = ExerciseFactory.createExercise(exerciseName, duration);
        return controlledSession.addTimedExercise(exercise);
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