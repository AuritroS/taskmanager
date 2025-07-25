package com.example.taskmanager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 2)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TaskDatabase.class,
                            "task_database"
                    )
                    .fallbackToDestructiveMigration()  // Ensures schema changes reset the DB
                    .allowMainThreadQueries()          // OK for simple apps, not for production
                    .build();
        }
        return instance;
    }
}
