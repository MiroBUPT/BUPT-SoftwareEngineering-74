package com.example.personalfinance;

import java.util.ArrayList;
import java.util.List;

public class UserManagerImpl implements UserManager {
    private static final UserManager INSTANCE = new UserManagerImpl();
    private List<User> userList;

    private UserManagerImpl() {
        userList = new ArrayList<>();
    }

    public static UserManager getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUserName(String userID) {
        for (User user : userList) {
            if (user.getUserId().equals(userID)) {
                return user.getUserName();
            }
        }
        return null;
    }

    @Override
    public void editPassword(String userID, String newPassword) {
        for (User user : userList) {
            if (user.getUserId().equals(userID)) {
                user.setPassword(newPassword);
                break;
            }
        }
    }

    @Override
    public void editUserName(String userID, String newUserName) {
        for (User user : userList) {
            if (user.getUserId().equals(userID)) {
                user.setUserName(newUserName);
                break;
            }
        }
    }

    @Override
    public void initData() {
        // 初始化用户数据，例如从文件读取或默认设置
    }
}