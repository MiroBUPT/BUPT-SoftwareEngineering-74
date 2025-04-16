package com.example.personalfinance;

public interface SavingManager {
    static SavingManager getinstance() {
        return SavingManagerImpl.getInstance();
    }

    void saveData();
    void initData();
}