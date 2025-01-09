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

public class SessionEditorActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private SessionController sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_editor);

        // Inicializar variables y vistas
        initViews();

        // Obtener los datos de la sesión
        String sessionName = getIntent().getStringExtra("session_name");
        String restDuration = getIntent().getStringExtra("rest_duration");
        long sessionId = getIntent().getLongExtra("session_id", -1);

        // Crear controlador para la sesión
        sessionController = new SessionController(this, sessionId);

        // Mostrar la tabla inicial con los detalles de la sesión
        addSessionTable(sessionName, restDuration);
    }

    // Inicialización de vistas y configuración de listeners
    private void initViews() {
        tableLayout = findViewById(R.id.table_layout_editor);

        Button btnAddStrength = findViewById(R.id.btn_add_strength);
        Button btnAddDuration = findViewById(R.id.btn_add_duration);
        Button btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> saveSession());
        btnAddStrength.setOnClickListener(v -> addStrengthExerciseRow());
        btnAddDuration.setOnClickListener(v -> addDurationExerciseRow());
    }

    // Agregar la fila inicial con los detalles de la sesión
    private void addSessionTable(String sessionName, String restDuration) {
        TableRow sessionRow = new TableRow(this);
        TextView sessionDetails = new TextView(this);
        sessionDetails.setText(String.format("Sesión: %s | Descanso: %s minutos", sessionName, restDuration));
        sessionDetails.setPadding(8, 8, 8, 8);
        sessionRow.addView(sessionDetails);

        tableLayout.addView(sessionRow);
    }

    // Métodos para agregar filas de ejercicios
    private void addStrengthExerciseRow() {
        if (!checkExerciseLimit()) {
            return;
        }
        // Inflar el diseño para una fila de ejercicio de fuerza
        LayoutInflater inflater = LayoutInflater.from(this);
        View strengthExerciseRow = inflater.inflate(R.layout.strength_exercise_row, tableLayout, false);
        strengthExerciseRow.setTag("strength");

        // Configurar el botón "X" para eliminar la fila
        Button btnRemove = strengthExerciseRow.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(v -> tableLayout.removeView(strengthExerciseRow));

        // Añadir la fila a la tabla
        tableLayout.addView(strengthExerciseRow);

        Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
    }

    private void addDurationExerciseRow() {
        if (!checkExerciseLimit()) {
            return;
        }
        // Inflar el diseño para una fila de ejercicio de duración
        LayoutInflater inflater = LayoutInflater.from(this);
        View durationExerciseRow = inflater.inflate(R.layout.duration_exercise_row, tableLayout, false);
        durationExerciseRow.setTag("duration");

        // Configurar el botón "X" para eliminar la fila
        Button btnRemove = durationExerciseRow.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(v -> tableLayout.removeView(durationExerciseRow));

        // Añadir la fila a la tabla
        tableLayout.addView(durationExerciseRow);

        Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
    }

    // Métodos auxiliares para guardar datos en la base de datos
    private int saveStrengthExercise(View strengthExerciseRow) {
        EditText etName = strengthExerciseRow.findViewById(R.id.et_name);
        EditText etSeries = strengthExerciseRow.findViewById(R.id.et_series);
        EditText etReps = strengthExerciseRow.findViewById(R.id.et_reps);
        EditText etWeight = strengthExerciseRow.findViewById(R.id.et_weight);

        String name = etName.getText().toString().trim();
        String seriesStr = etSeries.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (name.isEmpty() || seriesStr.isEmpty() || repsStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            int series = Integer.parseInt(seriesStr);
            int reps = Integer.parseInt(repsStr);
            int weight = Integer.parseInt(weightStr);
            if (sessionController.addStrenghExercise(this, name, series, reps, weight) == -1) {
                Toast.makeText(this, "Error al insertar compruebe el nombre", Toast.LENGTH_SHORT).show();
            }
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
            sessionController.addTimedExercise(this, name, duration);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Datos numéricos inválidos", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 0;
    }

    private void saveSession() {

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

    private boolean checkExerciseLimit() {
        int exerciseNumber = tableLayout.getChildCount() - 1;
        if (exerciseNumber == 7 && !getIntent().getBooleanExtra("premium", false)) {
            Toast.makeText(this, "No puedes tener más de 7 ejercicios sin ser premium", Toast.LENGTH_SHORT).show();
            return false;
        } else if(exerciseNumber == 15) {
            Toast.makeText(this, "No puedes tener más de 15 ejercicios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
