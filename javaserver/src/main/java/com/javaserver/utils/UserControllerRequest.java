package com.javaserver.utils;

public class UserControllerRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String dateOfBirth;

    public UserControllerRequest() {
    }

    public UserControllerRequest(String firstName, String lastName, String username, String password,
            String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "UserControllerRequest [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
                + ", password=" + password + ", dateOfBirth=" + dateOfBirth + "]";
    }

    public boolean isValid() throws RuntimeException {
        if (this.firstName == null || this.firstName.length() < 1) {
            throw new RuntimeException("first name is not valid");
        }
        if (this.lastName == null || this.lastName.length() < 1) {
            throw new RuntimeException("last name is not valid");
        }
        if (this.username == null || this.username.length() < 4) {

            throw new RuntimeException("username is not valid");
        }
        if (this.password == null || this.password.length() < 8) {
            throw new RuntimeException("password is not valid");
        }
        if (this.dateOfBirth == null || this.dateOfBirth.length() < 1) {
            throw new RuntimeException("date of birth is not valid");
        }
        return true;
    }

}
