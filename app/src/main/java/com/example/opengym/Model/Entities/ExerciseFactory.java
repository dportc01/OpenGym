package com.example.opengym.Model.Entities;

public class ExerciseFactory {

    public static IExercise createExercise(String nombre, int duracion) {
        return new TimedExercise(nombre, duracion);
    }

    public static IExercise createExercise(String nombre, int duracion, long id) {
        return new TimedExercise(nombre, duracion, id);
    }

    public static IExercise createExercise(String nombre, int sets, int reps, float weight) {
        return new StrengthExercise(nombre, sets, reps, weight);
    }

    public static IExercise createExercise(String nombre, int sets, int reps, float weight, long id) {
        return new StrengthExercise(nombre, sets, reps, weight, id);
    }

    public static IExercise createExercise(String tipo) {

        if (tipo.equals("Timed")) {
            return new TimedExercise();
        } else if (tipo.equals("Strength")) {
            return new StrengthExercise();
        }
        return null;
    }
}
