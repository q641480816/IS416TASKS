package com.is416.tasks;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.is416.tasks.adapter.TaskListAdapter;
import com.is416.tasks.model.Task;
import com.is416.tasks.util.ActivityManager;
import com.is416.tasks.util.KeyboardChangeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private static final String name =  "TASK_LIST_ACTIVITY";
    private Context mContext;
    private List<Task> tasks;
    private ListView content;
    private TaskListAdapter taskListAdapter;
    private LinearLayout shadow;

    private KeyboardChangeListener softKeyboardStateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ActivityManager.addActivity(name, this);
        init();
        bindView();
        addListener();
    }

    private void init(){
        this.mContext = this;
        this.tasks = getTasks();
        this.taskListAdapter = new TaskListAdapter(this.tasks,this.mContext,name);
        this.softKeyboardStateHelper = new KeyboardChangeListener(this);
    }

    private void bindView(){
        this.content = findViewById(R.id.content);
        this.shadow = findViewById(R.id.shadow);

        this.content.setAdapter(this.taskListAdapter);
    }

    private void addListener(){
        softKeyboardStateHelper.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    //TODO
                } else {
                    System.out.println("cleared");
                    taskListAdapter.clearFocus();
                    shadow.requestFocus();
                }
            }
        });
    }

    public boolean addTask(){
        //
        return true;
    }

    public boolean completeTask(){
        //
        return true;
    }



    private List<Task> getTasks(){
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            tasks.add(new Task(new Date(), "test 1", false));
        }
        tasks.add(new Task(null, "test 1", false));
        return tasks;
    }



    @Override
    public void finish() {
        super.finish();
        ActivityManager.finishActivity(name);
    }
}
