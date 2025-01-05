package com.example.opengym.Model.Entities;

public class StrengthExercise implements IExercise {

    private long id;
    private int numOfReps;
    private int numOfSets;
    private float weight;
    private String name;

    public StrengthExercise(String name, int numOfReps, int numOfSets, float weight) {
        this.name = name;
        this.numOfReps = numOfReps;
        this.numOfSets = numOfSets;
        this.weight = weight;
    }

    public StrengthExercise(String name, int numOfReps, int numOfSets, float weight, long id) {
        this.name = name;
        this.numOfReps = numOfReps;
        this.numOfSets = numOfSets;
        this.weight = weight;
        this.id = id;
    }

    public StrengthExercise() {
        this.name = "";
        this.numOfReps = 0;
        this.numOfSets = 0;
        this.weight = 0;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return "Strength";
    }

    public int getNumOfReps() {
        return numOfReps;
    }

    public int getNumOfSets() {
        return numOfSets;
    }

    public float getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumOfReps(int numOfReps) {
        this.numOfReps = numOfReps;
    }

    public void setNumOfSets(int numOfSets) {
        this.numOfSets = numOfSets;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }
}
