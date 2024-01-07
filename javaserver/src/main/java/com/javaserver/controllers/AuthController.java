package com.javaserver.controllers;

import java.util.HashMap;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.javaserver.model.User;
import com.javaserver.utils.HibernateUtil;
import com.javaserver.utils.Sessions;
import com.javaserver.utils.requests.LoginRequest;

import io.javalin.http.Context;

public class AuthController {

    public static void login(Context context) {
        System.out.println("POST /api/login");
        LoginRequest loginRequest = context.bodyAsClass(LoginRequest.class);
        System.out.println(loginRequest.toString());
        if (isUsernameExists(loginRequest.getUsername())) {
            if (!isPasswordCorrect(loginRequest.getUsername(), loginRequest.getPassword())) {
                context.status(401);
                Gson gson = new Gson();
                HashMap<String, String> responseHashMap = new HashMap<>();
                responseHashMap.put("message", "Invalid username or password");
                context.result(
                        gson.toJson(responseHashMap));

            } else {
                handleSuccessfulLogin(context, loginRequest.getUsername());
            }
        } else {
            context.status(401);
            Gson gson = new Gson();
            HashMap<String, String> responseHashMap = new HashMap<>();
            responseHashMap.put("message", "Invalid username or password");
            context.result(
                    gson.toJson(responseHashMap));
        }
    }

    private static boolean isUsernameExists(String username) {
        try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
            return (Long) session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .uniqueResult() > 0;
        }
    }

    private static boolean isPasswordCorrect(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
            return (Long) session
                    .createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.password = :password")
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
        Gson gson = new Gson();
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("message", "Login successful");
        context.result(
                gson.toJson(responseHashMap));

    }

    public static void getIdentity(Context context) {
        String sessionId = context.cookie("SessionId");
        if (sessionId == null) {
            context.status(400);
            Gson gson = new Gson();
            HashMap<String, String> responseHashMap = new HashMap<>();
            responseHashMap.put("message", "SessionId cookie not found");
            context.result(
                    gson.toJson(responseHashMap));

            return;
        }

        String username = context.sessionAttribute(sessionId);
        if (username != null) {
            context.cookie("SessionId", sessionId);
            context.status(200);
            context.result(username);
        } else {
            context.status(401);
            Gson gson = new Gson();
            HashMap<String, String> responseHashMap = new HashMap<>();
            responseHashMap.put("message", "Unauthorized");
            context.result(
                    gson.toJson(responseHashMap));

        }
    }

    public static void logout(Context context) {
        context.status(200);
        // TODO: delete session
        Gson gson = new Gson();
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("message", "Logout successful");
        context.result(
                gson.toJson(responseHashMap));

    }
}
