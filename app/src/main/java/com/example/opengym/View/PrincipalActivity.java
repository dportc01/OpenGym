package com.example.opengym.View;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.R; // TODO Cambiar

import java.security.Principal;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private PrincipalController principalController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_selector);
        User user; // TODO Sacar de la base de datos
        principalController = new PrincipalController(user);
    }

    // TODO Este metodo se ejecuta al presionar una rutina en especifico
    public void onRoutineSelection(String routineName) {
        Intent intent = new Intent(PrincipalActivity.this, RoutineActivity.class);
        intent.putExtra("routineName", routineName);
        startActivity(intent);
    }

    public List<Routine> getScreenRoutines(){
        return principalController.getUserRoutines();
    }
}
