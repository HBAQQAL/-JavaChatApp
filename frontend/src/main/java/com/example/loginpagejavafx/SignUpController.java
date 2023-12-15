package com.example.loginpagejavafx;


import com.example.loginpagejavafx.utils.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Created By: Naman Agarwal
 * User ID: naman2807
 * Package Name: sample
 * Project Name: JavaFXLoginSignUp
 * Date: 09-02-2021
 */

public class SignUpController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField userName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private PasswordField password;

    public User addUser() {
        String fName = firstName.getText();
        String lName = lastName.getText();
        String user = userName.getText();
        String phone = phoneNumber.getText();
        String pass = password.getText();
        return new User(fName, lName, phone, user, pass);
    }

    public void signup(){
        System.out.println("the action is working");
    }

    @FXML
    public void switchToLogin() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
