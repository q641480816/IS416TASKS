package com.is416.tasks;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

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

    public void showBottomSheet(int position){
        final BottomSheetDialog dialog=new BottomSheetDialog(mContext);
        View dialogView= LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet,null);
        LinearLayout undo = dialogView.findViewById(R.id.undo);
        LinearLayout delete = dialogView.findViewById(R.id.delete);

        undo.setOnClickListener((v) -> {
            this.taskFragmentPagerAdapter.undo(position);
            dialog.dismiss();
        });

        delete.setOnClickListener((v) -> {
            this.taskFragmentPagerAdapter.deleteTask(position);
            dialog.dismiss();
        });

        dialog.setContentView(dialogView);
        dialog.show();
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
