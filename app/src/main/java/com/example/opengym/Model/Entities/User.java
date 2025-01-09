package com.example.opengym.Model.Entities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.opengym.Model.DAO.RoutineDAO;
import com.example.opengym.Model.DAO.UserDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Objects;

public class User {
    private long id;
    private String name;
    private String password;
    private boolean premium;
    private ArrayList<Routine> routinesList;

    public User(String name, String password, boolean premium, long id) {
        this.name = name;
        this.password = password;
        this.premium = premium;
        this.id = id;
        this.routinesList = new ArrayList<>();
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.premium = false;
        this.routinesList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Routine> getRoutinesList() {
        return this.routinesList;
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

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public ArrayList<Routine> getRoutinesDB(Context context) {
        RoutineDAO routineDAO = new RoutineDAO(context);
        this.routinesList = routineDAO.readAll(this.id);
        return this.routinesList;
    }


    public void exportUserRoutine(Context context, Routine routine) {
        if (routine == null) {
            return;
        }

        routine.setInfoDB(context, routine.getId());

        for (Session session : routine.getSessionsList()) {
            session.setInfoDB(context, session.getId());
        }
    
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, routine.getName() + "_simple.csv");
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Rutina,Descripcion,Sesion,Fecha,DuracionDescanso,NombreEjercicio,TipoEjercicio,Repeticiones,Series,Peso,Duracion\n");
            for (Session session : routine.getSessionsList()) {
                for (IExercise exercise : session.getExercisesList()) {
                    String exerciseData;
                    if (exercise instanceof StrengthExercise) {
                        StrengthExercise strengthExercise = (StrengthExercise) exercise;
                        exerciseData = String.format(
                            "%s,%s,%s,%s,%d,%s,%s,%d,%d,%.2f,%d\n",
                            routine.getName(),
                            routine.getDescription(),
                            session.getName(),
                            session.getDate() != null ? session.getDate().toString() : "NoDate",
                            session.getRestDuration(),
                            exercise.getName(),
                            "Strength",
                            strengthExercise.getNumOfReps(),
                            strengthExercise.getNumOfSets(),
                            strengthExercise.getWeight(),
                            0
                        );
                    } else {
                        TimedExercise timedExercise = (TimedExercise) exercise;
                        exerciseData = String.format(
                            "%s,%s,%s,%s,%d,%s,%s,%d,%d,%.2f,%d\n",
                            routine.getName(),
                            routine.getDescription(),
                            session.getName(),
                            session.getDate() != null ? session.getDate().toString() : "NoDate",
                            session.getRestDuration(),
                            exercise.getName(),
                            "Cardio",
                            0,
                            0, 
                            0.0,
                            timedExercise.getTime()
                        );
                    }
                    writer.write(exerciseData);
                }
            }
        } catch (IOException e) {
            Log.e("User", "Error exporting user routine", e);
        }
    }


    public String importUserRoutine(String filePath, Context context) {
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            Routine newRoutine;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            line = reader.readLine();
            if(line == null) {
                return "No data found, No data found";
            }

            String[] parts = line.split(",");
            String routineName = parts[0];
            String routineDescription = parts[1];
            ArrayList<Session> sessionList = new ArrayList<Session>();
            newRoutine = new Routine(routineName, routineDescription, sessionList);
            this.addRoutine(routineName, routineDescription, context);

            String currentSession = parts[2];
            IExercise newExercise = null;
            while(line != null) {
                parts = line.split(",");
                currentSession = parts[2];
                ArrayList<IExercise> exercises = new ArrayList<IExercise>();
                Date date = formatter.parse(parts[3]);
                Session newSession = new Session(parts[2], date, Integer.parseInt(parts[4]), exercises);
                newRoutine.addSession(context, parts[2], date, Integer.parseInt(parts[4]));
                while(Objects.equals(parts[2], currentSession) && line != null) {
                    parts = line.split(",");
                    newExercise = extractExerciseData(parts);
                    if(parts[7].equals("Strength")) {
                        newSession.addStrengthExercise(newExercise, newSession.getId(), context);
                    } else {
                        newSession.addTimedExercise(newExercise, newSession.getId(), context);
                    }   
                    line = reader.readLine();
                }
                line = reader.readLine();
            }  
            String routineData = newRoutine.getName() + "," + newRoutine.getDescription(); 
            return routineData;
        } catch (IOException | ParseException e) {
            Log.e("User", "Error importing user routine", e);
        }
        return "No data found, No data found";
    }

    private IExercise extractExerciseData(String[] dataRow) {
        IExercise newExercise;

        if (dataRow[7].equals("Strength")) {
            newExercise = ExerciseFactory.createExercise(
                dataRow[6],
                Integer.parseInt(dataRow[8]),
                Integer.parseInt(dataRow[9]),
                Float.parseFloat(dataRow[10])
            );
        } else {
            newExercise = ExerciseFactory.createExercise(
                dataRow[6], 
                Integer.parseInt(dataRow[11]));
        }
        return newExercise;
    }

    public long addRoutine(String routineName, String routineDescription, Context context) {

        ArrayList<Session> routineSessions = new ArrayList<>();
        Routine newRoutine = new Routine(routineName, routineDescription, routineSessions);

        RoutineDAO routineDAO = new RoutineDAO(context);

        try {
            long id = routineDAO.create(newRoutine, this.id);

            if (id == -1) {
                return -1;
            }

            routinesList.add(newRoutine);
            newRoutine.setId(id);
            return id;
        }
        catch (Exception e) {
            Log.e("User", e.getMessage(), e);
            return -1;
        }
    }

    public long removeRoutine(String routineName, Context context) {
        RoutineDAO routineDAO = new RoutineDAO(context);
        try{
            for (Routine routine : routinesList) {
                if (routine.getName().equals(routineName)) {
                    long id = routineDAO.delete(routine.getId());
                    if (id == -1) {
                        return -1;
                    }
                    routinesList.remove(routine);
                }
            }
        } catch (Exception e) {
            Log.e("User", e.getMessage(), e);
            return -1;
        }
        return id;
    }

    public int updateRoutine(long routineId, Context context, String routineName, String routineDescription) {

        RoutineDAO routineTable = new RoutineDAO(context);

        try {
            Routine updatedRoutine = new Routine(routineName, routineDescription, new ArrayList<>());

            int affected = routineTable.update(updatedRoutine, routineId);

            if (affected > 0) {

                routinesList.removeIf(routine -> routine.getId() == routineId);
                routinesList.add(updatedRoutine);
            }

            return affected;
        } catch (Exception e) {
            Log.e("Database", "Exception occurred", e);
            return -1;
        }
    }

    public void changePremium(Context context) {
        UserDAO userDAO = new UserDAO(context);
        long userTime = Long.parseLong(userDAO.readDate(this.id));
        long currentTime = System.currentTimeMillis();
        long fourWeeksInMillis = 4L * 7 * 24 * 60 * 60 * 1000; // 4 weeks in milliseconds

        if((currentTime - userTime) >= fourWeeksInMillis && !this.premium) {
            this.premium = true;
            userDAO.update(this, this.id);
        }
    }

    public boolean getPremium() {
        return this.premium;
    }

}
