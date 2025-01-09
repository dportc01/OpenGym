package com.example.opengym.Model.Entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

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
        for (Session session: sessionsList) {
            session.setInfoDB(context, session.getId());
        }
    }


    public long removeSession(String name, Context context) {
        SessionDAO sessionDAO = new SessionDAO(context);
        if (sessionsList.isEmpty()){
            return -1;
        }
        try {
            for (Session session : sessionsList) {
                if (session.getName().equals(name)) {
                    long id = sessionDAO.delete(session.getId());
                    if (id == -1) {
                        return -1;
                    }
                    sessionsList.remove(session);
                }
            }
        }    catch (Exception e) {
            Log.e("Routine", e.getMessage(), e);
            return -1;
        }
        return -1;
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
