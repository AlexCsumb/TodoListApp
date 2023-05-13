package com.example.todolistapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {

    private EditText editTextTaskName;
    private Button addButton;
    private Button goBackButton;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Initialize database instance
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Initialize UI components
        editTextTaskName = findViewById(R.id.editTextTaskName);
        addButton = findViewById(R.id.addButton);
        goBackButton = findViewById(R.id.backButton);

        // Add task button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        // Go back button click listener
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addTask() {
        String taskName = editTextTaskName.getText().toString().trim();

        if (!taskName.isEmpty()) {
            // Create a new Task object
            Task task = new Task(taskName);

            // Retrieve the current user from the database
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User currentUser = appDatabase.userDao().findByUsername("alex");
                    // Check if the current user exists
                    if (currentUser != null) {
                        // Set the userId for the Task object
                        task.setUserId(currentUser.getUserId());

                        // Insert the task into the database
                        appDatabase.taskDao().insert(task);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Clear the input field after adding the task
                                editTextTaskName.setText("");

                                // Display a success message
                                Toast.makeText(TaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Display an error message if the current user is not found
                                Toast.makeText(TaskActivity.this, "Current user not found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
