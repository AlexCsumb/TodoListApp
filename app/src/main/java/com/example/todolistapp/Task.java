package com.example.todolistapp;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "tasks",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long taskId;
    public long userId;
    public String taskName;
    public Date dueDate;
    public String notes;
    public boolean isComplete;
}
