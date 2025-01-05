package com.example.opengym.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.Controller.RoutineController;
import com.example.opengym.R;

import java.util.ArrayList;
import java.util.List;

public class RoutineActivity extends AppCompatActivity {


    private TableLayout tableLayout;
    private Button btnAddSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_viewer);

        // Initialize views
        tableLayout = findViewById(R.id.table_layout);
        btnAddSession = findViewById(R.id.btn_add_session);

        // Set listener for "Añadir Sesión" button
        btnAddSession.setOnClickListener(v -> addSessionTable());

    }

    // Method to add session table dynamically
    private void addSessionTable() {
        // Inflate the session table row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View sessionTableRow = inflater.inflate(R.layout.session_table, null);

        // Add the session table to the TableLayout
        TableRow tableRow = new TableRow(this);
        tableRow.addView(sessionTableRow);
        tableLayout.addView(tableRow);

        // Add the "Añadir Ejercicio" row dynamically
        View addExerciseRow = inflater.inflate(R.layout.add_exercise, null);
        tableRow = new TableRow(this);
        tableRow.addView(addExerciseRow);
        tableLayout.addView(tableRow);

        // Set button listeners inside "Añadir Ejercicio"
        Button btnAddStrength = addExerciseRow.findViewById(R.id.btn_add_strength);
        Button btnAddDuration = addExerciseRow.findViewById(R.id.btn_add_duration);

        btnAddStrength.setOnClickListener(v -> addStrengthExerciseRow(addExerciseRow));
        btnAddDuration.setOnClickListener(v -> addDurationExerciseRow(addExerciseRow));
    }

    // Method to add strength exercise row
    private void addStrengthExerciseRow(View addExerciseRow) {
        // Hide the add exercise buttons
        addExerciseRow.setVisibility(View.GONE);

        // Inflate the strength exercise row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View strengthExerciseRow = inflater.inflate(R.layout.strength_exercise_row, null);

        // Add the strength exercise row to the TableLayout
        TableRow tableRow = new TableRow(this);
        tableRow.addView(strengthExerciseRow);
        tableLayout.addView(tableRow);

        // Optionally, add further behavior for handling input in strengthExerciseRow
    }

    // Method to add duration exercise row
    private void addDurationExerciseRow(View addExerciseRow) {
        // Hide the add exercise buttons
        addExerciseRow.setVisibility(View.GONE);

        // Inflate the duration exercise row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View durationExerciseRow = inflater.inflate(R.layout.duration_exercise_row, null);

        // Add the duration exercise row to the TableLayout
        TableRow tableRow = new TableRow(this);
        tableRow.addView(durationExerciseRow);
        tableLayout.addView(tableRow);
    }

    /* TODO Puede que lo tenga que recibir de la base de datos
    // Metodo para obtener lista de sesiones
    public ArrayList<String> getScreenSessions() {
        return routineController.getRoutineSessions();
    }

     */

    // TODO Misma historia que Principal, boton -> pasar a sesion
    public void onSessionSelection(String sessionName) {
        Intent intent = new Intent(RoutineActivity.this, SessionActivity.class);
        intent.putExtra("sessionName", sessionName);
        startActivity(intent);
    }
}
