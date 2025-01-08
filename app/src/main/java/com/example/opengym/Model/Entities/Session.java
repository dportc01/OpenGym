package com.example.opengym.Model.Entities;

import android.content.Context;

import com.example.opengym.Model.DAO.StrengthExerciseDAO;
import com.example.opengym.Model.DAO.TimedExerciseDAO;

import java.util.ArrayList;
import java.util.Date;

public class Session {

    private long id;
    private String name;
    private Date date;
    private int restDuration;
    private ArrayList<IExercise> exercisesList;

    public Session(String name, Date date, int restDuration, ArrayList<IExercise> exercisesList) {
        this.name = name;
        this.date = date;
        this.restDuration = restDuration;
        this.exercisesList = exercisesList;
    }

    public Session(String name, Date date, int restDuration, long id) {
        this.name = name;
        this.date = date;
        this.restDuration = restDuration;
        this.id = id;
        this.exercisesList = new ArrayList<>();
    }

    public Session() {
        this.name = "";
        this.date = new Date();
        this.restDuration = 0;
        this.exercisesList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setInfoDB(Context context, long id) {
        StrengthExerciseDAO strengthExerciseTable = new StrengthExerciseDAO(context);
        TimedExerciseDAO timedExerciseTable = new TimedExerciseDAO(context);
        
        ArrayList<IExercise> exercisesTable = new ArrayList<>();
        exercisesTable.addAll(strengthExerciseTable.readAll(id));
        exercisesTable.addAll(timedExerciseTable.readAll(id));
        
        this.exercisesList = exercisesTable;
    }
    
    public Date getDate() {
        return date;
    }

    public int getRestDuration() {
        return restDuration;
    }

    public ArrayList<IExercise> getExercisesList() {
        return exercisesList;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void getInfoDB() {
        // TODO implement here
    }

    public void removeExercise(String name) {
        if (exercisesList.isEmpty()){
            return;
        }
        for (IExercise exercise : exercisesList) {
            if (exercise.getName().equals(name)) {
                exercisesList.remove(exercise);
                return;
            }
        }
    }

    public void addExercise(IExercise exercise) {
        exercisesList.add(exercise);
    }

    public long addStrengthExercise(IExercise STexercise){
        exercisesList.add(STexercise);
        return 0; // TODO cambiar lo que devuelve cuando funcione con el DAO
    }


    public long addTimedExercise(IExercise TIexercise){
        exercisesList.add(TIexercise);
        return 0; // TODO cambiar cuando funcione con el DAO
    }

}
