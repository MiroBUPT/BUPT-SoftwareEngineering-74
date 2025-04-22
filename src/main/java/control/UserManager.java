package control;

import entity.User;

import main.java.control.Manager;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

public class UserManager extends Manager {
    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    private String currentUserId;
    private List<User> userList = new ArrayList<>();

    @Override
    public void Init() {
        System.out.println("UserManager initialized.");
    }

    public List<User> getUserList() {
        return userList;
    }

    /**
     * 根据用户ID获取用户名
     * 
     * @param userId 用户ID
     * @return 用户名，如果用户不存在则返回null
     */
    public String getUserName(String userId) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                return user.name;
            }
        }
        return null;
    }

    /**
     * 修改用户密码
     * 
     * @param userId   用户ID
     * @param password 新密码
     * @return 是否修改成功
     */
    public boolean editPassword(String userId, String password) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                user.password = password;

                return true;

            }
        }
        return false;
    }

    /**
     * 修改用户名
     * 
     * @param userId   用户ID
     * @param userName 新用户名
     * @return 是否修改成功
     */
    public boolean editUserName(String userId, String userName) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                user.name = userName;

                return true;

            }
        }
        return false;
    }

    /**
     * 加载用户数据
     * 
     * @param users 用户列表
     */
    public void loadData(List<User> users) {
        userList.clear();
        userList.addAll(users);
    }

    /**
     * 获取当前登录用户ID
     * 
     * @return 当前用户ID
     */
    public String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * 设置当前登录用户
     * 
     * @param userId 用户ID
     */
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
    }

    /**
     * 添加新用户
     * 
     * @param user 要添加的用户对象
     * @return 是否添加成功（如果用户ID已存在则失败）
     */
    public boolean addUser(User user) {
        for (User u : userList) {
            if (u.userId.equals(user.userId)) {
                return false;
            }
        }
        userList.add(user);
        return true;
    }

    /**
     * 根据用户ID获取用户对象
     * 
     * @param userId 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    public User getUserById(String userId) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                return user;
            }
        }
        return null;
    }
}