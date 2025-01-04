package com.example.opengym.Model.Entities;

import java.util.ArrayList;
import java.util.Date;

public class Routine {
    private String name;
    private String description;
    private ArrayList<Session> sessionsList;

    public Routine(String name, String description, ArrayList<Session> sessionsList) {
        this.name = name;
        this.description = description;
        this.sessionsList = sessionsList;
    }

    public Routine() {
        this.name = "";
        this.description = "";
        this.sessionsList = new ArrayList<Session>();
    }

    public void getInfoDB() {
        // TODO implement here
    }

    public void removeSession(String name) {
        if (sessionsList.isEmpty()){
            return;
        }
        for (Session session : sessionsList) {
            if (session.getName().equals(name)) {
                sessionsList.remove(session);
                return;
            }
        }
    }

    public void addSession(String sessionName, Date date, int restDuration) {
        ArrayList<IExercise> exercises = new ArrayList<>();
        Session session = new Session(sessionName, date, restDuration, exercises);
        sessionsList.add(session);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Session> getSessionsList() {
        return sessionsList;
    }
}
