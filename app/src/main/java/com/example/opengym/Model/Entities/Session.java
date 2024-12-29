package com.example.opengym.Model.Entities;

import java.util.ArrayList;
import java.util.Date;

public class Session {

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

    public Session() {
        this.name = "";
        this.date = new Date();
        this.restDuration = 0;
        this.exercisesList = new ArrayList<IExercise>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<IExercise> getExercisesList() {
        return exercisesList;
    }

    public void getInfoDB() {
        // TODO implement here
    }

    public void removeExercise() {
        // TODO implement here
    }

    public void addExercise() {
        // TODO implement here
    }

}
