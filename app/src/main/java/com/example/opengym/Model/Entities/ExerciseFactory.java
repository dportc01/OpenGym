package com.example.opengym.Model.Entities;

public class ExerciseFactory {
    public IExercise createExercise(String nombre, int duracion) {
        IExercise exercise = new TimedExercise(nombre, duracion);
        return exercise;
    }

    public IExercise createExercise(String nombre, int sets, int reps, float weight) {
        IExercise exercise = new StrengthExercise(nombre, sets, reps, weight);
        return exercise;
    }

    public IExercise createExercise(String tipo) {
        IExercise exercise = null;
        if (tipo.equals("Timed")) {
            exercise = new TimedExercise();
        } else if (tipo.equals("Strength")) {
            exercise = new StrengthExercise();
        }
        return exercise;
    }
}
