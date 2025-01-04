package com.example.opengym.Controller;

import android.content.Context;

import com.example.opengym.Model.Entities.IExercise;
import com.example.opengym.Model.Entities.Session;

import java.util.ArrayList;

public class SessionController {
    private final Session controlledSession;

    public SessionController(Context context) {
        this.controlledSession = new Session();
        controlledSession.getInfoDB();
    }

    public String getSessionName() {
        return controlledSession.getName();
    }

    public void removeSessionExercise(String name) {
        controlledSession.removeExercise(name);
    }

    /* TODO No puede recibir ejercicios
    public void addSessionExercise(IExercise exercise) {
        controlledSession.addExercise(exercise);
    }

     */

    public ArrayList<String> getSessionExercises() {
        ArrayList<IExercise> exerciseList = controlledSession.getExercisesList();
        ArrayList<String> exerciseNames = new ArrayList<>();
        for (IExercise exercise : exerciseList) {
            exerciseNames.add(exercise.getName());
        }
        return exerciseNames;
    }
}
