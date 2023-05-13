package com.example.todolistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button addTaskButton;
    private Button viewTasksButton;
    private Button addReminderButton;
    private Button viewReminderButton;
    private Button adminButton;
    private Button logoutButton;
    private String loggedInUsername;
    private AppDatabase appDatabase;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database instance
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Get the logged-in username from the Intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username")) {
            loggedInUsername = intent.getStringExtra("username");
        }

        // Initialize UI components
        addTaskButton = findViewById(R.id.TaskButton);
        viewTasksButton = findViewById(R.id.viewTasksButton);
        addReminderButton = findViewById(R.id.reminderButton);
        viewReminderButton = findViewById(R.id.buttonViewReminder);
        adminButton = findViewById(R.id.adminButton);
        logoutButton = findViewById(R.id.logoutButton);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Log.d("MainActivity", "Retrieved username: " + username);
        // Display the welcome message at the top of the screen
        welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Hi " + username);

        // Set OnClickListener for addTaskButton
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TaskActivity
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for viewTasksButton
        viewTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ViewTaskActivity
                Intent intent = new Intent(MainActivity.this, ViewTaskActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for addReminderButton
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ReminderActivity
                Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for viewReminderButton
        viewReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ViewReminderActivity
                Intent intent = new Intent(MainActivity.this, ViewReminderActivity.class);
                startActivity(intent);
            }
        });

        // Retrieve the user from the database based on the logged-in username
        new Thread(new Runnable() {
            @Override
            public void run() {
                User currentUser = appDatabase.userDao().findByUsername(loggedInUsername);
                if (currentUser != null && currentUser.isAdmin()) {
                    // User is an admin, show the admin button
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adminButton.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();

        // Set OnClickListener for adminButton (if needed)
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for deleteTaskButton
        Button deleteTaskButton = findViewById(R.id.deleteTaskButton);
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete task action
                Intent intent = new Intent(MainActivity.this, DeleteTaskActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for deleteReminderButton
        Button deleteReminderButton = findViewById(R.id.deleteReminderButton);
        deleteReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete reminder action
                Intent intent = new Intent(MainActivity.this, DeleteReminderActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}