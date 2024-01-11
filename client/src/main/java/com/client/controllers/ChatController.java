package com.client.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ChatController {
    @FXML
    private VBox MAIN_CONTACTS = null;
    @FXML
    private Button SEND_BTN = null;
    @FXML
    private TextArea MSG_TXT = null;

    @FXML
    private VBox MSGS_CONTAINER = null;

    private String id = null;

    private String receiver = null;

    public void UpdateContacts(ArrayList<Socket.Contact> contacts) {
        System.out.println("updating contacts");
        System.out.println("contacts " + contacts);
        Platform.runLater(() -> {
            MAIN_CONTACTS.getChildren().clear();
            contacts.forEach(contact -> {
                System.out.println("contact " + contact);
                // create a new button for each contact
                Button button = new Button(contact.getName());
                // add an action listener to the button
                button.setOnAction(e -> {
                    // set the receiver to the name of the contact
                    this.receiver = contact.getName();
                });
                // add the button to the vbox
                MAIN_CONTACTS.getChildren().add(button);

            });
        });

    }

    public void sendMessage() {
        System.out.println("sending message");
        this.id = MSG_TXT.getText();
        // Message message = new Message(id, id, receiver, "message");
        // Client.sendMessage(message.toString());
        MSG_TXT.clear();

    }

    public void showMessage(String message) {
        System.out.println("showing message");
        Platform.runLater(() -> {
            // create a new button for each contact
            Label msg = new Label(message);
            // add the button to the vbox
            MSGS_CONTAINER.getChildren().add(msg);
        });

    }

}
