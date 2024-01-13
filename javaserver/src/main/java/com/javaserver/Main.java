package com.javaserver;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.javaserver.routes.AuthRoutes;
import com.javaserver.routes.UserRoutes;
import com.javaserver.utils.Sessions;
import Sockets.Server;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
            config.jetty.sessionHandler(() -> Sessions.fileSessionHandler());
            config.jsonMapper(new GsonJsonMapper());
        });
        app.error(404, ctx -> {
            ctx.result("Not found");
        });
        UserRoutes.setupUserRoutes(app);
        AuthRoutes.setupAuthRoutes(app);
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

        // @Override
        // public String toJson(Object obj) {
        // return gson.toJson(obj);
        // }

        // @Override
        // public <T> T fromJson(String json, Class<T> targetClass) {
        // return gson.fromJson(json, targetClass);
        // }

        // @Override
        // public <T> T fromJson(String json, TypeToken<T> targetType) {
        // return gson.fromJson(json, targetType.getType());
        // }
    }
}
