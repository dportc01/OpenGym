package com.example.opengym.Controller;

import com.example.opengym.Model.DAO.UserDAO;
import com.example.opengym.Model.Entities.User;

import android.content.Context;

public class SignUpController {

    public SignUpController() {
    }

    /**
     * Check for a user with the specified name and password.
     * <p>
     * @return <p><code>-1</code> if the user doesn't exist<br />
     * <code>0</code> if the user exists but password isn't right<br />
     * <code>1</code> if the user exists and the password is right<p />
     */
    public int checkUser(Context context, String name, String password) {

        UserDAO userDAO = new UserDAO(context);
        User readUser = userDAO.read(name);
        if (readUser == null) {
            return -1;
        }
        else {
            if (readUser.getPassword().equals(password)) {
                return 1;
            }
            return 0;
        }
    }

    public void signUp(String name, String password, Context context) {
        UserDAO userDao = new UserDAO(context);
        long check = userDao.create(new User(name, password), -1);
        if (check == -1) {
            throw new IllegalArgumentException("Error creating user");
        }
    }
}
