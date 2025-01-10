package com.example.opengym.View;  // Asegúrate de usar el paquete correcto

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.Controller.SignUpController;
import com.example.opengym.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText enterUsr, enterPass;
    private Button supabutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        enterUsr = findViewById(R.id.enter_usr);
        enterPass = findViewById(R.id.enter_pass);
        supabutton = findViewById(R.id.supabutton);

        supabutton.setOnClickListener(v -> {

            SignUpController signUpController = new SignUpController();

            String username = enterUsr.getText().toString();
            String password = enterPass.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {

                Toast.makeText(SignUpActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent;

                switch (signUpController.checkUser(this ,username, password)) {
                    case -1:
                        signUpController.signUp(username, password, SignUpActivity.this);
                        Toast.makeText(SignUpActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        intent = new Intent(SignUpActivity.this, PrincipalActivity.class);
                        intent.putExtra("userName", username);
                        startActivity(intent);
                        finish();

                        break;

                    case 0:
                        Toast.makeText(SignUpActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        intent = new Intent(SignUpActivity.this, PrincipalActivity.class);
                        intent.putExtra("userName", username);
                        startActivity(intent);
                        finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            // Show an AlertDialog with the info message
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage("Esta es la pantalla de registro. Aquí puedes crear una cuenta nueva para acceder a la aplicación o acceder a una cuenta ya creada previamente.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}