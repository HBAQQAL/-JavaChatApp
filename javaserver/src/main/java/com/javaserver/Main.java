package com.javaserver;


import com.javaserver.routes.AuthRoutes;
import com.javaserver.routes.UserRoutes;

import com.javaserver.utils.Sessions;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
            config.jetty.sessionHandler(() -> Sessions.fileSessionHandler());
        });
        app.error(404, ctx -> {
            ctx.result("Not found");
        });
        UserRoutes.setupUserRoutes(app);
        AuthRoutes.setupAuthRoutes(app);

        app.start(7000);
    }
}

