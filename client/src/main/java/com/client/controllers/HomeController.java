package com.client.controllers;

import java.util.Map;
import com.client.utils.ApiClient;

import javafx.fxml.FXML;

public class HomeController {

    @FXML
    public void handleButtonClick() {
        // Add your login logic here (e.g., check credentials, open a new scene, etc.)
        System.out.println("Button clicked! ");

        Map<String, Object> response = ApiClient.makeHttpRequest("http://localhost:7000/api/identity", "", "GET");

        System.out.println(response);

    }

}
