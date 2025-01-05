package com.example.opengym.Controller;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.Entities.User;

import java.util.ArrayList;

public class PrincipalController {
    private final User controlledUser;

    public PrincipalController(Context context, String userName) {
        this.controlledUser = new User();
        controlledUser.getInfoDB(userName, context);
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

    public void removeUserRoutine(String routineName) {
        controlledUser.removeRoutine(routineName);
    }

    public void addUserRoutine(String routineName, String routineDescription) {
        controlledUser.addRoutine(routineName, routineDescription);
    }

    public List<String> getUserRoutines() {
        ArrayList<Routine> routinesList = controlledUser.getRoutinesList();
        ArrayList<String> routinesNamesList = new ArrayList<>();
        for (Routine routine: routinesList) {
            routinesNamesList.add(routine.getName());
        }
        return routinesNamesList;
    }

}
