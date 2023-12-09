package com.example.loginpagejavafx;

//import com.example.loginpagejavafx.database.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.chrono.MinguoChronology;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginView/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),500,600);


        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        String cssFile = Main.class.getResource("loginView/login-styles.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(cssFile);
        primaryStage.show();
    }


    @Override
    public void init() throws Exception {
        super.init();
//        if(!DataSource.getInstance().open()){
//            System.out.println("Something Went wrong.");
//        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
//        DataSource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}