package com.example.opengym.Model;

public class User {
    private String name;
    private String email;
    private String password;
    private boolean premium;

    public User(String name, String email, String password, boolean premium) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.premium = premium;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

}
