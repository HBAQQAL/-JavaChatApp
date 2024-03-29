package com.javaserver.controllers;

import java.util.HashMap;

// import org.eclipse.jetty.websocket.api.StatusCode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.javaserver.model.User;
import com.javaserver.utils.HibernateUtil;
import com.javaserver.utils.ResponseUtils;
import com.javaserver.utils.requests.UserControllerRequest;

import io.javalin.http.Context;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public static void getUser(Context context) {
        context.status(200);
        context.result("GET /user");
    }

    // this method is called when the user sends a POST request to api/users
    public static void createUser(Context context) {
        logger.info("Creating User");

        UserControllerRequest userControllerRequest;
        try {

            userControllerRequest = context.bodyAsClass(UserControllerRequest.class);
            System.out.println(userControllerRequest.toString());
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
            Gson gson = new Gson();
            HashMap<String, String> responseHashMap = new HashMap<>();
            responseHashMap.put("message", "Invalid request body");

            context.result(
                    gson.toJson(responseHashMap).toString());
            return;
        }

        // validate body: if an attribute is null or empty, return 400
        try {
            userControllerRequest.isValid();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            context.status(400);

            ResponseUtils.createResponse(e.getMessage(), context);

            return;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            context.status(400);
            context.result(e.getMessage());
            return;
        }

        // Create the user and handle the response
        createUserWithResponse(context, userControllerRequest);
    }

    public static void searchUser(Context context) {
        System.out.println("GET /users/search");

        String username = context.queryParam("username");
        System.out.println(username);
        if (username == null || username.isEmpty() || username.equals("")) {
            ResponseUtils.createResponse("Invalid username", context, 400);
            return;
        }
        User user = null;
        try {
            user = getUserInfo(username);
            if (user == null) {
                HashMap<String, String> responseHashMap = new HashMap<>();
                responseHashMap.put("message", "User not found");
                ResponseUtils.createResponse(responseHashMap, context, 200);
                return;
            }
        } catch (Exception e) {
            // TODO: handle exception
            ResponseUtils.createResponse(e.getMessage(), context, 400);
            return;
        }
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("username", user.getUsername());
        responseHashMap.put("firstName", user.getFirstname());
        responseHashMap.put("lastName", user.getLastname());

        ResponseUtils.createResponse(responseHashMap, context, 200);
    }

    private static User getUserInfo(String username) throws RuntimeException {
        try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
            User user = (User) session.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            System.out.println(user.toString());
            return user;
        }
    }

    private static void createUserWithResponse(Context context, UserControllerRequest userControllerRequest) {
        User user = new User(
                userControllerRequest.getFirstName(),
                userControllerRequest.getLastName(),
                userControllerRequest.getUsername(),
                userControllerRequest.getDateOfBirth(),
                userControllerRequest.getPassword());

        // Check if the username already exists
        if (isUsernameExists(user.getUsername())) {
            context.status(com.javaserver.utils.StatusCode.BAD_REQUEST); // Conflict status code
            context.result("Username already exists");
        } else {
            String resultString = createUser(user);
            // check if the user was created successfully
            if (resultString.equals("Failed to create user")) {
                context.status(com.javaserver.utils.StatusCode.INTERNAL_SERVER_ERROR);
            } else {
                context.status(com.javaserver.utils.StatusCode.CREATED);
            }
            Gson gson = new Gson();
            HashMap<String, String> responseHashMap = new HashMap<>();
            responseHashMap.put("message", resultString);
            context.result(
                    gson.toJson(responseHashMap).toString());
        }
    }

    private static String createUser(User user) {
        try {
            if (isUsernameExists(user.getUsername())) {
                return "Username already exists";
            }

            saveUser(user);
            return "User created";
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return "Failed to create user";
        }
    }

    private static boolean isUsernameExists(String username) {
        try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
            return (Long) session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .uniqueResult() > 0;
        }
    }

    private static void saveUser(User user) {
        try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    public static void getUserById(Context context) {
        context.status(200);
        context.result("GET /user/:id");
    }

    public static void updateUser(Context context) {
        context.status(200);
        context.result("PUT /user/:id");
    }

    public static void deleteUser(Context context) {
        context.status(200);
        context.result("DELETE /user/:id");
    }

    public static void getAllUsers(Context context) {
        context.status(200);
        context.result("GET /allusers");
    }
}
