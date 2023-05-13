package com.example.todolistapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todolistapp.Converters;
import com.example.todolistapp.Reminder;
import com.example.todolistapp.ReminderDao;
import com.example.todolistapp.Task;
import com.example.todolistapp.TaskDao;
import com.example.todolistapp.User;
import com.example.todolistapp.UserDao;

@Database(entities = {User.class, Task.class, Reminder.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance; // Add volatile keyword for thread safety

    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
    public abstract ReminderDao reminderDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "todo-list-db")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    UserDao userDao = instance.userDao();
                    User adminUser = new User("alex", "123", true);
                    User User = new User("user", "123", false);
                    userDao.insertAdmin(adminUser);
                    userDao.insertAdmin(User);
                    Log.d("AppDatabase", "Admin user added to the database");
                    return null;
                }
            }.execute();
        }
    };
}
