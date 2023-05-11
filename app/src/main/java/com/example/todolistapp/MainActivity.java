package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the username from the Intent extras
        String username = getIntent().getStringExtra("username");

        // Set the welcome message
        TextView welcomeTextView = findViewById(R.id.welcomeMessageTextView);
        welcomeTextView.setText("Hi " + username);

        // Show/hide the "Admin" button based on the logged-in user's role
        Button adminButton = findViewById(R.id.adminButton);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                // Check if the user is an admin
                return isUserAdmin(username);
            }

            @Override
            protected void onPostExecute(Boolean isAdmin) {
                // Set the visibility of the "Admin" button based on the result
                if (isAdmin) {
                    adminButton.setVisibility(View.VISIBLE);
                } else {
                    adminButton.setVisibility(View.GONE);
                }
            }
        }.execute();

        // Logout Button
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity to go back to the login screen
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() to close the current MainActivity
            }
        });
    }

    // Check if the logged-in user is an admin
    private boolean isUserAdmin(String username) {
        // Return true if the user is an admin, false otherwise
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        UserDao userDao = appDatabase.userDao();
        User user = userDao.findByUsername(username);
        return user != null && user.isAdmin();
    }
}
