package com.javaserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaserver.model.User;
import com.javaserver.utils.UserControllerRequest;

import io.javalin.http.Context;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public static void getUser(Context context) {
        context.status(200);
        context.result("GET /user");
    }

    public static void createUser(Context context) {
        logger.info("creating user");

        UserControllerRequest userControllerRequest;

        try {
            userControllerRequest = context.bodyAsClass(UserControllerRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
            context.result("body is not valid");
            return;
        }

        boolean isbodyValid = false;

        // validate body : if an attribute is null or empty, return 400
        try {
            isbodyValid = userControllerRequest.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            context.status(400);
            context.result(e.getMessage());
            return;
        }

        User user = new User(userControllerRequest.getFirstName(), userControllerRequest.getLastName(),
                userControllerRequest.getUsername(),
                userControllerRequest.getDateOfBirth(), userControllerRequest.getPassword());
        String resulString = user.createUser();
        context.status(200);
        context.result(resulString);
    }

    public static void getUserById(Context context) {
        context.status(200);
        context.result("GET /user/:id");
    }

    public static void updateUserById(Context context) {
        context.status(200);
        context.result("PUT /user/:id");
    }

    public static void deleteUserById(Context context) {
        context.status(200);
        context.result("DELETE /user/:id");
    }

    public static void getAllUsers(Context context) {
        context.status(200);
        context.result("GET /allusers");
    }

}
