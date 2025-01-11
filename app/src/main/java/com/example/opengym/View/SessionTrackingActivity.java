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
        LayoutInflater inflater = LayoutInflater.from(this);
        View sessionTable = inflater.inflate(R.layout.tracking_table, tableLayout, false);
        tableLayout.addView(sessionTable);
    }

    private void loadExercises() {

        ArrayList<ArrayList<String>> exercisesList = sessionController.returnExercises();

        boolean hasPreviousExercises = sessionController.loadNewestSession(this);
        ArrayList<ArrayList<String>> prevExercisesList = sessionController.returnPreviousExerciseData();

        boolean added = false;

        for (ArrayList<String> exercise : exercisesList) {
            if (hasPreviousExercises) {
                for (ArrayList<String> prevExercise : prevExercisesList) {
                    if (exercise.get(1).equals(prevExercise.get(1)) && exercise.get(0).equals(prevExercise.get(0))) {

                        added = true;

                        if (exercise.get(0).equals("Strength")) {
                            addStrengthExerciseRow(exercise.get(1), exercise.get(3), prevExercise.get(2) + "x" + prevExercise.get(3));
                        }
                        else {
                            addDurationExerciseRow(exercise.get(1), prevExercise.get(2));
                        }
                    }
                }

                if (!added) {
                    if (exercise.get(0).equals("Strength")) {
                        addStrengthExerciseRow(exercise.get(1), exercise.get(3), null);
                    }
                    else {
                        addDurationExerciseRow(exercise.get(1), null);
                    }
                }
            }
            else {
                if (exercise.get(0).equals("Strength")) {
                    addStrengthExerciseRow(exercise.get(1), exercise.get(3), null);
                }
                else {
                    addDurationExerciseRow(exercise.get(1), null);
                }
            }

            added = false;
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
        TextView tvName = strengthExerciseRow.findViewById(R.id.tv_name);
        TextView tvSeries = strengthExerciseRow.findViewById(R.id.tv_series);
        EditText etReps = strengthExerciseRow.findViewById(R.id.et_reps);
        EditText etWeight = strengthExerciseRow.findViewById(R.id.et_weight);

        String name = tvName.getText().toString().trim();
        String seriesStr = tvSeries.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (repsStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            sessionController.createNewSession(this, getIntent().getLongExtra("routine_id", -1));
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
        TextView tvName = durationExerciseRow.findViewById(R.id.tv_name);
        EditText etDuration = durationExerciseRow.findViewById(R.id.et_duration);

        String name = tvName.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();

        if (name.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            sessionController.createNewSession(this, getIntent().getLongExtra("routine_id", -1));
            int duration = Integer.parseInt(durationStr);
            sessionController.addTimedExerciseToNewSession(this, name, duration);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 0;
    }

    private void saveSession() {

        for (int i = 2; i < tableLayout.getChildCount(); i++) {
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
                    .setMessage("Esta pantalla muestra los ejercicios de la sesión seleccionada y las marcas obtenidas la última vez que la sesión fue realizada (En el caso de los ejercicios de fuerza, las marcas vienen dadas por Repeticiones x Peso). Puedes ingresar " +
                            "los valores de las repeticiones y peso para los ejercicios de fuerza, o la duración para los ejercicios de tiempo.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}