package control;

import java.util.ArrayList;
import java.util.List;

import entity.User;

/**
 * Manager class for handling user-related operations.
 * Implements the Singleton pattern and manages user data including authentication and profile management.
 */
public class UserManager extends Manager {
    /** Singleton instance of UserManager */
    private static UserManager instance;

    /**
     * Gets the singleton instance of UserManager.
     * @return The singleton instance
     */
    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    /** ID of the currently logged-in user */
    private String currentUserId;
    /** List of all registered users */
    private List<User> userList = new ArrayList<>();

    @Override
    public void Init() {
        System.out.println("UserManager initialized.");
    }

    /**
     * Gets the list of all users.
     * @return List of all registered users
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * Gets the username for a given user ID.
     * @param userId The user ID to look up
     * @return The username, or null if user doesn't exist
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
     * Gets the user ID for a given username.
     * @param userName The username to look up
     * @return The user ID, or null if user doesn't exist
     */
    public String getUserIdByName(String userName) {
        for (User user : userList) {
            if (user.name.equals(userName)) {
                return user.userId;
            }
        }
        return null;
    }

    /**
     * Gets the password for a given username.
     * @param userName The username to look up
     * @return The password, or null if user doesn't exist
     */
    public String getPasswordByName(String userName) {
        for (User user : userList) {
            if (user.name.equals(userName)) {
                return user.password;
            }
        }
        return null;
    }

    /**
     * Gets the password for a given user ID.
     * @param userId The user ID to look up
     * @return The password, or null if user doesn't exist
     */
    public String getPassword(String userId) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                return user.password;
            }
        }
        return null;
    }

    /**
     * Updates the password for a given user ID.
     * @param userId The user ID to update
     * @param password The new password
     * @return true if update was successful, false otherwise
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
     * Updates the user ID for a given user.
     * @param userId The current user ID
     * @param newUserId The new user ID
     * @return true if update was successful, false otherwise
     */
    public boolean editUserId(String userId, String newUserId) {
        for (User user : userList) {
            if (user.userId.equals(userId)) {
                user.userId = newUserId;
                setCurrentUser(newUserId);
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the username for a given user ID.
     * @param userId The user ID to update
     * @param userName The new username
     * @return true if update was successful, false otherwise
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
     * Loads user data from a list of users.
     * @param users The list of users to load
     */
    public void loadData(List<User> users) {
        userList.clear();
        userList.addAll(users);
    }

    /**
     * Gets the ID of the currently logged-in user.
     * @return The current user ID
     */
    public String getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Sets the currently logged-in user.
     * @param userId The user ID to set as current
     */
    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
    }

    /**
     * Adds a new user to the system.
     * @param user The user object to add
     * @return true if user was added successfully, false if user ID already exists
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
     * Gets a user object by their ID.
     * @param userId The user ID to look up
     * @return The user object, or null if user doesn't exist
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