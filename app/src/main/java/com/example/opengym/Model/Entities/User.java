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
            writer.write("Rutina,Descripcion,Sesion,Fecha,DuracionDescanso,NombreEjercicio,TipoEjercicio,Repeticiones,Series,Peso,Duracion\n");
            String routineData = String.format(
                "%s,%s\n",
                routine.getName(),
                routine.getDescription()
            );
            for (Session session : routine.getSessionsList()) {
                String sessionData = String.format(
                    "%s%s,%s,%s,%d,",
                    routineData,
                    session.getName(),
                    session.getDate().toString(),
                    session.getRestDuration()
                );
                for (Exercise exercise : session.getExercisesList()) {
                    String exerciseData;
                    if (exercise.getType() == "Strength") {
                        StrengthExercise strenghExercise = (StrengthExercise) exercise;
                        exerciseData = String.format(
                            "%s%s,%s,%s,%d,%d,%.2f,-\n",
                            sessionData,
                            se.getName(),
                            "Fuerza",
                            se.getNumOfReps(),
                            se.getNumOfSets(),
                            se.getWeight()
                        );
                    } else {
                        TimeExercise timedExercise = (TimeExercise) exercise;
                        exerciseData = String.format(
                            "%s%s,%s,%s,-,-,-,%d\n",
                            sessionData,
                            te.getName(),
                            "Tiempo",
                            te.getTime()
                        );
                    }
                    writer.write(exerciseData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importUserRoutine(String filePath) {
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            Routine newRoutine = null;

            line = reader.readLine();
            if(line == null) {
                return;
            }

            String[] parts = line.split(",");
            String routineName = parts[0];
            String routineDescription = parts[1];
            newRoutine = new Routine(routineName, routineDescription);
            routinesList.add(newRoutine);

            currentSession = parts[2];
            while(line != null) {
                parts = line.split(",");
                currentSession = parts[2];
                ArrayList<IExercise> exercises = new ArrayList<IExercise>();
                while(parts[2] == currentSession) {
                    newExercise = extractExerciseData(reader, newRoutine);
                    exercises.add(newExercise);
                    line = reader.readLine();
                }
                Session newSession = new Session(parts[2], parts[3], Integer.parseInt(parts[4]), exercises);
                newRoutine.add(newSession);
                // TODO Crear metodo para crear y añadir sesiones a rutinas
                line = reader.readLine();
            }  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IExercise extractExerciseData(String[] dataRow) {
        IExercise newExercise = null;
        if (dataRow[7].equals("Strength")) {
            newExercise = new ExerciseFactory.createExercise(
                dataRow[6],
                Integer.parseInt(dataRow[8]),
                Integer.parseInt(dataRow[9]),
                Float.parseFloat(dataRow[10])
            );
        } else {
            newExercise = new ExerciseFactory.createExercise(
                dataRow[6], 
                Integer.parseInt(dataRow[11]));
        }
        return newExercise;
    }

    public void addRoutine(Routine routine) {
        routinesList.add(routine);
    }

    public void removeRoutine(Routine routine) {
        for (Routine r : routinesList) {
            if (r.getName().equals(routine.getName())) {
                routinesList.remove(r);
                return;
            }
        }
    }
}
