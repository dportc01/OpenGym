package com.example.opengym.View;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.R; // TODO Cambiar
import com.example.opengym.Controller.PrincipalController;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private PrincipalController principalController;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_selector);

        tableLayout = findViewById(R.id.tableLayout);
        Button addRowButton = findViewById(R.id.add_row_button);

        principalController = new PrincipalController(this, getIntent().getStringExtra("userName"));

        loadExistingRoutines();

        addRowButton.setOnClickListener(v -> showNameInputDialog());
    }

    public void onRoutineSelection(String routineName) {
        Intent intent = new Intent(PrincipalActivity.this, RoutineActivity.class);
        intent.putExtra("routineName", routineName);
        startActivity(intent);
    }

    private void loadExistingRoutines() {
        List<String> existingRoutines = principalController.getUserRoutines(this);
        for (String routine : existingRoutines) {
            addNewRoutine(routine);
        }
    }

    private void addNewRoutine(String routineName) {
        // Inflate the card from the XML template
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.routine_card, tableLayout, false);
        TextView routineTextView = cardView.findViewById(R.id.routine_name);
        routineTextView.setText(routineName);

        // Set click listener on the card
        cardView.setOnClickListener(v -> {
            Toast.makeText(this, "Rutina seleccionada: " + routineName, Toast.LENGTH_SHORT).show();
            onRoutineSelection(routineName); // Navigate to the selected routine
        });

        // Add the card to the container
        tableLayout.addView(cardView);
    }

    private void showNameInputDialog() {
        // Crear un EditText para que el usuario introduzca el nombre de la rutina
        final EditText input = new EditText(this);
        input.setHint("Escribe el nombre de la rutina");

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo nombre de rutina")
                .setView(input)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String routineName = input.getText().toString().trim();
                    if (routineName.isEmpty()) {
                        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        if (principalController.addUserRoutine(this, routineName, null) == -1) { // Añadir la rutina a la base de datos
                            Toast.makeText(this, "Ya existe una rutina con ese nombre", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            addNewRoutine(routineName); // Añadir la rutina con el nombre personalizado
                        }
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
