package com.is416.tasks;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.is416.tasks.adapter.TaskFragmentPagerAdapter;
import com.is416.tasks.util.ActivityManager;
import com.is416.tasks.util.KeyboardChangeListener;

public class TasksActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    private static final String name =  "TASK_LIST_ACTIVITY";
    private Context mContext;
    private ViewPager viewPager;
    private TaskFragmentPagerAdapter taskFragmentPagerAdapter;
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
        this.taskFragmentPagerAdapter = new TaskFragmentPagerAdapter(getSupportFragmentManager());
        this.softKeyboardStateHelper = new KeyboardChangeListener(this);
    }

    private void bindView(){
        this.viewPager = findViewById(R.id.viewPager);
        this.viewPager.setAdapter(this.taskFragmentPagerAdapter);
        this.viewPager.setCurrentItem(0);
    }

    private void addListener(){

    }

    public boolean addTask(){
        //
        return true;
    }

    public boolean completeTask(){
        //
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        ActivityManager.finishActivity(name);
    }

    public Context getContext(){
        return this.mContext;
    }

    public String getName(){
        return name;
    }

    public void setViewPager(int i){
        this.viewPager.setCurrentItem(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
