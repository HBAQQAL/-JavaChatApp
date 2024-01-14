package com.client.controllers;

import java.io.IOException;
import java.util.Map;

import com.client.utils.ApiClient;
import com.client.utils.CoockieHandler;
import com.client.utils.FXRouter;

import Socket.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

        handleApiResponse(response, event);
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

        CoockieHandler.getInstance().setUserUsername(usernameField.getText());

        return String.format("{\"username\":\"%s\",\"password\":\"%s\"}", usernameField.getText(),
                passwordField.getText());
    }

    private String getApiUrl() {
        return "http://localhost:7000/api/login";
    }

    private void handleApiResponse(Map<String, Object> response, ActionEvent event) {
        if (response.get("status").equals("error")) {
            alertMessage.setText(response.get("message").toString());
        } else {
            alertMessage.setText("Login successful");

            try {

                FXRouter.goTo("chat");
                // // get the root from the event
                // Node node = (Node) event.getSource();
                // // get the stage from the root
                // Stage stage = (Stage) node.getScene().getWindow();
                // // create a new FXMLLoader
                // FXMLLoader loader = new
                // FXMLLoader(getClass().getResource("/chatView/chat.fxml"));
                // // load the FXML file
                // Parent root = loader.load();
                // // get the controller
                // ChatController controller = loader.getController();
                // // // use the controller...
                new Client("localhost", "7001", usernameField.getText(), FXRouter.getController("chat"));
                // // set the scene to the stage
                // stage.setScene(new Scene(root));
                // // show the stage
                // stage.show();

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
