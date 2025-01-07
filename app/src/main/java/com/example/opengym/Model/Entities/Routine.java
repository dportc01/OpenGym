package com.example.opengym.Model.Entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.opengym.Model.DAO.RoutineDAO;
import com.example.opengym.Model.DAO.SessionDAO;

import java.util.ArrayList;
import java.util.Date;

public class Routine implements Parcelable {
    private long id;
    private String name;
    private String description;
    private ArrayList<Session> sessionsList;

    public Routine(String name, String description, ArrayList<Session> sessionsList) {
        this.name = name;
        this.description = description;
        this.sessionsList = sessionsList;
    }

    public Routine(String name, String description, long id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Routine() {
        this.name = "";
        this.description = "";
        this.sessionsList = new ArrayList<Session>();
    }

    protected Routine(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<Routine> CREATOR = new Creator<Routine>() {
        @Override
        public Routine createFromParcel(Parcel in) {
            return new Routine(in);
        }

        @Override
        public Routine[] newArray(int size) {
            return new Routine[size];
        }
    };


    public void setInfoDB(Context context, long id) {
        SessionDAO sessionTable = new SessionDAO(context);
        this.sessionsList = sessionTable.readAll(id);
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

    public long addSession(Context context, String sessionName, Date date, int restDuration) {

        ArrayList<IExercise> exercises = new ArrayList<>();
        Session session = new Session(sessionName, date, restDuration, exercises);
        SessionDAO sessionTable = new SessionDAO(context);

        try {
            long sessionId = sessionTable.create(session, this.id);

            if (sessionId == -1) {
                return -1;
            }

            session.setId(sessionId);

            sessionsList.add(session);
            return sessionId;
        }
        catch (Exception e) {
            return -1;
        }
    }

    // Moví este metodo a Users ya que users necesita RoutineDAO para getRoutinesDB sry UwU
//    public long addRoutineDB(Context context, long userID) {
//        RoutineDAO routineDAO = new RoutineDAO(context);
//
//        try {
//            long id = routineDAO.create(this, userID);
//
//            if (id == -1) {
//                return -1;
//            }
//
//            this.setId(id);
//            return id;
//        }
//        catch (Exception e) {
//            return -1;
//        }

//    }

    public void removeRoutineDB(Context context) {
        RoutineDAO routineDAO = new RoutineDAO(context);
        routineDAO.delete(this.id);
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

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
    }
}
