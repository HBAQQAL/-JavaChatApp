package com.javaserver.utils;

import java.util.HashMap;

import com.google.gson.Gson;
import io.javalin.http.Context;

public class ResponseUtils {
    public static void createResponse(String message, Context context) {
        Gson gson = new Gson();
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("message", message);
        context.result(gson.toJson(responseHashMap));
    }

    public static void createResponse(String message, Context context, int statusCode) {
        Gson gson = new Gson();
        HashMap<String, String> responseHashMap = new HashMap<>();
        responseHashMap.put("message", message);
        context.status(statusCode);
        context.result(gson.toJson(responseHashMap));
    }

    public static void createResponse(Object object, Context context, int statusCode) {
        Gson gson = new Gson();
        context.status(statusCode);
        context.result(gson.toJson(object));
    }

}