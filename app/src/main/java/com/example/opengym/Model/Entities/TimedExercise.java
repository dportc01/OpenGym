package com.example.opengym.Model.Entities;

public class TimedExercise implements IExercise {
    private String name;
    private int durationExercise;

    public TimedExercise(String name, int durationExercise) {
        this.name = name;
        this.durationExercise = durationExercise;
    }

    public TimedExercise() {
        this.name = "";
        this.durationExercise = 0;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return durationExercise;
    }
}
