package com.example.todolistapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteReminderActivity extends AppCompatActivity {

    private EditText taskNameEditText;
    private Button deleteButton;
    private Button goBackButton;
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
        goBackButton = findViewById(R.id.backButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameEditText.getText().toString().trim();

                if (!taskName.isEmpty()) {
                    // Delete the reminder from the database
                    deleteReminder(taskName);
                } else {
                    Toast.makeText(DeleteReminderActivity.this, "Please enter a task name", Toast.LENGTH_SHORT).show();
                }
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

    private void deleteReminder(String taskName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrieve the reminder from the database
                Reminder reminder = appDatabase.reminderDao().findByTaskName(taskName);

                if (reminder != null) {
                    // Delete the reminder from the database
                    appDatabase.reminderDao().delete(reminder);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeleteReminderActivity.this, "Reminder deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeleteReminderActivity.this, "Reminder not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
