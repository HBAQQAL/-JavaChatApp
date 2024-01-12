package com.client.model;

public class Message {
    private static int id = 0;
    private String message = null;
    private String sender;
    private String receiver;
    private String type = "connection";

    public Message(String message, String sender, String receiver, String type) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        id++;
    }

    @Override
    public String toString() {
        return id + " " + message + " " + sender + " " + receiver + " " + type;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Message.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
