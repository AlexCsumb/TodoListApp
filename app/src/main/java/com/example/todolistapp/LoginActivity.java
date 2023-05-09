package com.example.todolistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the login button
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        // Set an onClickListener to handle login attempts
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the username and password entered by the user
                EditText usernameEditText = findViewById(R.id.usernameEditText);
                EditText passwordEditText = findViewById(R.id.passwordEditText);
                // Get the username and password entered by the user
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Use an AsyncTask to query the database for the user on a background thread
                new AsyncTask<Void, Void, User>() {
                    @Override
                    protected User doInBackground(Void... voids) {
                        UserDao userDao = AppDatabase.getInstance(LoginActivity.this).userDao();
                        return userDao.findByUsernameAndPassword(username, password);
                    }

                    @Override
                    protected void onPostExecute(User user) {
                        // If the user is found, launch the main activity
                        if (user != null) {
                            // Store the username of the logged in user in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", user.getUsername());
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If the user is not found, display an error message
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });

        // Set an onClickListener to handle sign up attempts
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the username and password entered by the user
                EditText usernameEditText = findViewById(R.id.usernameEditText);
                EditText passwordEditText = findViewById(R.id.passwordEditText);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Create a new user with the provided username and password
                User user = new User(username, password);

                // Use an AsyncTask to insert the new user into the database on a background thread
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        UserDao userDao = AppDatabase.getInstance(LoginActivity.this).userDao();
                        userDao.insert(user);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // Display a success message to the user
                        Toast.makeText(LoginActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            }
        });
    }
}
