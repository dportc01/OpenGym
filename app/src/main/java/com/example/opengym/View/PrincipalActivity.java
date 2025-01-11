package com.example.opengym.View;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.R;
import com.example.opengym.Controller.PrincipalController;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private PrincipalController principalController;
    private TableLayout tableLayout;
    private ActivityResultLauncher<Intent> importRoutineLauncher;

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
        addRowButton.setOnClickListener(v -> showNameInputDialog(null));

        importRoutineLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            String filePath = uri.getPath();
                            Log.d("File path", filePath);
                            String routineData = principalController.importUserRoutine(uri, this);
                            if (routineData.equals("Error")) {
                                Toast.makeText(this, "Error al importar la rutina", Toast.LENGTH_SHORT).show();
                            } else if (routineData.equals("Existe")) {
                                Toast.makeText(this, "Ya existe esta rutina", Toast.LENGTH_SHORT).show();
                            } else {
                                String[] routineInfo = routineData.split(",");
                                addNewRoutine(routineInfo[0], routineInfo[1]);
                                Toast.makeText(this, "Rutina importada", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
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
                .setPositiveButton("Sí", (dialog, which) -> removeRoutine(cardView))
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

        ImageView routineDownload = cardView.findViewById(R.id.export_button);
        routineDownload.setOnClickListener(v -> {
            exportRoutine(routineName);
            new AlertDialog.Builder(this)
                    .setTitle("Exportación")
                    .setMessage("La rutina se ha exportado correctamente en el directorio del descargas del dispositivo")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        cardView.setOnClickListener(v -> {
            Toast.makeText(this, "Rutina seleccionada: " + routineName, Toast.LENGTH_SHORT).show();
            onRoutineSelection(principalController.getRoutineId(routineName)); // Navigate to the selected routine
        });

        tableLayout.addView(cardView);
    }

    private void updateRoutine(String routineName, String routineDescription, View edit) {

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
        LinearLayout routineLayout = new LinearLayout(this);
        routineLayout.setOrientation(LinearLayout.VERTICAL);
        routineLayout.setPadding(50,40,50,10);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Escribe el nombre de la rutina");

        final EditText descriptionInput = new EditText(this);
        descriptionInput.setHint("Escribe una descripcion para la rutina");
        descriptionInput.setLines(3);

        routineLayout.addView(nameInput);
        routineLayout.addView(descriptionInput);
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
                        if (edit == null) {
                            if (principalController.addUserRoutine(this, routineName, routineDescription) == -1) {
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
                    principalController.logOut(this);
                    Intent intent = new Intent(PrincipalActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void exportRoutine(String routineName){
        principalController.exportUserRoutine(this, routineName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sign_out) {
            logOut();
            return true;
        } else if (id == R.id.github_link) {
            openGitHub();
            return true;
        } else if (id == R.id.import_routine) {
            importRoutine();
            return true;
        } else if (id == R.id.action_info) {
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage("Esta es la pantalla principal. Aquí puedes ver tus rutinas y seleccionar una para modificarla," +
                            " eliminarla o realizar el seguimiento de una de sus sesiones. También puedes crear una nueva rutina," +
                            " importarla o exportar una ya creada a un archivo.\n El límite de rutinas es de 3 si eres un usuario base" +
                            " y de 10 si eres premium.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
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

    private void importRoutine() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        importRoutineLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String filePath = uri.getPath();
            Log.d("File path", filePath);
            String routineData = principalController.importUserRoutine(uri, this);
            if (routineData.equals("Error")) {
                Toast.makeText(this, "Error al importar la rutina", Toast.LENGTH_SHORT).show();
            } else if(routineData.equals("Existe")){
                Toast.makeText(this, "Ya existe esta rutina", Toast.LENGTH_SHORT).show();
            } else{
                String[] routineInfo = routineData.split(",");
                addNewRoutine(routineInfo[0], routineInfo[1]);
            }
        }
    }


}
