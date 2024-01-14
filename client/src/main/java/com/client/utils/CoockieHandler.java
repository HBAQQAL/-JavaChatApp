package com.client.utils;

public class CoockieHandler {
    private static CoockieHandler instance;
    private String coockie;
    private String JSESSIONID;
    private String userUsername;

    private CoockieHandler() {
        // Private constructor to prevent instantiation from outside the class
    }

    public static CoockieHandler getInstance() {
        if (instance == null) {
            synchronized (CoockieHandler.class) {
                if (instance == null) {
                    instance = new CoockieHandler();
                }
            }
        }
        return instance;
    }

    public String getCoockie() {
        return coockie;
    }

    public void setCoockie(String coockie) {
        this.coockie = coockie;
    }

    public static String getCoockie(String coockie) {
        return coockie.split(";")[0];
    }

    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String jSESSIONID) {
        JSESSIONID = jSESSIONID;
    }

    public static String getCoockieFromHeader(String header) {
        return header.split(";")[0];
    }

    public void setCoockieFromHeader(String header) {
        this.coockie = header.split(";")[0];
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

}
