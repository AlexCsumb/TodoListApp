package com.example.todolistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText editTextTask;
    private TextView textViewReminderTime;
    private Button buttonSetReminder;
    private Button goBackButton;
    private Calendar calendar;
    private AppDatabase appDatabase;
    private Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        editTextTask = findViewById(R.id.editTextTask);
        textViewReminderTime = findViewById(R.id.textViewReminderTime);
        buttonSetReminder = findViewById(R.id.buttonSetReminder);
        goBackButton = findViewById(R.id.backButton);

        calendar = Calendar.getInstance();
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Retrieve the selected task from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("taskId")) {
            long taskId = intent.getLongExtra("taskId", -1);
            Log.d("ReminderActivity", "Task ID: " + taskId);

            new AsyncTask<Long, Void, Task>() {
                @Override
                protected Task doInBackground(Long... params) {
                    long taskId = params[0];
                    return appDatabase.reminderDao().findTaskById(taskId);
                }

                @Override
                protected void onPostExecute(Task task) {
                    selectedTask = task;
                    Log.d("ReminderActivity", "Selected Task: " + selectedTask);

                    if (selectedTask != null) {
                        editTextTask.setText(selectedTask.getTaskName());
                    } else {
                        Toast.makeText(ReminderActivity.this, "No task found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }.execute(taskId);
        }

        textViewReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        buttonSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDateTimePicker() {
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String reminderTime = dateFormat.format(calendar.getTime());
        textViewReminderTime.setText(reminderTime);
    }

    private void saveReminder() {
        String reminderTime = textViewReminderTime.getText().toString();

        Log.d("ReminderActivity", "Selected Task in saveReminder(): " + selectedTask);

        // Create a new Reminder object
        Reminder reminder = new Reminder();

        // Assign the selected task's ID to reminder.taskId
        if (selectedTask != null) {
            reminder.taskId = selectedTask.getTaskId();
        } else {
            // Handle case when no task is selected
            Toast.makeText(this, "No task selected", Toast.LENGTH_SHORT).show();
            return;
        }

        reminder.reminderTime = calendar.getTime();

        // Insert the reminder into the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.reminderDao().insert(reminder);
            }
        }).start();

        Toast.makeText(this, "Reminder saved: " + selectedTask.getTaskName() + " at " + reminderTime, Toast.LENGTH_SHORT).show();
        finish();
    }

}
