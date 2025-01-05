package com.example.opengym;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.View.PrincipalActivity;
import com.example.opengym.View.SignUpActivity;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDAO userDAO = new UserDAO(this);

        Intent intent;
        String logInName = userDAO.readLastLogin();

        if (logInName != null) {
            intent = new Intent(this, PrincipalActivity.class);
            intent.putExtra("userName", logInName);

        }
        else {
            intent = new Intent(this, SignUpActivity.class);
        }

        userDAO.closeConnection();
        startActivity(intent);
        finish();
    }
}
