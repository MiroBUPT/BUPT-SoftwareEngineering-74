package control;

import java.util.ArrayList;
import java.util.List;

import entity.User;

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

    public String getUserName(String string) {
        return "";
    }

    public void editPassword(String userId, String password) {
    }

    public void editUserName(String userId, String userName) {
    }

    public void loadData(List<User> users) {
        userList.clear();
        userList.addAll(users);
    }
}
