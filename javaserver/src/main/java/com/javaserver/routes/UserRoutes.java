package com.javaserver.routes;
import com.javaserver.controllers.UserController;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class UserRoutes {

    public static void setupUserRoutes(Javalin app) {
        app.routes(() -> {
            ApiBuilder.path("api/users", () -> {
                ApiBuilder.get(ctx -> UserController.getAllUsers(ctx));
                ApiBuilder.get("{id}", ctx -> UserController.getUserById(ctx));
                ApiBuilder.post(ctx -> UserController.createUser(ctx));
                ApiBuilder.put("{id}", ctx -> UserController.updateUserById(ctx));
                ApiBuilder.delete("{id}", ctx -> UserController.deleteUserById(ctx));
            });
        });
    }
}