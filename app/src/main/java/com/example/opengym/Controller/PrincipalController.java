package com.example.opengym.Controller;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.Entities.User;

import java.util.ArrayList;

public class PrincipalController {

    private final User controlledUser;

    public PrincipalController(Context context, String userName) {
        UserDAO userDAO = new UserDAO(context);
        controlledUser = userDAO.read(userName);
    }

    public void exportUserRoutine(Context context, String routineName) {
        ArrayList<Routine> routinesList = controlledUser.getRoutinesDB(context);
        for (Routine routine : routinesList) {
            if (routine.getName().equals(routineName)) {
                controlledUser.exportUserRoutine(context, routine);
                break;
            }
        }
    }

    public String importUserRoutine(Uri uri, Context context) {
        return controlledUser.importUserRoutine(context, uri);
    }

    public long removeUserRoutine(String routineName, Context context) {
        return controlledUser.removeRoutine(routineName ,context);
    }

    public long addUserRoutine(Context context, String routineName, String routineDescription) {
        return controlledUser.addRoutine(routineName, routineDescription, context);
    }

    public List<String> getUserRoutines(Context context) {
        ArrayList<Routine> routinesList = controlledUser.getRoutinesDB(context);
        ArrayList<String> routinesNamesList = new ArrayList<>();
        for (Routine routine: routinesList) {
            routinesNamesList.add(routine.getName() + "," + routine.getDescription());
        }
        return routinesNamesList;
    }

    public long getRoutineId(String routineName) {
        ArrayList<Routine> routines = controlledUser.getRoutinesList();
        long id = -1;
        for (Routine routine: routines) {
            if (routine.getName().equals(routineName)) {
                id = routine.getId();
                break;
            }
        }
        return id;
    }

    public long updateRoutine(Context context, String routineName, String routineDescription, String oldName) {

        long id = getRoutineId(oldName);

         if (id == -1) {
            return 0;
         }
         else {
            return controlledUser.updateRoutine(id, context, routineName, routineDescription);
         }
    }

    public void changePremium(Context context) {
        controlledUser.changePremium(context);
    }

    public boolean isPremium() {
        return controlledUser.getPremium();
    }

    public void logOut(Context context) {
        controlledUser.removeLastLogin(context);
    }

}

