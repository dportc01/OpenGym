package com.example.opengym.Model.Entities;

public class TimedExercise implements IExercise {

    private long id;
    private String name;
    private int durationExercise;

    public TimedExercise(String name, int durationExercise) {
        this.name = name;
        this.durationExercise = durationExercise;
    }

    public TimedExercise(String name, int durationExercise, long id) {
        this.name = name;
        this.durationExercise = durationExercise;
        this.id = id;
    }

    public TimedExercise() {
        this.name = "";
        this.durationExercise = 0;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return "Timed";
    }

    public int getTime() {
        return durationExercise;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int durationExercise) {
        this.durationExercise = durationExercise;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }
}
