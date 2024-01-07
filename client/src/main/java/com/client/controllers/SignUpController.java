package com.client.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import com.client.utils.ApiClient;
import com.client.utils.FXRouter;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SignUpController {
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField dateofbirthField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmationPasswordField;
    @FXML
    private Text alertMessage;

    @FXML
    public void signup() {
        if (!validateInput()) {
            return;
        }

        String jsonInputString = buildJsonInputString();

        Map<String, Object> response = ApiClient.makeHttpRequest("http://localhost:7000/api/users",
                jsonInputString,
                "POST");

        handleApiResponse(response);
    }

    @FXML
    public void switchToLogin() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateInput() {
        if (fieldsAreEmpty()) {
            alertMessage.setText("Please fill in all the fields");
            return false;
        }

        if (!passwordsMatch()) {
            alertMessage.setText("Passwords don't match");
            return false;
        }

        return true;
    }

    private boolean passwordsMatch() {
        return passwordField.getText().equals(confirmationPasswordField.getText());
    }

    private boolean fieldsAreEmpty() {
        return Stream
                .of(firstnameField, lastnameField, usernameField, dateofbirthField, passwordField,
                        confirmationPasswordField)
                .anyMatch(field -> field.getText().isEmpty());
    }

    private String buildJsonInputString() {
        return String.format(
                "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"username\":\"%s\",\"dateOfBirth\":\"%s\",\"password\":\"%s\"}",
                firstnameField.getText(), lastnameField.getText(), usernameField.getText(), dateofbirthField.getText(),
                passwordField.getText());
    }

    private void handleApiResponse(Map<String, Object> response) {
        if (response.get("status").equals("success")) {
            try {
                alertMessage.setText(response.get("message").toString());
                FXRouter.goTo("login");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            alertMessage.setText(response.get("message").toString());
            alertMessage.setStyle("-fx-fill: red;");
        }
    }
}
