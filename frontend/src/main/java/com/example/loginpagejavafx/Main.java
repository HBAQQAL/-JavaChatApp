package com.example.loginpagejavafx;

//import com.example.loginpagejavafx.database.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.example.loginpagejavafx.utils.FXRouter;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXRouter.bind(this, primaryStage, "Chat app", 800, 600);

        FXRouter.when("login", "loginView/login.fxml","loginView/login-styles.css");
        FXRouter.when("signup", "signUpView/signup.fxml","signUpView/signup-styles.css");
        try{
            FXRouter.goTo("login");
        }catch (Exception e) {
            System.out.println(e);
        }


    }

    @Override
    public void init() throws Exception {
        super.init();
        // if(!DataSource.getInstance().open()){
        // System.out.println("Something Went wrong.");
        // }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // DataSource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}