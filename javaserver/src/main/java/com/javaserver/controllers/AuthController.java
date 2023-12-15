package com.javaserver.controllers;

import com.javaserver.model.User;
import com.javaserver.utils.Sessions;
import com.javaserver.utils.requests.LoginRequest;
import io.javalin.http.Context;

public class AuthController {
    public static void login(Context context) {

        LoginRequest loginRequest = context.bodyAsClass(LoginRequest.class);

        if(User.doesUserExist(loginRequest.getUsername())){
            if (!User.isPasswordCorrect(loginRequest.getUsername(), loginRequest.getPassword())) {
                context.status(401);
                context.json("Invalid username or password");
                return;
            }

            String sessionId = Sessions.generateSessionId();
            context.sessionAttribute(sessionId,loginRequest.getUsername());
            context.cookie("SessionId", sessionId);

            context.status(200);
            context.result("POST /api/login");
        }else{
                context.status(401);
                context.json("Invalid username or password");
        }
    }



    public static void getIdentity(Context context) {
        String sessionId = context.cookie("SessionId");
        System.out.println(sessionId);
        System.out.println(context.cookie("SessionId"));
        if (sessionId == null) {
            context.status(400);
            context.result("SessionId is null");
            return;
        }
        String myValue = context.sessionAttribute(sessionId);

        context.cookie("SessionId", sessionId);
        context.status(200);
        context.result(myValue);
    }

    public static void logout(Context context) {
        context.status(200);
        context.result("POST /api/logout");
    }
}
