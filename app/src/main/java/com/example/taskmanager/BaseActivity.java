package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_activity_layout);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (this instanceof MainActivity) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else if (this instanceof AddEditTaskActivity) {
            bottomNav.setSelectedItemId(R.id.nav_add);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                if (!(this instanceof MainActivity)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                return true;
            } else if (id == R.id.nav_add) {
                if (!(this instanceof AddEditTaskActivity)) {
                    startActivity(new Intent(this, AddEditTaskActivity.class));
                    finish();
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        if (contentFrame != null) {
            LayoutInflater.from(this).inflate(layoutResID, contentFrame, true);
        } else {
            super.setContentView(layoutResID);
        }
    }
}
