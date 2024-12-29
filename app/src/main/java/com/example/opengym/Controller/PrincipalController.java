package com.example.opengym.Controller;

import com.example.opengym.Model.User;

import java.util.ArrayList;

public class PrincipalController {
    private final User controlledUser;

    public PrincipalController() {
        this.controlledUser = new User();
        controlledUser.getInfoDB();
    }

    public String getUserName() {
        return controlledUser.getName();
    }

    public void exportUserRoutine(String routineName) {
        controlledUser.exportRoutine(routineName);
    }

    public void importUserRoutine(String filePath) {
        controlledUser.importRoutine(filePath);
    }

    public void removeUserRoutine(Routine routine) {
        controlledUser.removeRoutine(routine);
    }

    public void addUserRoutine(Routine routine) {
        controlledUser.addRoutine(routine);
    }

    public ArrayList<String> getUserRoutinesName() {
        ArrayList<String> routinesName = new ArrayList<String>();
        for(int i = 0; i < controlledUser.getRoutinesList().size(); i++){
            routinesName.add(controlledUser.getRoutinesList().get(i).getName());
        }
        return routinesName;
    }
}
