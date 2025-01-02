package com.example.opengym.View;  // Asegúrate de usar el paquete correcto

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

            String username = enterUsr.getText().toString();
            String password = enterPass.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            } else {

                SignUpController signUpController = new SignUpController();
                signUpController.signUp(username, password, SignUpActivity.this);
                Toast.makeText(SignUpActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}