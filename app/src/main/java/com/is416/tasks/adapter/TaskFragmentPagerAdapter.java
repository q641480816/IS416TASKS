package com.is416.tasks.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.is416.tasks.TasksActivity;
import com.is416.tasks.component.TmrTaskFragment;
import com.is416.tasks.component.TodayTaskFragment;

/**
 * Created by Gods on 2/5/2018.
 */

public class TaskFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    private TodayTaskFragment todayTaskFragment;
    private TmrTaskFragment tmrTaskFragment;

    public TaskFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        todayTaskFragment = new TodayTaskFragment();
        tmrTaskFragment = new TmrTaskFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case TasksActivity.PAGE_ONE:
                fragment = todayTaskFragment;
                break;
            case TasksActivity.PAGE_TWO:
                fragment = tmrTaskFragment;
                break;
        }
        return fragment;
    }

    public void undo(int i){
        this.todayTaskFragment.undo(i);
    }

    public void deleteTask(int i){
        this.todayTaskFragment.deleteTask(i);
    }

    public void updateReminder(){
        this.todayTaskFragment.updateReminder();
    }
}
