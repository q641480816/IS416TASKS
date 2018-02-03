package com.is416.tasks;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.is416.tasks.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private Context mContext;
    private List<Task> tasks;
    private ListView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        bindView();
        init();
    }

    private void bindView(){
        this.content = findViewById(R.id.content);
    }

    private void init(){
        this.mContext = this;
        this.tasks = getTasks();
    }

    private List<Task> getTasks(){
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            tasks.add(new Task(new Date(), "test 1", false));
        }
        tasks.add(new Task(null, "test 1", false));
        for (int i = 0; i < 4; i++){
            tasks.add(new Task(new Date(), "test 1", true));
        }
        return tasks;
    }

}
