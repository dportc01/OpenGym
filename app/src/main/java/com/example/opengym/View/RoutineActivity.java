package com.example.opengym.View;

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

import android.view.LayoutInflater;
import android.view.View;

import com.example.opengym.Controller.RoutineController;
import com.example.opengym.R;

public class RoutineActivity extends AppCompatActivity {

    private RoutineController controller;
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

        controller = new RoutineController(this, getIntent().getLongExtra("routine", -1));

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
        View sessionTableRow = inflater.inflate(R.layout.session_table, tableLayout, false);
        TableRow tableRow = new TableRow(this);
        tableRow.addView(sessionTableRow);
        tableLayout.addView(tableRow);
    }
}
