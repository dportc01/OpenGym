package com.example.opengym.Controller;

import com.example.opengym.Model.Routine;

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

    public void removeRoutineSession() {
        controlledRoutine.removeSession();
    }

    public void addRoutineSession() {
        controlledRoutine.addSession();
    }
}
