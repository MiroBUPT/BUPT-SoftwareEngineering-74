package control;

import entity.User;
public class User_test{
    public static void main(String[] args){
        // 添加一些测试用户
        UserManager userManager = UserManager.getInstance();
        User user1 = new entity.User();
        user1.userId = "001";
        user1.name = "11";
        user1.password = "zhangsan123";
        userManager.addUser(user1);

        User user2 = new entity.User();
        user2.userId = "002";
        user2.name = "22";
        user2.password = "lisi456";
        userManager.addUser(user2);

        // 保存到CSV文件
        String filePath = "users.csv";
        if (SavingManager.getInstance().saveUsersToCSV()) {
            System.out.println("用户数据保存成功");
        }

        // // 从CSV文件加载
        // if SavingManager.getInstance().loadUsersFromCSV(filePath)) {
        //     System.out.println("用户数据加载成功");
        // }
    }
    
}
