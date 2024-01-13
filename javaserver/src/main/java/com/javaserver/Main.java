package com.javaserver;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.javaserver.Sockets.Server;
import com.javaserver.routes.AuthRoutes;
import com.javaserver.routes.MessageRoutes;
import com.javaserver.routes.UserRoutes;
import com.javaserver.utils.Sessions;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
            config.jetty.sessionHandler(() -> Sessions.fileSessionHandler());
            config.jsonMapper(new GsonJsonMapper());
            config.plugins.enableDevLogging();
        });
        app.error(404, ctx -> {
            // print the error message
            System.out.println(ctx);
            // set the response status code
            ctx.status(404);
            // send the response body in JSON format
            ctx.json("Not found");

        });

        app.exception(Exception.class, (e, ctx) -> {
            // print the error message
            System.out.println(e.getMessage());
            // set the response status code
            ctx.status(500);
            // send the response body in JSON format
            ctx.json("Internal server error");

        });
        UserRoutes.setupUserRoutes(app);
        AuthRoutes.setupAuthRoutes(app);
        MessageRoutes.setupMessageRoutes(app);
        new Server(7001);
        app.start(7000);

    }

    static class GsonJsonMapper implements io.javalin.json.JsonMapper {

        private final Gson gson = new Gson();

        @Override
        public <T> T fromJsonString(String json, Type targetType) {
            return gson.fromJson(json, targetType);
        }

        @Override
        public String toJsonString(Object obj, Type type) {
            return gson.toJson(obj, type);
        }

    }
}
