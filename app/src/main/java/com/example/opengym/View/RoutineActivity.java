package com.example.opengym.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import com.example.opengym.Controller.RoutineController;
import com.example.opengym.R;

public class RoutineActivity extends AppCompatActivity {

    private RoutineController controller;
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

        controller = new RoutineController(getIntent().getParcelableExtra("routine"));

        loadSessions();
    }

    private void loadSessions() {

        controller.loadSessions(this);

        int i = 0;
        String sessionName = controller.getSessionName(i);
        String sessionRest = controller.getSessionRest(i);

        while (sessionName != null & sessionRest != null) {
            addSessionTable(sessionName, sessionRest);
            i++;
            sessionName = controller.getSessionName(i);
            sessionRest = controller.getSessionName(i);
        }
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
                // Add the session to the Routine session list
                if (controller.addSession(this, sessionName, restDuration) == -1) {
                    Toast.makeText(this, "Error al insertar, porfavor compruebe que el nombre no se repita", Toast.LENGTH_SHORT).show();
                    return;
                }
                addSessionTable(sessionName, restDuration);
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
        tableLayout.addView(sessionDetailsRow);

        // Add the session table row below the session details row
        LayoutInflater inflater = LayoutInflater.from(this);
        View sessionTableRow = inflater.inflate(R.layout.session_table, null);
        TableRow tableRow = new TableRow(this);
        tableRow.addView(sessionTableRow);
        tableLayout.addView(tableRow);

        // Add the "Añadir Ejercicio" button row below the session table row
        addAddExerciseRow(sessionDetailsRow);  // Pass the session row to add exercises below it
    }


    private void addAddExerciseRow(TableRow sessionDetailsRow) {
        // Inflate the "Añadir Ejercicio" row layout with buttons
        LayoutInflater inflater = LayoutInflater.from(this);
        View addExerciseRow = inflater.inflate(R.layout.add_exercise, null);

        // Create a TableRow to hold the "Añadir Ejercicio" buttons
        TableRow tableRow = new TableRow(this);
        tableRow.addView(addExerciseRow);

        // Add the exercise row below the session row
        int index = tableLayout.indexOfChild(sessionDetailsRow) + 2;  // Add 2 to place it after both the session and session table rows
        tableLayout.addView(tableRow, index);

        // Set button listeners for adding strength or duration exercises
        Button btnAddStrength = addExerciseRow.findViewById(R.id.btn_add_strength);
        Button btnAddDuration = addExerciseRow.findViewById(R.id.btn_add_duration);

        btnAddStrength.setOnClickListener(v -> addStrengthExerciseRow(addExerciseRow, sessionDetailsRow));
        btnAddDuration.setOnClickListener(v -> addDurationExerciseRow(addExerciseRow, sessionDetailsRow));
    }

    private void addStrengthExerciseRow(View addExerciseRow, TableRow sessionDetailsRow) {
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

        // Add the "Añadir Ejercicio" buttons row again below the newly added exercise
        addAddExerciseRow(sessionDetailsRow);
    }

    private void addDurationExerciseRow(View addExerciseRow, TableRow sessionDetailsRow) {
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

        // Add the "Añadir Ejercicio" buttons row again below the newly added exercise
        addAddExerciseRow(sessionDetailsRow);
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

    private void closeRoutine(){
        Intent intent = new Intent(RoutineActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    private void removeSessionPopUp(){
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Escribe el nombre de la sesion");

        AlertDialog.Builder removePopUp = new AlertDialog.Builder(this);
        removePopUp.setTitle("¿Que sesion quieres borrar?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String routineName = nameInput.getText().toString().trim();
                    if (routineName.isEmpty()) {
                        Toast.makeText(this, "Introduce el nombre de una sesion", Toast.LENGTH_SHORT).show();
                    } else {
                        if (controller.removeRoutineSession(routineName, this) == -1) { // Añadir la rutina a la base de datos
                            Toast.makeText(this, "No existe la sesion que quieres borrar", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(this, "Sesion eliminada", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_routine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.close_routine:
                closeRoutine();
                return true;
            case R.id.remove_routine:
                removeSessionPopUp();
                return true;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
