package com.client.controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;

import Socket.Client;
import Socket.Message;
import UIHandllers.MessageHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ChatController {
    @FXML
    private VBox MAIN_CONTACTS = null;
    @FXML
    private Button SEND_BTN = null;
    @FXML
    private TextArea MSG_TXT = null;
    @FXML
    private Button FILES_BTN = null;
    @FXML
    private VBox MSGS_CONTAINER = null;

    private boolean isFile = false;

    private String file = null;

    private String id = null;

    private String receiver = null;

    String fileName = null;
    MessageHandler SR = new MessageHandler();

    public void UpdateContacts(ArrayList<Socket.Contact> contactList) {
        System.out.println("updating contacts");
        System.out.println("contacts " + contactList);
        Platform.runLater(() -> {
            MAIN_CONTACTS.getChildren().clear();
            contactList.forEach(contact -> {
                System.out.println("contact " + contact);
                Button button = new Button(contact.getName());
                button.setOnAction(e -> {
                    this.receiver = contact.getName();
                });
                MAIN_CONTACTS.getChildren().add(button);

            });
        });

    }

    public void sendMessage() {

        if (isFile) {
            Message m = new Message(this.fileName, id, receiver, "file");
            System.out.println(m.getReceiver());
            System.out.println("file name is " + this.fileName);
            Client.sendMessage(m);
            Client.sendFile(file);
            isFile = false;
            return;
        }
        if (!MSG_TXT.getText().equals("")) {
            SR.sendMessage(MSG_TXT.getText(), new SimpleDateFormat("hh:mm a").format(new Date()), MSGS_CONTAINER,
                    (event) -> {
                        System.out.println("1. Clecked :D");
                    });
            System.out.println("sending message");
            this.id = MSG_TXT.getText();
            Message message = new Message(id, id, receiver, "message");
            Client.sendMessage(message);
            MSG_TXT.clear();

        }

    }

    public void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("All Files", "*.*"));

        Stage stage = (Stage) SEND_BTN.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // File selected, do something with it
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            this.file = selectedFile.getAbsolutePath();
            this.isFile = true;
            this.fileName = selectedFile.getName();

        }
    }

    public void showMessage(String message) {
        System.out.println("showing message");
        Platform.runLater(() -> {
            // create a new button for each contact
            // Label msg = new Label(message);
            // add the button to the vbox
            // MSGS_CONTAINER.getChildren().add(msg);
            SR.receiveMessage(message, new SimpleDateFormat("hh:mm a").format(new Date()), MSGS_CONTAINER, null);
            System.out.println(message);
        });

    }

}
