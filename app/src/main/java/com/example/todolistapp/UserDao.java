package com.example.todolistapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User findByUsernameAndPassword(String username, String password);

    @Insert
    void insert(User user);
}
