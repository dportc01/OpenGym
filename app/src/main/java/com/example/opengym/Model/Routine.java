package com.example.opengym.Model;

public class Routine {
    private String name;
    private String description;

    public Routine(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void removeSession() {
        // TODO implement here
    }

    public void addSession() {
        // TODO implement here
    }

    public void exportRoutine() {
        // TODO implement here
    }

    public void importRoutine() {
        // TODO implement here
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

}
