package com.javaserver.controllers;

import java.util.HashMap;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.javaserver.utils.HibernateUtil;
import com.javaserver.utils.Sessions;
import com.javaserver.utils.requests.LoginRequest;

import io.javalin.http.Context;

public class AuthController {

    public static void login(Context context) {
        System.out.println("POST /api/login");

        if (context.body().isEmpty() || context.bodyAsClass(LoginRequest.class) == null) {
            sendBadRequestResponse(context, "Invalid request body, the body is empty or malformed");
            return;
        }

        LoginRequest loginRequest = context.bodyAsClass(LoginRequest.class);
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            sendBadRequestResponse(context, "Invalid request body, username or password is missing");
            return;
        }

        System.out.println(loginRequest.toString());

        if (isCredentialsValid(loginRequest.getUsername(), loginRequest.getPassword())) {
            handleSuccessfulLogin(context, loginRequest.getUsername());
        } else {
            sendUnauthorizedResponse(context, "Invalid username or password");
        }
    }

    public static void getIdentity(Context context) {
        System.out.println("POST /api/identity");
        String sessionId = context.cookie("SessionId");
        System.out.println("SessionId: " + sessionId);

        if (sessionId == null) {
            System.out.println("session is not found");
            sendBadRequestResponse(context, "SessionId cookie not found");
            return;
        }

        String username = context.sessionAttribute(sessionId);
        System.out.println("username: " + username);
        if (username != null) {
            // context.cookie("SessionId", sessionId);
            context.status(200);
            // send the response in the form of JSON
            Gson gson = new Gson();
            HashMap<String, String> responseHashMap = new HashMap<>();
            responseHashMap.put("username", username);
            context.result(gson.toJson(responseHashMap));

        } else {
            sendUnauthorizedResponse(context, "Unauthorized");
        }
    }

    public static void logout(Context context) {
        context.status(200);
        // TODO: delete session
        sendSuccessResponse(context, "Logout successful");
    }

    private static boolean isCredentialsValid(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
            // these comments are just for the staruml to work with generating the class
            return (Long) session
                    .createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.password = :password",
                            Long.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult() > 0;
        }
    }

    private static void handleSuccessfulLogin(Context context, String username) {
        String sessionId = Sessions.generateSessionId();
        context.sessionAttribute(sessionId, username);
        context.cookie("SessionId", sessionId);
        context.status(200);
        sendSuccessResponse(context, "Login successful");
    }

    private static void sendBadRequestResponse(Context context, String message) {
        context.status(400);
        Gson gson = new Gson();
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("message", message);
        context.result(gson.toJson(responseHashMap));
    }

    private static void sendUnauthorizedResponse(Context context, String message) {
        context.status(401);
        Gson gson = new Gson();
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("message", message);
        context.result(gson.toJson(responseHashMap));
    }

    private static void sendSuccessResponse(Context context, String message) {
        Gson gson = new Gson();
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("message", message);
        context.result(gson.toJson(responseHashMap));
    }
}
