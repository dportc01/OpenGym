package com.example.opengym.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import com.example.opengym.Controller.RoutineController;
import com.example.opengym.R;

import java.util.ArrayList;

public class RoutineActivity extends AppCompatActivity {

    private RoutineController routineController;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_viewer);

        // Initialize views
        tableLayout = findViewById(R.id.table_layout);
        Button btnAddSession = findViewById(R.id.btn_add_session);

        // Set listener for "Añadir Sesión" button
        btnAddSession.setOnClickListener(v -> promptForSessionDetails());

        routineController = new RoutineController(this, getIntent().getLongExtra("routine", -1));

        loadSessions();
    }

    private void loadSessions() {

        routineController.loadSessions(this);

        int i = 0;
        String sessionName = routineController.getSessionName(i);
        String sessionRest = routineController.getSessionRest(i);

        while (sessionName != null & sessionRest != null) {
            addSessionTable(sessionName, sessionRest);
            i++;
            sessionName = routineController.getSessionName(i);
            sessionRest = routineController.getSessionRest(i);
        }
    }

    // Prompt the user to enter session name and rest duration
    private void promptForSessionDetails() {
        if (!checkSessionLimit()) {
            return;
        }
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
                long sessionId = routineController.addSession(this, sessionName, restDuration);
                if (sessionId == -1) {
                    Toast.makeText(this, "Error al insertar, por favor compruebe que el nombre no se repita", Toast.LENGTH_SHORT).show();
                    return;
                }

                addSessionTable(sessionName, restDuration);

                // Inicia SessionEditorActivity con los detalles de la sesión
                Intent intent = new Intent(this, SessionEditorActivity.class);
                intent.putExtra("session_name", sessionName);
                intent.putExtra("rest_duration", restDuration);
                intent.putExtra("session_id", sessionId);
                intent.putExtra("premium", getIntent().getBooleanExtra("premium", false));
                startActivity(intent);
            }
        });


        builder.setNegativeButton("Cancelar", null);

        // Show the dialog
        builder.show();
    }


    private void addSessionTable(String sessionName, String restDuration) {
        // Add the session name and rest duration row
        TableRow sessionDetailsRow = new TableRow(this);
        TextView sessionDetailsText = new TextView(this);
        sessionDetailsText.setText(String.format("Sesión: %s | Descanso: %s minutos", sessionName, restDuration));
        sessionDetailsText.setTextSize(16);
        sessionDetailsText.setPadding(8, 8, 8, 8);
        sessionDetailsRow.addView(sessionDetailsText);
    
        /// Add the options button
        ImageButton optionsButton = new ImageButton(this);
        optionsButton.setImageResource(R.drawable.ic_more_vert); // Asegúrate de tener un ícono de tres puntos verticales en drawable
        optionsButton.setBackgroundColor(Color.TRANSPARENT); // Hacer el fondo transparente
        optionsButton.setOnClickListener(v -> sessionOptionMenu(v, sessionName, restDuration, sessionDetailsRow));
        optionsButton.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT));
        optionsButton.setPadding(8, 8, 8, 8);
        sessionDetailsRow.addView(optionsButton);
    
        tableLayout.addView(sessionDetailsRow);

        loadSessionExercises();
    }

    private void loadSessionExercises() {

        TableRow strExerciseRow = new TableRow(this);

        TextView exerciseName = new TextView(this);
        exerciseName.setText("Nombre: ");
        exerciseName.setPadding(8, 0, 0, 0);

        TextView exerciseSets = new TextView(this);
        exerciseName.setText("sets: ");

        TextView exerciseReps = new TextView(this);
        exerciseName.setText("repeticiones: ");

        TextView exerciseWeight = new TextView(this);
        exerciseName.setText("peso: ");

        strExerciseRow.addView(exerciseName);

        tableLayout.addView(strExerciseRow);
    }
    
    private void sessionOptionMenu(View view, String sessionName, String restDuration, TableRow sessionRow) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.session_options_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.start_session) {
                startSession(sessionName, restDuration);
                return true;
            } else if (item.getItemId() == R.id.edit_session) {
                editSession(sessionName, restDuration);
                return true;
            } else if (item.getItemId() == R.id.delete_session) {
                deleteSession(sessionRow, sessionName);
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
    
    private void startSession(String sessionName, String restDuration) {
        Intent intent = new Intent(RoutineActivity.this, WorkoutActivity.class);
        intent.putExtra("session_name", sessionName);
        intent.putExtra("rest_duration", restDuration);
        intent.putExtra("session_id", routineController.getSessionId(sessionName));
        startActivity(intent);
    }
    
    private void editSession(String sessionName, String restDuration) {
        Intent intent = new Intent(RoutineActivity.this, SessionEditorActivity.class);
        intent.putExtra("session_name", sessionName);
        intent.putExtra("rest_duration", restDuration);
        intent.putExtra("session_id", routineController.getSessionId(sessionName));
        intent.putExtra("premium", getIntent().getBooleanExtra("premium", false));
        startActivity(intent);
    }
    
    private void deleteSession(TableRow sessionRow, String sessionName) {
        tableLayout.removeView(sessionRow);
        routineController.removeRoutineSession(sessionName, this);
    }

    private boolean checkSessionLimit() {
        int sessionNumber = routineController.getSessionsList().size();
        if (sessionNumber == 4 && !getIntent().getBooleanExtra("premium", false)) {
            Toast.makeText(this, "No puedes tener más de 4 sesiones sin ser premium", Toast.LENGTH_SHORT).show();
            return false;
        } else if(sessionNumber == 10) {
            Toast.makeText(this, "No puedes tener más de 10 sesiones", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
