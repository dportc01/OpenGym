package com.example.opengym.Controller;

import static android.content.Intent.getIntent;

import android.content.Context;

import java.util.List;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.Entities.User;

import java.util.ArrayList;

public class PrincipalController {

    private User controlledUser;

    public PrincipalController(Context context, String userName) {
        UserDAO userDAO = new UserDAO(context);
        controlledUser = userDAO.read(userName);
    }

    public String getUserName() {
        return controlledUser.getName();
    }

    public void exportUserRoutine(String routineName) {
        controlledUser.exportUserRoutine(routineName);
    }

    public void importUserRoutine(String filePath) {
        //controlledUser.importRoutine(filePath);
    }

    public void removeUserRoutine(String routineName, Context context) {
        controlledUser.removeRoutine(routineName, context);
    }

    public long addUserRoutine(Context context, String routineName, String routineDescription) {
        return controlledUser.addRoutine(routineName, routineDescription, context);
    }

    public List<String> getUserRoutines(Context context) {
        ArrayList<Routine> routinesList = controlledUser.getRoutinesDB(context);
        ArrayList<String> routinesNamesList = new ArrayList<>();
        for (Routine routine: routinesList) {
            routinesNamesList.add(routine.getName());
        }
        return routinesNamesList;
    }

}
