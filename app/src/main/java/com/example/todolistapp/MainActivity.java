package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the username of the logged in user from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Display the welcome message at the top of the screen
        welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Hi " + username);
    }

}