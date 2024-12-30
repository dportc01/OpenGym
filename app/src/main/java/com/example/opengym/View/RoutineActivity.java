package com.example.opengym.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.opengym.R;

import java.util.ArrayList;
import java.util.List;

public class RoutineActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private List<String> rutinaNames = new ArrayList<>(); // Lista para almacenar los nombres de las rutinas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_selector);

        tableLayout = findViewById(R.id.tableLayout);
        Button addRowButton = findViewById(R.id.add_row_button);

        // Establecer el OnClickListener usando la sintaxis lambda
        addRowButton.setOnClickListener(v -> showNameInputDialog());
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
                        Toast.makeText(RoutineActivity.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        addNewRoutine(routineName);  // Añadir la rutina con el nombre personalizado
                        rutinaNames.add(routineName); // Agregar el nombre de la rutina a la lista
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void addNewRoutine(String routineName) {
        // Crear una nueva fila
        TableRow newRow = new TableRow(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        newRow.setLayoutParams(params);

        // Crear un nuevo botón para la rutina con el nombre proporcionado
        Button newRoutineButton = new Button(this);
        newRoutineButton.setText(routineName);  // Usar el nombre personalizado
        newRoutineButton.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1
        ));
        newRoutineButton.setPadding(16, 16, 16, 16);
        newRoutineButton.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        newRoutineButton.setTextColor(ContextCompat.getColor(this, R.color.white));

        // Agregar funcionalidad al botón
        newRoutineButton.setOnClickListener(v ->
                Toast.makeText(RoutineActivity.this,
                        "Rutina seleccionada: " + ((Button) v).getText(),
                        Toast.LENGTH_SHORT).show()
        );

        // Añadir el botón a la fila
        newRow.addView(newRoutineButton);

        // Añadir la fila al TableLayout
        tableLayout.addView(newRow);
    }

    // Método para obtener la lista de rutinas
    public List<String> getRutinas() {
        return rutinaNames;  // Devuelve la lista con los nombres de las rutinas
    }
}
