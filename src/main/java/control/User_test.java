package control;

import entity.User;

import java.util.List;

public class User_test {
    public static void main(String[] args) {
        // 获取 SavingManager 实例并初始化
        SavingManager savingManager = SavingManager.getInstance();
        savingManager.Init();

        // 获取 UserManager 实例
        UserManager userManager = UserManager.getInstance();


        // 创建新用户
        User newUser = new User();
        newUser.userId = "0013";
        newUser.name = "newUser";
        newUser.password = "newPassword";


        // 添加新用户到用户列表
        if (userManager.addUser(newUser)) {
            savingManager.saveUsersToCSV();
            System.out.println("新用户添加成功");
        } else {
            System.out.println("新用户添加失败，用户 ID 已存在");

        }

        // 获取用户列表
        List<User> userList = userManager.getUserList();

        // 打印用户信息
        if (userList.isEmpty()) {
            System.out.println("当前没有用户信息。");
        } else {
            System.out.println("当前用户信息如下：");
            for (User user : userList) {
                System.out.println("用户ID: " + user.userId + ", 用户名: " + user.name + ", 密码: " + user.password);
            }
        }
    }
}