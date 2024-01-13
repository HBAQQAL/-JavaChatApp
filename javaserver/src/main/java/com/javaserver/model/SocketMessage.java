package com.javaserver.model;

public class SocketMessage {
    public static int id = 0;
    public String message = null;
    public String sender;
    public String receiver;
    public String type = "connection";

    public SocketMessage(String message, String sender, String receiver, String type) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        id++;
    }

    // getter and setter methods
    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public static int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public static void setId(int id) {
        SocketMessage.id = id;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return id + " " + message + " " + sender + " " + receiver + " " + type;
    }

}
