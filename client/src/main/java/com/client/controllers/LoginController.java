package com.client.controllers;

import java.io.IOException;
import java.util.Map;

import com.client.utils.ApiClient;
import com.client.utils.FXRouter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signinButton;
    @FXML
    private Text alertMessage;

    @FXML
    private void login(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        String jsonInputString = buildJsonInputString();
        Map<String, Object> response = ApiClient.makeHttpRequest(getApiUrl(), jsonInputString, "POST");

        handleApiResponse(response);
    }

    private boolean validateInput() {
        if (fieldsAreEmpty()) {
            alertMessage.setText("Please fill in all the fields");
            return false;
        }
        return true;
    }

    private boolean fieldsAreEmpty() {
        return usernameField.getText().isEmpty() || passwordField.getText().isEmpty();
    }

    private String buildJsonInputString() {
        return String.format("{\"username\":\"%s\",\"password\":\"%s\"}", usernameField.getText(),
                passwordField.getText());
    }

    private String getApiUrl() {
        return "http://localhost:7000/api/login";
    }

    private void handleApiResponse(Map<String, Object> response) {
        if (response.get("status").equals("error")) {
            alertMessage.setText(response.get("message").toString());
        } else {
            alertMessage.setText("Login successful");

            try {
                FXRouter.goTo("chat");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void switchToSignUpView() {
        try {
            FXRouter.goTo("signup");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
