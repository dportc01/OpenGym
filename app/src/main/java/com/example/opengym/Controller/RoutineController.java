package com.example.opengym.Controller;

import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.View.SessionActivity;

import java.util.ArrayList;

public class RoutineController {
    private final Routine controlledRoutine;

    public RoutineController() {
        this.controlledRoutine = new Routine();
        controlledRoutine.getInfoDB();
    }

    public String getRoutineName() {
        return controlledRoutine.getName();
    }

    public String getRoutineDescription() {
        return controlledRoutine.getDescription();
    }

    public List<Session> getRoutineSessions() {
        return controlledRoutine.getSessionsList();
    }

    public void removeRoutineSession(String name) {
        controlledRoutine.removeSession(name);
    }

    public void addRoutineSession(Session session) {
        controlledRoutine.addSession(session);
    }
}
