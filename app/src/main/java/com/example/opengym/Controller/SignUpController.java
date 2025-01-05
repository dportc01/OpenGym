package com.example.opengym.Controller;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.User;

import android.content.Context;

public class SignUpController {

    public SignUpController() {
    }

    public void signUp(String name, String password, Context context) {
        UserDAO userDao = new UserDAO(context);
        long check = userDao.create(new User(name, password), -1);
        if (check == -1) {
            throw new IllegalArgumentException("Error creating user");
        }
    }
}
