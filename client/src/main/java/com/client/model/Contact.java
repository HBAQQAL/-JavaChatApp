package com.client.model;

public class Contact {
    private String firstName;
    private String lastName;
    private String username;

    public Contact() {
        this.firstName = "";
        this.lastName = "";
        this.username = "";
    }

    public Contact(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public String getNames() {
        return this.firstName + " " + this.lastName;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return "Contact [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + "]";
    }

}
