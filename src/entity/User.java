package entity;

import control.Manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    public String userId;
    public String name;
    public String password;
    private double income; // 添加收入字段

    // 构造函数
    public User(String userId, String name, String password, double income) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.income = income;
    }

    // userId 的 getter 和 setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // name 的 getter 和 setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // password 的 getter 和 setter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // income 的 getter 和 setter
    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }



    }


