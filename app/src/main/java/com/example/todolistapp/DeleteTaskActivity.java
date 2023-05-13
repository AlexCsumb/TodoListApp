package com.example.todolistapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteTaskActivity extends AppCompatActivity {

    private EditText taskNameEditText;
    private Button goBackButton;
    private Button deleteButton;
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_task);

        // Initialize database instance
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Initialize UI components
        taskNameEditText = findViewById(R.id.taskNameEditText);
        deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameEditText.getText().toString().trim();

                if (!taskName.isEmpty()) {
                    // Delete the task from the database
                    deleteTask(taskName);
                } else {
                    Toast.makeText(DeleteTaskActivity.this, "Please enter a task name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBackButton = findViewById(R.id.backButton);
        // Go back button click listener
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteTask(String taskName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrieve the task from the database
                Task task = appDatabase.taskDao().findByName(taskName);

                if (task != null) {
                    // Delete the task from the database
                    appDatabase.taskDao().delete(task);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeleteTaskActivity.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeleteTaskActivity.this, "Task not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
