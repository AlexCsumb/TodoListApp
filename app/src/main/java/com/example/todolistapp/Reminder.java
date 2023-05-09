package com.example.todolistapp;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "reminders",
        foreignKeys = {
                @ForeignKey(
                        entity = Task.class,
                        parentColumns = "taskId",
                        childColumns = "taskId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    public long reminderId;
    public long taskId;
    public Date reminderTime;
}
