package com.example.opengym.View;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.R;
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
        // change the user to premium if the user has a premium account
        principalController.changePremium(this);

        loadExistingRoutines();

        addRowButton.setOnClickListener(v -> showNameInputDialog(null));
    }

    public void onRoutineSelection(long id) {
        Intent intent = new Intent(PrincipalActivity.this, RoutineActivity.class);
        intent.putExtra("routine", id);
        intent.putExtra("premium", principalController.isPremium());
        startActivity(intent);
    }

    private void loadExistingRoutines() {
        List<String> existingRoutines = principalController.getUserRoutines(this);
        for (String routine : existingRoutines) {
            String[] routineData = routine.split(",");
            addNewRoutine(routineData[0], routineData[1]);
        }
    }

    private void showRoutineDescription(String routineDescription){
        AlertDialog.Builder infoPopUp = new AlertDialog.Builder(this);
        infoPopUp.setTitle("Descripcion")
                .setMessage(routineDescription)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void askRemove(View cardView){
        AlertDialog.Builder removePopUp = new AlertDialog.Builder(this);
        removePopUp.setTitle("Eliminar rutina")
                .setMessage("¿Estás seguro de que quieres eliminar esta rutina?")
                .setPositiveButton("Sí", (dialog, which) -> { removeRoutine(cardView); })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void removeRoutine(View cardView){//TODO
        TextView routineNameTextView = cardView.findViewById(R.id.routine_name);
        if (principalController.removeUserRoutine(routineNameTextView.getText().toString(),this) == -1) {
            Toast.makeText(this, "No se ha podido eliminar la rutina", Toast.LENGTH_SHORT).show();
            
        } else {
            Toast.makeText(this, "Rutina eliminada", Toast.LENGTH_SHORT).show();
            tableLayout.removeView(cardView);
        }
    }

    private void addNewRoutine(String routineName, String routineDescription) {

        // Inflate the card from the XML template
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.routine_card, tableLayout, false);

        TextView routineTextView = cardView.findViewById(R.id.routine_name);
        routineTextView.setText(routineName);

        TextView routineInfo = cardView.findViewById(R.id.info_button);
        routineInfo.setOnClickListener(v -> showRoutineDescription(routineDescription));

        TextView routineRemove = cardView.findViewById(R.id.delete_button);
        routineRemove.setOnClickListener(v -> askRemove(cardView));

        ImageView routineEdit = cardView.findViewById(R.id.edit_button);
        routineEdit.setOnClickListener(v -> showNameInputDialog(cardView));

        // Set click listener on the card
        cardView.setOnClickListener(v -> {
            Toast.makeText(this, "Rutina seleccionada: " + routineName, Toast.LENGTH_SHORT).show();
            onRoutineSelection(principalController.getRoutineId(routineName)); // Navigate to the selected routine
        });

        // Add the card to the container
        tableLayout.addView(cardView);
    }

    private void updateRoutine(String routineName, String routineDescription, View edit) {

        //voler a poner los botones porque si no la view coge el antiguo routine na,e

        TextView routineText = edit.findViewById(R.id.routine_name);
        routineText.setText(routineName);

        TextView routineInfo = edit.findViewById(R.id.info_button);
        routineInfo.setOnClickListener(v -> showRoutineDescription(routineDescription));

        edit.setOnClickListener(v -> {
            Toast.makeText(this, "Rutina seleccionada: " + routineName, Toast.LENGTH_SHORT).show();
            onRoutineSelection(principalController.getRoutineId(routineName)); // Navigate to the selected routine
        });
    }

    /**
     * Creates a new view or modifies an already existing one if
     * provided with the parameter
     * @param edit view of the entry to be modified
     */
    private void showNameInputDialog(View edit) {
        if (!checkRoutineLimit() && edit == null) {
            return;
        }
        // Primero crear un LinearLayout que contenga los 2 campos de nombre y descripcion
        LinearLayout routineLayout = new LinearLayout(this);
        routineLayout.setOrientation(LinearLayout.VERTICAL);
        routineLayout.setPadding(50,40,50,10);

        // Crear un EditText para que el usuario introduzca el nombre de la rutina
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Escribe el nombre de la rutina");

        final EditText descriptionInput = new EditText(this);
        descriptionInput.setHint("Escribe una descripcion para la rutina");
        descriptionInput.setLines(3);

        routineLayout.addView(nameInput);
        routineLayout.addView(descriptionInput);
        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo nombre de rutina")
                .setView(routineLayout)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String routineName = nameInput.getText().toString().trim();
                    String routineDescription = descriptionInput.getText().toString().trim();
                    if(routineDescription.isEmpty()){
                        routineDescription = "No hay descripción";
                    }
                    if (routineName.isEmpty()) {
                        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        if (edit == null) { // Añadir la rutina a la base de datos
                            if (principalController.addUserRoutine(this, routineName, routineDescription) == -1) { // Añadir la rutina a la base de datos
                                Toast.makeText(this, "Ya existe una rutina con ese nombre", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            addNewRoutine(routineName, routineDescription);
                        }
                        else {

                            TextView routineText = edit.findViewById(R.id.routine_name);
                            String oldName = routineText.getText().toString();

                            if (principalController.updateRoutine(this, routineName, routineDescription, oldName) <= 0) {
                                Toast.makeText(this, "Error al insertar, compruebe el nombre", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            updateRoutine(routineName, routineDescription, edit);
                        }
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void logOut() {
        AlertDialog.Builder logOutPopUp = new AlertDialog.Builder(this);
        logOutPopUp.setTitle("¿Quieres cerrar tu sesión?")
                .setPositiveButton("Sí", (dialog, which) -> { 
                    Intent intent = new Intent(PrincipalActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            logOut();
            return true;
        } else if (item.getItemId() == R.id.github_link) {
            openGitHub();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGitHub(){
        String url = "https://github.com/dportc01/OpenGym";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private boolean checkRoutineLimit() {
        int routineNumber = principalController.getUserRoutines(this).size();
        if (routineNumber == 3 && !principalController.isPremium()) {
            Toast.makeText(this, "No puedes tener más de 3 rutinas sin ser premium", Toast.LENGTH_SHORT).show();
            return false;
        } else if(routineNumber == 10) {
            Toast.makeText(this, "No puedes tener más de 10 rutinas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
