package com.example.opengym.Controller;

import android.content.Context;
import java.util.List;

import com.example.opengym.Model.Entities.Routine;
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
        //controlledUser.exportRoutine(routineName);
        // a
    }

    public void importUserRoutine(String filePath) {
        //controlledUser.importRoutine(filePath);
    }

    public void removeUserRoutine(Routine routine) {
        controlledUser.removeRoutine(routine);
    }

    public void addUserRoutine(Routine routine) {
        controlledUser.addRoutine(routine);
    }

    public List<Routine> getUserRoutines() {
        return controlledUser.getRoutinesList();
    }
}
