package com.example.opengym.Controller;

import android.content.Context;

import com.example.opengym.Model.DAO.RoutineDAO;
import com.example.opengym.Model.Entities.IExercise;
import com.example.opengym.Model.Entities.Routine;
import com.example.opengym.Model.Entities.Session;
import com.example.opengym.Model.Entities.StrengthExercise;
import com.example.opengym.Model.Entities.TimedExercise;

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

    public ArrayList<ArrayList<String>> returnExercises(int sessionPosition) {

        int i = 0;
        ArrayList<ArrayList<String>> exercisesArray = new ArrayList<>();
        ArrayList<String> exerciseFields;

        try {
            while (i < controlledRoutine.getSessionsList().get(sessionPosition).getExercisesList().size()) {
                IExercise exercise = controlledRoutine.getSessionsList().get(sessionPosition).getExerciseAt(i);
                if (exercise.getType().equals("Strength")) {
                    StrengthExercise strExercise = (StrengthExercise) exercise;
                    exerciseFields = new ArrayList<>();

                    exerciseFields.add(strExercise.getType());
                    exerciseFields.add(strExercise.getName());
                    exerciseFields.add(String.valueOf(strExercise.getNumOfReps()));
                    exerciseFields.add(String.valueOf(strExercise.getNumOfSets()));
                    exerciseFields.add(String.valueOf(strExercise.getWeight()));
                }
                else {
                    TimedExercise timedExercise = (TimedExercise) exercise;
                    exerciseFields = new ArrayList<>();

                    exerciseFields.add(timedExercise.getType());
                    exerciseFields.add(timedExercise.getName());
                    exerciseFields.add(String.valueOf(timedExercise.getTime()));
                }

                exercisesArray.add(exerciseFields);
                i++;
            }
        } catch (Exception e) {
            return null;
        }

        return exercisesArray;
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

    public ArrayList<Session> getSessionsList() {
        return controlledRoutine.getSessionsList();
    }
}
