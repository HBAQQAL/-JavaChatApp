package com.javaserver.routes;

import com.javaserver.controllers.AuthController;
import com.javaserver.utils.Path;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class AuthRoutes {
    public static void setupAuthRoutes(Javalin app) {
        app.routes(() -> {
            ApiBuilder.path("/api", () -> {
                ApiBuilder.post("/login",ctx -> AuthController.login(ctx));
                ApiBuilder.get("/identity",ctx -> AuthController.getIdentity(ctx));
            });

        });
    }
}
