package com.example.opengym.Model;

public class StrengthExercise implements IExercise {

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
}
