package com.example.personalfinance;

public interface UserManager {
    static UserManager getinstance() {
        return UserManagerImpl.getInstance();
    }

    String getUserName(String userID);
    void editPassword(String userID, String newPassword);
    void editUserName(String userID, String newUserName);
    void initData();
}