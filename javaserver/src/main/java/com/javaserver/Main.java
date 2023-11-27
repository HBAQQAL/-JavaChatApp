package com.javaserver;

import com.javaserver.routes.UserRoutes;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class Main {
    public static void main(String[] args) {

        // Configuring the JSON mapper
        // https://javalin.io/documentation#configuring-the-json-mapper
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
        });
        UserRoutes.setupUserRoutes(app);
        app.start(7000);

    }
}
