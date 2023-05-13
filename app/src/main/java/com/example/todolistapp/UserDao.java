package com.example.todolistapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User findByUsernameAndPassword(String username, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAdmin(User user);

    @Query("SELECT * FROM users WHERE isAdmin = 1")
    List<User> getAdmins();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users WHERE username = :username")
    void deleteByUsername(String username);

}
