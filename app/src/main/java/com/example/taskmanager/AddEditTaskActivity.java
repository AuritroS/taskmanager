package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTaskActivity extends BaseActivity {

    private EditText editTextTitle, editTextDescription;
    private TextView textViewDueDate;
    private Button buttonSave, buttonPickDate;

    private TaskDatabase database;
    private int taskId = -1;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewDueDate = findViewById(R.id.text_view_due_date);
        buttonPickDate = findViewById(R.id.button_pick_date);
        buttonSave = findViewById(R.id.button_save);
        database = TaskDatabase.getInstance(this);

        if (getIntent().hasExtra("task_id")) {
            taskId = getIntent().getIntExtra("task_id", -1);
            editTextTitle.setText(getIntent().getStringExtra("title"));
            String incomingDescription = getIntent().getStringExtra("description");
            editTextDescription.setText(incomingDescription != null ? incomingDescription : "");
            selectedDate = getIntent().getStringExtra("due_date");
            textViewDueDate.setText("Due Date: " + selectedDate);
        }

        buttonPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    AddEditTaskActivity.this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        selectedDate = sdf.format(calendar.getTime());
                        textViewDueDate.setText("Due Date: " + selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(this, "Please pick a due date", Toast.LENGTH_SHORT).show();
            return;
        }

        String safeDescription = description.isEmpty() ? "" : description;

        Task task = new Task(title, safeDescription, selectedDate);

        if (taskId == -1) {
            database.taskDao().insert(task);
            Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
        } else {
            task.setId(taskId);
            database.taskDao().update(task);
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
