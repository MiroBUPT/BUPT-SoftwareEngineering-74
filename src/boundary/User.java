package com.example.personalfinance;

public class User {
    private String userId;
    private String userName;
    private String password;
    private String location;
    private boolean isIncome;

    public User(String userId, String userName, String password, String location, boolean isIncome) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.location = location;
        this.isIncome = isIncome;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public boolean isIncome() {
        return isIncome;
    }
}