package com.example.opengym.View;  // Asegúrate de usar el paquete correcto

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.Controller.LogInController;
import com.example.opengym.R;

public class LogInActivity extends AppCompatActivity {

    // Declaración de las variables para los campos de texto y el botón
    private EditText enterUsr, enterPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Establecer la vista

        // Inicialización de los campos EditText y el botón
        enterUsr = findViewById(R.id.enter_usr);
        enterPass = findViewById(R.id.enter_pass);
        Button supabutton = findViewById(R.id.supabutton);

        // Configuración del botón para manejar el clic
        supabutton.setOnClickListener(v -> {
            // Recopilación de los valores ingresados por el usuario
            String username = enterUsr.getText().toString();
            String password = enterPass.getText().toString();

            // Verificar si ambos campos tienen contenido
            if (username.isEmpty() || password.isEmpty()) {
                // Mostrar un mensaje si algún campo está vacío
                Toast.makeText(LogInActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Aquí se pasa la información al controlador
                Intent intent = new Intent(LogInActivity.this, LogInController.class);
                intent.putExtra("username", username);  // Pasar el nombre de usuario
                intent.putExtra("password", password);  // Pasar la contraseña
                startActivity(intent);  // Iniciar la nueva actividad
                finish();  // Finalizar la actividad actual para evitar el regreso a esta
            }
        });
    }
}