package com.example.todolistapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE taskId = :taskId")
    List<Reminder> getRemindersForTask(long taskId);

    @Insert
    void insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);
}
