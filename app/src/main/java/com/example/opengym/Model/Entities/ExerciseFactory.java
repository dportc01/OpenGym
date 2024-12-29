package com.example.opengym.Model.Entities;

public class ExerciseFactory {
    public IExercise createExercise(String exerciseType) {
        IExercise exercise = null;
        if (exerciseType == null) {
            return null;
        }
        if (exerciseType.equalsIgnoreCase("TIMED")) {
            exercise = new TimedExercise();
        } else if (exerciseType.equalsIgnoreCase("STRENGTH")) {
            exercise = new StrengthExercise();
        }
        return exercise;
    }
}
