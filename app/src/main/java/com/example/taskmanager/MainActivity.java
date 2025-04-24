package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends BaseActivity {

    private TaskDatabase database;
    private TaskAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = TaskDatabase.getInstance(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();
    }

    private void loadTasks() {
        List<Task> tasks = database.taskDao().getAllTasks();
        adapter = new TaskAdapter(tasks, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onEdit(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("task_id", task.getId());
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("due_date", task.getDueDate());
                startActivity(intent);
            }

            @Override
            public void onDelete(Task task) {
                database.taskDao().delete(task);
                loadTasks();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // Refresh task list
    }
}
