package com.javaserver.controllers;

import com.javaserver.model.User;

import io.javalin.http.Context;

public class UserController {

    public static void getUser(Context context) {
        context.status(200);
        context.result(new User().getUser());
    }

    public static void createUser(Context context) {
        context.status(200);
        context.result(new User().createUser());
    }

    public static void getUserById(Context context) {
        context.status(200);
        context.result("GET /user/:id");
    }

    public static  void  updateUserById(Context context) {
        context.status(200);
        context.result("PUT /user/:id");
    }

    public  static void deleteUserById(Context context) {
        context.status(200);
        context.result("DELETE /user/:id");
    }

    public  static void getAllUsers(Context context) {
        context.status(200);
        context.result("GET /allusers");
    }

}
