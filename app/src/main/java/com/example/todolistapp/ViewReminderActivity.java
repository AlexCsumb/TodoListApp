package com.example.todolistapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewReminderActivity extends AppCompatActivity {

    private Button goBackButton;
    private TextView textViewReminders;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);

        goBackButton = findViewById(R.id.backButton);
        textViewReminders = findViewById(R.id.textViewReminders);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Fetch reminders from the database
        fetchReminders();
    }

    private void fetchReminders() {
        new AsyncTask<Void, Void, List<Reminder>>() {
            @Override
            protected List<Reminder> doInBackground(Void... voids) {
                return appDatabase.reminderDao().getAllReminders();
            }

            @Override
            protected void onPostExecute(List<Reminder> reminders) {
                displayReminders(reminders);
            }
        }.execute();
    }

    private void displayReminders(List<Reminder> reminders) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Reminder reminder : reminders) {
            stringBuilder.append("Reminder ID: ").append(reminder.reminderId)
                    .append(", Task ID: ").append(reminder.taskId)
                    .append(", Reminder Time: ").append(reminder.reminderTime)
                    .append("\n");
        }

        textViewReminders.setText(stringBuilder.toString());
    }
}
