package com.example.opengym.View;

import com.example.opengym.Controller.SessionController;
import com.example.opengym.R;


import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SessionActivity extends AppCompatActivity {
    private SessionController sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_viewer);
        // Context sessionName = getIntent().getStringExtra("sessionName");// TODO Obtener sesión de la base de datos o del modelo, otra vez
        sessionController = new SessionController(this);
    }

    public ArrayList<String> getScreenExercises(){
        return sessionController.getSessionExercises();
    }
}
