package com.example.opengym.Controller;

import com.example.opengym.Model.Entities.IExercise;
import com.example.opengym.Model.Entities.Session;

import java.util.ArrayList;

public class SessionController {
    private Session controlledSession;

    public SessionController() {
        this.controlledSession = new Session();
        controlledSession.getInfoDB();
    }

    public String getSessionName() {
        return controlledSession.getName();
    }

    public void removeSessionExercise(String name) {
        //controlledSession.removeExercise(name);
    }

    public void addSessionExercise(IExercise exercise) {
        controlledSession.addExercise(exercise);
    }

    /*
    public List<IExercise> getSessionExercises() {
        controlledSession.getExercisesList();
    }
     */


}
