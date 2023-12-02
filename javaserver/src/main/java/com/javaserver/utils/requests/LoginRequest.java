package com.javaserver.utils.requests;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {
    }
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName){
        this.username = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
