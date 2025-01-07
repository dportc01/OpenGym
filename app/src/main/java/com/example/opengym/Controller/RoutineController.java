package com.example.opengym.Controller;

import android.content.Context;

import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.Entities.Session;

import java.util.ArrayList;

public class RoutineController {
    private final Routine controlledRoutine;

    public RoutineController(Routine routine) {
        this.controlledRoutine = routine;
    }

    public void loadSessions(Context context) {
        controlledRoutine.setInfoDB(context, controlledRoutine.getId());
    }

    public long addSession(Context context, String sessionName, String restDuration) {
        return controlledRoutine.addSession(context, sessionName, null, Integer.parseInt(restDuration));
    }

    public String getSessionName(int position) {
        try {
            return controlledRoutine.getSessionsList().get(position).getName();
        } catch (Exception e) {
            return  null;
        }
    }

    public String getSessionRest(int position) {
        try {
            return String.valueOf(controlledRoutine.getSessionsList().get(position).getRestDuration());
        } catch (Exception e) {
            return null;
        }
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

    /* TODO Cambiar para pasar todos los parametros que necesite
    public void addRoutineSession(Session session) {
        controlledRoutine.addSession(session);
    }

     */
}
