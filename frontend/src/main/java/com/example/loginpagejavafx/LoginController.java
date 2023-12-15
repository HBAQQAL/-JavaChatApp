package com.example.loginpagejavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signinButton;

    private Button signInLink;
    @FXML
    private void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Add your login logic here (e.g., check credentials, open a new scene, etc.)
        System.out.println("Logging in with username: " + username + " and password: " + password);
    }

    public void switchToSignUpView() {
        try {
            File file = new File("src/main/resources/com/example/loginpagejavafx/signInView/signin.fxml");
            if(file.exists()){
                System.out.println("file exists ");
            }else{
                System.out.println("the file dosen't exist");
            }
            FXMLLoader loader = new FXMLLoader(file.toURL());
            Parent root = loader.load();


            Scene signUpScene = new Scene(root,500,600);
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Assuming usernameField is any control in your current scene
            stage.setScene(signUpScene);
            String cssFile = Main.class.getResource("signInView/signin-styles.css").toExternalForm();
            stage.getScene().getStylesheets().add(cssFile);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}