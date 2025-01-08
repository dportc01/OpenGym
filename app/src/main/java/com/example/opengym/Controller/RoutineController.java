package com.example.opengym.Controller;

import android.content.Context;

import com.example.opengym.Model.DAO.RoutineDAO;
import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.Entities.Session;

import java.util.ArrayList;

public class RoutineController {
    private final Routine controlledRoutine;

    public RoutineController(Context context, long id) {
        RoutineDAO routineDAO = new RoutineDAO(context);
        this.controlledRoutine = routineDAO.read(id);
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

    public long removeRoutineSession(String name, Context context) {
        return controlledRoutine.removeSession(name, context);
    }

    public long getSessionId(String routineName) {
        ArrayList<Session> sessions = controlledRoutine.getSessionsList();
        long id = -1;
        for (Session session : sessions) {
            if (session.getName().equals(routineName)) {
                id = session.getId();
                break;
            }
        }
        return id;
    }
}
