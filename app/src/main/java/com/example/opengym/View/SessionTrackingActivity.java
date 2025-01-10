package com.example.opengym.View;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    }

    private void loadExercises() {
        sessionController.loadExercises(this);

        int i = 0;
        String exerciseName = sessionController.getExerciseName(i);
        String exerciseType = sessionController.getExerciseType(i);

        while (exerciseName != null && exerciseType != null) {
            if (exerciseType.equals("Strength")) {
                addStrengthExerciseRow(exerciseName, sessionController.getPreviousStrengthValues(i));
            } else if (exerciseType.equals("Timed")) {
                addDurationExerciseRow(exerciseName, sessionController.getPreviousDurationValues(i));
            }
            i++;
            exerciseName = sessionController.getExerciseName(i);
            exerciseType = sessionController.getExerciseType(i);
        }
    }

    private void addStrengthExerciseRow(String exerciseName, ArrayList<String> previousValues) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View strengthExerciseRow = inflater.inflate(R.layout.strength_exercise_tracking_row, tableLayout, false);

        TextView name = strengthExerciseRow.findViewById(R.id.tv_name);
        name.setText(exerciseName);

        TextView series = strengthExerciseRow.findViewById(R.id.tv_series);
        TextView previousInfo = strengthExerciseRow.findViewById(R.id.tv_previous);

        tableLayout.addView(strengthExerciseRow);
    }

    private void addDurationExerciseRow(String exerciseName, String previousValue) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View durationExerciseRow = inflater.inflate(R.layout.duration_exercise_tracking_row, tableLayout, false);

        tableLayout.addView(durationExerciseRow);
    }

    private void saveSession() {
        /*
        int childCount = tableLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View row = tableLayout.getChildAt(i);
            if (row instanceof TableRow) {
                EditText etSeries = row.findViewById(R.id.et_series);
                EditText etReps = row.findViewById(R.id.et_reps);
                EditText etWeight = row.findViewById(R.id.et_weight);
                EditText etDuration = row.findViewById(R.id.et_duration);

                String name = ((TextView) row.findViewById(R.id.tv_exercise_name)).getText().toString().trim();
                if (etSeries != null && etReps != null && etWeight != null) {
                    String seriesStr = etSeries.getText().toString().trim();
                    String repsStr = etReps.getText().toString().trim();
                    String weightStr = etWeight.getText().toString().trim();

                    if (seriesStr.isEmpty() || repsStr.isEmpty() || weightStr.isEmpty()) {
                        Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int series = Integer.parseInt(seriesStr);
                        int reps = Integer.parseInt(repsStr);
                        int weight = Integer.parseInt(weightStr);
                        // sessionController.updateStrengthExercise(this, name, series, reps, weight);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
                    }
                } else if (etDuration != null) {
                    String durationStr = etDuration.getText().toString().trim();

                    if (durationStr.isEmpty()) {
                        Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int duration = Integer.parseInt(durationStr);
                        // sessionController.updateDurationExercise(this, name, duration);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        Toast.makeText(this, "Sesión guardada", Toast.LENGTH_SHORT).show();

         */
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