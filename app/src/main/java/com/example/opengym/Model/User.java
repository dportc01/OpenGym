package com.example.opengym.Model;

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

    public void exportUserRoutine() {
        // TODO
    }

    public void importUserRoutine() {
        // TODO
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
