package com.example.opengym.Controller;

import com.example.opengym.Model.Entities.Routine;

public class RoutineController {
    private Routine controlledRoutine;

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

    public ArrayList<Session> getRoutineSessions() {
        return controlledRoutine.getSessionsList();
    }

    public void removeRoutineSession(String name) {
        controlledRoutine.removeSession(name);
    }

    public void addRoutineSession(Session session) {
        controlledRoutine.addSession(session);
    }
}
