package com.example.opengym.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
        btnAddSession.setOnClickListener(v -> promptForSessionDetails());
    }

    // Prompt the user to enter session name and rest duration
    private void promptForSessionDetails() {
        // Create an AlertDialog for user input
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Sesión");

        // Create a vertical LinearLayout to hold the input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        // Add an EditText for session name
        EditText etSessionName = new EditText(this);
        etSessionName.setHint("Nombre de la sesión");
        etSessionName.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(etSessionName);

        // Add an EditText for rest duration
        EditText etRestDuration = new EditText(this);
        etRestDuration.setHint("Duración del descanso (minutos)");
        etRestDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(etRestDuration);

        // Set the layout to the dialog
        builder.setView(layout);

        // Set positive and negative buttons
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String sessionName = etSessionName.getText().toString().trim();
            String restDuration = etRestDuration.getText().toString().trim();

            if (sessionName.isEmpty() || restDuration.isEmpty()) {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                addSessionTable(sessionName, restDuration);
            }
        });

        builder.setNegativeButton("Cancelar", null);

        // Show the dialog
        builder.show();
    }

    // Method to add session table dynamically with session details
    private void addSessionTable(String sessionName, String restDuration) {
        // Add the session details as a TextView
        TableRow sessionDetailsRow = new TableRow(this);
        TextView sessionDetailsText = new TextView(this);
        sessionDetailsText.setText(String.format("Sesión: %s | Descanso: %s minutos", sessionName, restDuration));
        sessionDetailsText.setTextSize(16);
        sessionDetailsText.setPadding(8, 8, 8, 8);
        sessionDetailsRow.addView(sessionDetailsText);
        tableLayout.addView(sessionDetailsRow);

        // Inflate the session table row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View sessionTableRow = inflater.inflate(R.layout.session_table, null);
        TableRow tableRow = new TableRow(this);
        tableRow.addView(sessionTableRow);
        tableLayout.addView(tableRow);

        // Add the "Añadir Ejercicio" row dynamically
        addAddExerciseRow();
    }

    // Method to add the "Añadir Ejercicio" buttons row
    private void addAddExerciseRow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View addExerciseRow = inflater.inflate(R.layout.add_exercise, null);

        TableRow tableRow = new TableRow(this);
        tableRow.addView(addExerciseRow);
        tableLayout.addView(tableRow);

        // Set button listeners inside "Añadir Ejercicio"
        Button btnAddStrength = addExerciseRow.findViewById(R.id.btn_add_strength);
        Button btnAddDuration = addExerciseRow.findViewById(R.id.btn_add_duration);

        btnAddStrength.setOnClickListener(v -> {
            addStrengthExerciseRow(addExerciseRow);
        });

        btnAddDuration.setOnClickListener(v -> {
            addDurationExerciseRow(addExerciseRow);
        });
    }

    // Method to add strength exercise row
    private void addStrengthExerciseRow(View addExerciseRow) {
        // Remove the current "Añadir Ejercicio" row
        tableLayout.removeView((View) addExerciseRow.getParent());

        // Inflate the strength exercise row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View strengthExerciseRow = inflater.inflate(R.layout.strength_exercise_row, null);

        // Add the strength exercise row to the TableLayout
        TableRow tableRow = new TableRow(this);
        tableRow.addView(strengthExerciseRow);
        tableLayout.addView(tableRow);

        // Add the "Añadir Ejercicio" buttons row again
        addAddExerciseRow();
    }

    // Method to add duration exercise row
    private void addDurationExerciseRow(View addExerciseRow) {
        // Remove the current "Añadir Ejercicio" row
        tableLayout.removeView((View) addExerciseRow.getParent());

        // Inflate the duration exercise row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View durationExerciseRow = inflater.inflate(R.layout.duration_exercise_row, null);

        // Add the duration exercise row to the TableLayout
        TableRow tableRow = new TableRow(this);
        tableRow.addView(durationExerciseRow);
        tableLayout.addView(tableRow);

        // Add the "Añadir Ejercicio" buttons row again
        addAddExerciseRow();
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
