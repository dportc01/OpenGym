package com.example.opengym.Model.Entities;

import java.util.ArrayList;
import java.util.Date;

public class Session {

    private String name;
    private Date date;
    private int restDuration;
    private List<IExercise> exercisesList;

    public Session(String name, Date date, int restDuration, List<IExercise> exercisesList) {
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

    public Date getDate() {
        return date;
    }

    public int getRestDuration() {
        return restDuration;
    }

    public List<IExercise> getExercisesList() {
        return exercisesList;
    }

    public void getInfoDB() {
        // TODO implement here
    }

    public void removeExercise() {
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

}
