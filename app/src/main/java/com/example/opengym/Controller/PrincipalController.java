package com.example.opengym.Controller;

import com.example.opengym.Model.Entities.User;

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

    public void exportUserRoutine(String routineName){
        user.exportRoutine(routineName);
    }

    public void importUserRoutine() {
        // TODO
    }

    public void removeUserRoutine() {
        // TODO
    }

    public void addUserRoutine() {
        // TODO
    }

    public ArrayList<String> getUserRoutinesName() {
        // TODO
        return null;
    }
}
