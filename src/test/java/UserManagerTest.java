import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import control.UserManager;
import entity.User;

public class UserManagerTest {

    private UserManager userManager;

    @BeforeEach
    public void setUp() {
        userManager = UserManager.getInstance();

        // 初始化用户列表
        List<User> testUsers = new ArrayList<>();

        // 创建无构造函数的 User 并手动设置字段
        User user1 = new User();
        user1.userId = "u001";
        user1.name = "Alice";
        user1.password = "123456";

        User user2 = new User();
        user2.userId = "u002";
        user2.name = "Bob";
        user2.password = "abcdef";

        testUsers.add(user1);
        testUsers.add(user2);

        userManager.loadData(testUsers);
    }

    @Test
    public void testEditPassword_Success() {
        boolean result = userManager.editPassword("u001", "newpass123");
        assertTrue(result, "修改密码应成功");
        assertEquals("newpass123", userManager.getPassword("u001"), "密码应已被更新");
    }

    @Test
    public void testEditPassword_FailForNonExistentUser() {
        boolean result = userManager.editPassword("u999", "fakepass");
        assertFalse(result, "不存在的用户应修改失败");
    }
     @Test
    public void testEditUserName_Success() {
        boolean result = userManager.editUserName("u001", "AliceNew");
        assertTrue(result);
        assertEquals("AliceNew", userManager.getUserName("u001"));
    }

    @Test
    public void testEditUserId_Success() {
        boolean result = userManager.editUserId("u001", "u999");
        assertTrue(result);
        assertEquals("u999", userManager.getCurrentUserId());  // 被设置为当前用户
        assertNotNull(userManager.getUserById("u999"));
        assertNull(userManager.getUserById("u001"));  // 旧 ID 应找不到
    }

    @Test
    public void testAddUser_Success() {
        User newUser = new User();
        newUser.userId = "u003";
        newUser.name = "Charlie";
        newUser.password = "qwerty";

        boolean added = userManager.addUser(newUser);
        assertTrue(added);
        assertEquals("Charlie", userManager.getUserName("u003"));
    }

    @Test
    public void testAddUser_Fail_ExistingId() {
        User duplicateUser = new User();
        duplicateUser.userId = "u001";  // 已存在
        duplicateUser.name = "Duplicate";
        duplicateUser.password = "pass";

        boolean added = userManager.addUser(duplicateUser);
        assertFalse(added);
    }
}
