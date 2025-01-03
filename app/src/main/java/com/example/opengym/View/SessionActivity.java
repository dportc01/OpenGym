package com.example.opengym.View;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class SessionActivity extends AppCompatActivity {
    private SessionController sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_selector);
        //Context sessionName = getIntent().getStringExtra("sessionName");
        //Session session; // TODO Obtener sesión de la base de datos o del modelo, otra vez
        //sessionController = new SessionController(session);
    }

    /*
    public List<Session> getScreenExercises(){
        return sessionController.getSessionExercises();
    }
     */
}
