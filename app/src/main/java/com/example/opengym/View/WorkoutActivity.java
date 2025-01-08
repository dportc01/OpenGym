package com.example.opengym.View;

import android.os.Bundle;
import android.view.LayoutInflater;
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

public class WorkoutActivity extends AppCompatActivity {

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
        View strengthExerciseRow = inflater.inflate(R.layout.strength_exercise_row, tableLayout, false);

        TextView tvExerciseName = strengthExerciseRow.findViewById(R.id.tv_exercise_name);
        tvExerciseName.setText(exerciseName);

        EditText etSeries = strengthExerciseRow.findViewById(R.id.et_series);
        EditText etReps = strengthExerciseRow.findViewById(R.id.et_reps);
        EditText etWeight = strengthExerciseRow.findViewById(R.id.et_weight);

        TextView tvPreviousSeries = strengthExerciseRow.findViewById(R.id.tv_previous_series);
        TextView tvPreviousReps = strengthExerciseRow.findViewById(R.id.tv_previous_reps);
        TextView tvPreviousWeight = strengthExerciseRow.findViewById(R.id.tv_previous_weight);

        tvPreviousSeries.setText(previousValues.get(0));
        tvPreviousReps.setText(previousValues.get(1));
        tvPreviousWeight.setText(previousValues.get(2));

        tableLayout.addView(strengthExerciseRow);
    }

    private void addDurationExerciseRow(String exerciseName, String previousValue) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View durationExerciseRow = inflater.inflate(R.layout.duration_exercise_row, tableLayout, false);

        TextView tvExerciseName = durationExerciseRow.findViewById(R.id.tv_exercise_name);
        tvExerciseName.setText(exerciseName);

        EditText etDuration = durationExerciseRow.findViewById(R.id.et_duration);
        TextView tvPreviousDuration = durationExerciseRow.findViewById(R.id.tv_previous_duration);

        tvPreviousDuration.setText(previousValue);

        tableLayout.addView(durationExerciseRow);
    }

    private void saveSession() {
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
    }
}