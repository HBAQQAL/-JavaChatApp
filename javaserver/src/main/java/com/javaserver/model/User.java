package com.javaserver.model;

public class User {
    public String createUser() {
        System.out.println("creating user in the database");
        return "POST /user";
    }

    public String getUser() {
        System.out.println("getting user fromt the database");
        return "GET /user";
    }

    public String getUserById() {
        System.out.println("getting user by id from the database");
        return "GET /user/:id";
    }

    public String getAllUsers() {
        System.out.println("getting all users from the database");
        return "GET /allusers";
    }
}
