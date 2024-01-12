package com.javaserver.routes;

import com.javaserver.controllers.UserController;
import com.javaserver.utils.Path;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class UserRoutes {
    public static final String SEARCHUSER = "/search";

    public static void setupUserRoutes(Javalin app) {
        app.routes(() -> {
            ApiBuilder.path(Path.API.USERS, () -> {
                ApiBuilder.post(ctx -> UserController.createUser(ctx));
                ApiBuilder.get(ctx -> UserController.getAllUsers(ctx));
                ApiBuilder.put(ctx -> UserController.updateUser(ctx));
                ApiBuilder.delete(ctx -> UserController.deleteUser(ctx));

                ApiBuilder.path(SEARCHUSER, () -> {
                    ApiBuilder.get(ctx -> UserController.searchUser(ctx));
                    // ApiBuilder.post(ctx -> {
                    // ctx.status(200);
                    // ctx.result("POST /users/search");
                    // });
                });
            });
        });
    }
}