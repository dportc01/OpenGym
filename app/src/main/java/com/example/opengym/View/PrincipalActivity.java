package com.example.opengym.View;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.R; // TODO Cambiar
import com.example.opengym.Controller.PrincipalController;

import java.security.Principal;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private PrincipalController principalController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_selector);
        principalController = new PrincipalController(this); // TODO Usar context en vez de this
    }

    // TODO Este metodo se ejecuta al presionar una rutina en especifico
    public void onRoutineSelection(String routineName) {
        Intent intent = new Intent(PrincipalActivity.this, RoutineActivity.class);
        intent.putExtra("routineName", routineName);
        startActivity(intent);
    }

    /*
    public ArrayList<String> getScreenRoutines(){ // TODO Esto deberia sacarse de la base de datos
        return principalController.getUserRoutines();
    }

     */
}
