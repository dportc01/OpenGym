package com.example.opengym.View;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.Controller.SessionController;
import com.example.opengym.R;

import java.util.ArrayList;
import java.util.Iterator;

public class SessionTrackingActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private SessionController sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_sesion);

        // Initialize views
        tableLayout = findViewById(R.id.table_layout_tracker);
        Button btnSaveSession = findViewById(R.id.btn_save_session);

        // Get session details from intent
        long sessionId = getIntent().getLongExtra("session_id", -1);
        String sessionName = getIntent().getStringExtra("session_name");
        String restDuration = getIntent().getStringExtra("rest_duration");
        sessionController = new SessionController(this, sessionId);

        addSessionTable(sessionName, restDuration);

        // Load exercises for the session
        loadExercises();

        // Set listener for "Guardar Sesión" button
        btnSaveSession.setOnClickListener(v -> saveSession());
    }

    private void addSessionTable(String sessionName, String restDuration) {
        TableRow sessionRow = new TableRow(this);
        TextView sessionDetails = new TextView(this);
        sessionDetails.setText(String.format("Sesión: %s | Descanso: %s minutos", sessionName, restDuration));
        sessionDetails.setPadding(8, 8, 8, 8);
        sessionRow.addView(sessionDetails);

        tableLayout.addView(sessionRow);
    }

    private void loadExercises() {


        LayoutInflater inflater = LayoutInflater.from(this);
        View exerciseRow;

        ArrayList<ArrayList<String>> exercisesList = sessionController.returnExercises();
        Iterator<ArrayList<String>> exercisesListIterator = exercisesList.iterator();
        ArrayList<String> exercise;

        boolean hasPreviousExercises = sessionController.loadOldestSession(this);
        ArrayList<ArrayList<String>> prevExercises;
        Iterator<ArrayList<String>> prevExercisesIterator = null;
        ArrayList<String> prevExercise = null;

        if(hasPreviousExercises) {
            prevExercises = sessionController.returnPreviousExerciseData();
            prevExercisesIterator = prevExercises.iterator();
        }


        while (exercisesListIterator.hasNext()) {
            exercise = exercisesListIterator.next();

            if(hasPreviousExercises) {
                prevExercise = prevExercisesIterator.next();
            }

            if (exercise.get(0).equals("Strength")) {

                String previous = null;
                if (hasPreviousExercises) {
                    previous = prevExercise.get(1) + "x" + prevExercise.get(2);
                }

                addStrengthExerciseRow(exercise.get(1), exercise.get(3), previous);
            }
            else {

                String previous = null;
                if (hasPreviousExercises) {
                    previous = prevExercise.get(1);
                }

                addDurationExerciseRow(exercise.get(1), previous);
            }
        }
    }


    private void addStrengthExerciseRow(String name, String series, String previous) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View strengthExerciseRow = inflater.inflate(R.layout.strength_exercise_tracking_row, tableLayout, false);

        TextView exerciseName = strengthExerciseRow.findViewById(R.id.tv_name);
        exerciseName.setText(name);

        TextView exerciseSeries = strengthExerciseRow.findViewById(R.id.tv_series);
        exerciseSeries.setText(series);

        TextView exercisePrevious = strengthExerciseRow.findViewById(R.id.tv_previous);
        if (previous != null) {
            exercisePrevious.setText(previous);
        }

        strengthExerciseRow.setTag("strength");

        tableLayout.addView(strengthExerciseRow);
    }

    private void addDurationExerciseRow(String name, String previous) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View durationExerciseRow = inflater.inflate(R.layout.duration_exercise_tracking_row, tableLayout, false);

        TextView exerciseName = durationExerciseRow.findViewById(R.id.tv_name);
        exerciseName.setText(name);

        TextView exercisePrevious = durationExerciseRow.findViewById(R.id.tv_duration);
        if (previous != null) {
            exercisePrevious.setText(previous);
        }

        durationExerciseRow.setTag("duration");

        tableLayout.addView(durationExerciseRow);
    }

    private int saveStrengthExercise(View strengthExerciseRow) {
        TextView etName = strengthExerciseRow.findViewById(R.id.et_name);
        TextView etSeries = strengthExerciseRow.findViewById(R.id.et_series);
        EditText etReps = strengthExerciseRow.findViewById(R.id.et_reps);
        EditText etWeight = strengthExerciseRow.findViewById(R.id.et_weight);

        String name = etName.getText().toString().trim();
        String seriesStr = etSeries.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (repsStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            int series = Integer.parseInt(seriesStr);
            int reps = Integer.parseInt(repsStr);
            float weight = Float.parseFloat(weightStr);
            sessionController.addStrengthExerciseToNewSession(this, name, series, reps, weight);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 0;
    }

    private int saveDurationExercise(View durationExerciseRow) {
        EditText etName = durationExerciseRow.findViewById(R.id.et_name);
        EditText etDuration = durationExerciseRow.findViewById(R.id.et_duration);

        String name = etName.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();

        if (name.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            int duration = Integer.parseInt(durationStr);
            sessionController.addTimedExerciseToNewSession(this, name, duration);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 0;
    }

    private void saveSession() {

        sessionController.createNewSession(this, getIntent().getLongExtra("routine_id", -1));

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            View child = tableLayout.getChildAt(i);
            if ("strength".equals(child.getTag())) {
                if(saveStrengthExercise(child) == -1) {
                    return;
                }
            } else if ("duration".equals(child.getTag())) {
                if (saveDurationExercise(child) == -1) {
                    return;
                }
            } else {
                return;
            }
        }

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            // Show an AlertDialog with the info message
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage("Esta pantalla muestra los ejercicios de la sesión seleccionada. Puedes ingresar " +
                            "los valores de las repeticiones y peso para los ejercicios de fuerza, o la duración para los ejercicios de tiempo.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}