package com.javaserver.routes;

import com.javaserver.controllers.UserController;
import com.javaserver.utils.Path;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class UserRoutes {

    public static void setupUserRoutes(Javalin app) {
        app.routes(() -> {
            ApiBuilder.path(Path.API.USERS, () -> {
                ApiBuilder.post(ctx -> UserController.createUser(ctx));
                ApiBuilder.get(ctx -> UserController.getAllUsers(ctx));
                ApiBuilder.get("{id}", ctx -> UserController.getUserById(ctx));
                ApiBuilder.put( ctx -> UserController.updateUser(ctx));
                ApiBuilder.delete(ctx -> UserController.deleteUser(ctx));
            });
        });
    }
}