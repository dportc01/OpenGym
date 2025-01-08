package com.example.opengym.View;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import com.example.opengym.Controller.SessionController;
import com.example.opengym.R;

public class SessionEditorActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private SessionController sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_editor);

        // Initialize views
        tableLayout = findViewById(R.id.table_layout_editor);


    }

    //private void addSessionTable(String sessionName, String restDuration) {
    //    // Add the session name and rest duration row
    //    TableRow sessionDetailsRow = new TableRow(this);
    //    TextView sessionDetailsText = new TextView(this);
    //    sessionDetailsText.setText(String.format("Sesión: %s | Descanso: %s minutos", sessionName, restDuration));
    //    sessionDetailsText.setTextSize(16);
    //    sessionDetailsText.setPadding(8, 8, 8, 8);
    //    sessionDetailsRow.addView(sessionDetailsText);
    //    tableLayout.addView(sessionDetailsRow);

    //    // Add the session table row below the session details row
    //    LayoutInflater inflater = LayoutInflater.from(this);
    //    View sessionTableRow = inflater.inflate(R.layout.session_table, null);
    //    TableRow tableRow = new TableRow(this);
    //    tableRow.addView(sessionTableRow);
    //    tableLayout.addView(tableRow);
    //    SessionController sesscontroller = new SessionController(this, sessionName);
    //    // Add the "Añadir Ejercicio" button row below the session table row
    //    addAddExerciseRow(sessionDetailsRow, sesscontroller);  // Pass the session row to add exercises below it
    //}


    //private void addAddExerciseRow(TableRow sessionDetailsRow, SessionController sesscontroller) {
    //    // Inflate the "Añadir Ejercicio" row layout with buttons
    //    LayoutInflater inflater = LayoutInflater.from(this);
    //    View addExerciseRow = inflater.inflate(R.layout.add_exercise, null);

    //    // Create a TableRow to hold the "Añadir Ejercicio" buttons
    //    TableRow tableRow = new TableRow(this);
    //    tableRow.addView(addExerciseRow);

    //    // Add the exercise row below the session row
    //    int index = tableLayout.indexOfChild(sessionDetailsRow) + 2;  // Add 2 to place it after both the session and session table rows
    //    tableLayout.addView(tableRow, index);

    //    // Set button listeners for adding strength or duration exercises
    //    Button btnAddStrength = addExerciseRow.findViewById(R.id.btn_add_strength);
    //    Button btnAddDuration = addExerciseRow.findViewById(R.id.btn_add_duration);

    //    btnAddStrength.setOnClickListener(v -> addStrengthExerciseRow(addExerciseRow, sessionDetailsRow, sesscontroller));
    //    btnAddDuration.setOnClickListener(v -> addDurationExerciseRow(addExerciseRow, sessionDetailsRow, sesscontroller));
    //}

    private void addDurationExerciseRow(View addExerciseRow, TableRow sessionDetailsRow, SessionController sesscontroller) {
        // Remove the "Añadir Ejercicio" row (which was clicked)
        tableLayout.removeView((View) addExerciseRow.getParent());

        // Inflate the duration exercise row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View durationExerciseRow = inflater.inflate(R.layout.duration_exercise_row, null);

        // Create a TableRow for the duration exercise
        TableRow tableRow = new TableRow(this);
        tableRow.addView(durationExerciseRow);

        // Add the exercise row below the sessionDetailsRow
        int index = tableLayout.indexOfChild(sessionDetailsRow) + 2;  // Add 2 to insert below session and session table
        tableLayout.addView(tableRow, index);

        addDurationExerciseDB(this, durationExerciseRow, sesscontroller);

        // Add the "Añadir Ejercicio" buttons row again below the newly added exercise
    //    addAddExerciseRow(sessionDetailsRow, sesscontroller);
    }

    private void addDurationExerciseDB(Context context, View durationExerciseRow, SessionController sesscontroller) {
        EditText etName = durationExerciseRow.findViewById(R.id.et_name);
        EditText etDuration = durationExerciseRow.findViewById(R.id.et_duration);

        String name = etName.getText().toString().trim();
        String durationTI = etDuration.getText().toString().trim();

        if (name.isEmpty() || durationTI.isEmpty()) {
            Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int duration = Integer.parseInt(durationTI);
            sesscontroller.addTimedExercise(context, name, duration);

        } catch (NumberFormatException e) {
            Toast.makeText(context, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void addStrengthExerciseRow(View addExerciseRow, TableRow sessionDetailsRow, SessionController sesscontroller) {
        // Remove the "Añadir Ejercicio" row (which was clicked)
        tableLayout.removeView((View) addExerciseRow.getParent());

        // Inflate the strength exercise row layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View strengthExerciseRow = inflater.inflate(R.layout.strength_exercise_row, null);

        // Create a TableRow for the strength exercise
        TableRow tableRow = new TableRow(this);
        tableRow.addView(strengthExerciseRow);

        // Add the exercise row below the sessionDetailsRow
        int index = tableLayout.indexOfChild(sessionDetailsRow) + 2;  // Add 2 to insert below session and session table
        tableLayout.addView(tableRow, index);

        addStrengthExerciseDB(this, strengthExerciseRow, sesscontroller);

        // Add the "Añadir Ejercicio" buttons row again below the newly added exercise
    //    addAddExerciseRow(sessionDetailsRow, sesscontroller);
    }

    public void addStrengthExerciseDB(Context context, View strengthExerciseRow, SessionController sesscontroller) {
        EditText etName = strengthExerciseRow.findViewById(R.id.et_name);
        EditText etSeries = strengthExerciseRow.findViewById(R.id.et_series);
        EditText etReps = strengthExerciseRow.findViewById(R.id.et_reps);
        EditText etWeight = strengthExerciseRow.findViewById(R.id.et_weight);

        String name = etName.getText().toString().trim();
        String seriesStr = etSeries.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (name.isEmpty() || seriesStr.isEmpty() || repsStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int series = Integer.parseInt(seriesStr);
            int reps = Integer.parseInt(repsStr);
            int weight = Integer.parseInt(weightStr);
            sesscontroller.addStrenghExercise(context, name, series, reps, weight);

        } catch (NumberFormatException e) {
            Toast.makeText(context, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
        }
    }
}
