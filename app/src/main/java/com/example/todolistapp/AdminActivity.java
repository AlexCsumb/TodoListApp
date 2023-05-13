package com.example.todolistapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button deleteButton;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize database instance
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteUser() {
        String username = usernameEditText.getText().toString().trim();

        if (!username.isEmpty()) {
            // Retrieve the user from the database
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = appDatabase.userDao().findByUsername(username);

                    if (user != null) {
                        // Delete the user from the database
                        appDatabase.userDao().delete(user);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Display a success message to the user
                                Toast.makeText(AdminActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                // Clear the input field
                                usernameEditText.setText("");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Display an error message if the user is not found
                                Toast.makeText(AdminActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }
    }
}
