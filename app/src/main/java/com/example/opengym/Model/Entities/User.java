package com.example.opengym.Model.Entities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class User {
    private String name;
    private String password;
    private boolean premium;
    private ArrayList<Routine> routinesList;

    public User(String name, String password, boolean premium, ArrayList<Routine> routinesList) {
        this.name = name;
        this.password = password;
        this.premium = premium;
        this.routinesList = routinesList;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.premium = false;
        this.routinesList = new ArrayList<Routine>();
    }

    public User() {
        this.name = "";
        this.password = "";
        this.premium = false;
        this.routinesList = new ArrayList<Routine>();
    }

    // Get the user's information from the database
    public void getInfoDB() {
        // TODO
    }
    public String getName() {
        return name;
    }

    public void exportUserRoutine(String name) {
        Routine routine = null;
        for (Routine r : routinesList) {
            if (r.getName().equals(name)) {
                routine = r;
                break;
            }
        }
        if (routine == null) {
            return;
        }
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, name + ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            write.write(String.format("NombreRutina: \"%s\"\n", routine.getName()));
            writer.write(String.format("Descripcion: \"%s\"\n", routine.getDescription()));
            writer.write("NombreSesion,TipoEjercicio,NombreEjercicio,Repeticiones,Series,Peso,Duración\n");
            for (Session session : routine.getSessionsList()) {
                for (Exercise exercise : session.getExercisesList()) {
                    String row;
                    if (exercise instanceof StrengthExercise) {
                        StrengthExercise strenghExercise = (StrengthExercise) exercise;
                        row = String.format(
                            "%s,%s,%s,%d,%d,%.2f,-\n",
                            session.getName(),
                            "Fuerza",
                            strenghExercise.getName(),
                            strenghExercise.getNumOfReps(),
                            strenghExercise.getNumOfSets(),
                            strenghExercise.getWeight()
                        );
                    } else {
                        TimeExercise timedExercise = (TimeExercise) exercise;
                        row = String.format(
                            "%s,%s,%s,-,-,-,%d\n",
                            session.getName(),
                            "Tiempo",
                            timedExercise.getName(),
                            timedExercise.getTime()
                        );
                    }
                    writer.write(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importUserRoutine() {

        // TODO
    }

    public void addRoutine(String name) {
        routinesList.add(routine);
    }

    public void removeRoutine(Routine routine) {
        routinesList.remove(routine);
    }

    public String getPassword() {
        return password;
    }

    public boolean getPremium() {
        return premium;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

}
