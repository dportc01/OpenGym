package com.example.opengym.Controller;

import com.example.opengym.Model.Session;

public class SessionController {
    private Session controlledSession;

    public SessionController() {
        this.controlledSession = new Session();
        controlledSession.getInfoDB();
    }

    public String getSessionName() {
        return controlledSession.getName();
    }

    public void removeSessionExercise() {
        controlledSession.removeExercise();
    }

    public void addSessionExercise() {
        controlledSession.addExercise();
    }

    public void getSessionExercises() {
        // TODO
    }


}
