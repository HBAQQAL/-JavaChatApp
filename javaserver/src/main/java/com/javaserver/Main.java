package com.javaserver;
import com.javaserver.routes.UserRoutes;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(/*config*/);
        UserRoutes.setupUserRoutes(app);
        app.start(7000);

        

        // app.get("/user", new UserController()::getUser);
        // app.post("/user", new UserController()::createUser);
        // app.get("/user/{id}", new UserController()::getUserById);
        // app.put("/user/{id}", new UserController()::updateUserById);
        // app.delete("/user/{id}", new UserController()::deleteUserById);
        // app.get("/allusers", new UserController()::getAllUsers);


    }
}
