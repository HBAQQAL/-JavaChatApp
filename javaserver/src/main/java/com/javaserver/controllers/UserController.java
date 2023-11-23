package com.javaserver.controllers;

import com.javaserver.model.User;

import io.javalin.http.Context;

public class UserController {

    public void getUser(Context context) {
        context.status(200);
        context.result(new User().getUser());
    }

    public void createUser(Context context) {
        context.status(200);
        context.result(new User().createUser());
    }

    public void getUserById(Context context) {
        context.status(200);
        context.result("GET /user/:id");
    }

    public void updateUserById(Context context) {
        context.status(200);
        context.result("PUT /user/:id");
    }

    public void deleteUserById(Context context) {
        context.status(200);
        context.result("DELETE /user/:id");
    }

    public void getAllUsers(Context context) {
        context.status(200);
        context.result("GET /allusers");
    }

}
