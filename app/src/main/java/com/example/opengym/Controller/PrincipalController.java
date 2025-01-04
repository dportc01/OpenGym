package com.example.opengym.Controller;

import android.content.Context;
import java.util.List;

import com.example.opengym.Model.Entities.User;

import java.util.ArrayList;

public class PrincipalController {
    private final User controlledUser;

    public PrincipalController(Context context) {
        this.controlledUser = new User();
        //controlledUser.getInfoDB();
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

    /* TODO cambiar para transformar la lista de rutinas en strings o dejarlo a la base de datos
    public List<String> getUserRoutines() {
        return controlledUser.getRoutinesList();
    }

     */
}
