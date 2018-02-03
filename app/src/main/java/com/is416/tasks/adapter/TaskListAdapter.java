package com.is416.tasks.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.is416.tasks.R;
import com.is416.tasks.model.Task;

import java.util.List;

/**
 * Created by Gods on 2/3/2018.
 */

public class TaskListAdapter extends BaseAdapter {

    private List<Task> tasks;
    private Context mContext;

    public TaskListAdapter(List<Task> tasks, Context mContext) {
        this.tasks = tasks;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tasks.get(i).getCreated() == null? -1L : tasks.get(i).getCreated().getTime();
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Task t = tasks.get(i);
        if(t.getCreated() == null){
            return LayoutInflater.from(mContext).inflate(R.layout.list_seprater,viewGroup,false);
        }else if(t.isComplete()){
            return drawChecked(LayoutInflater.from(mContext).inflate(R.layout.task_item_checked,viewGroup,false),t);
        }else{
            return drawUnchecked(LayoutInflater.from(mContext).inflate(R.layout.task_item_uncheck,viewGroup,false),t);
        }
    }

    private View drawChecked(View view, Task task){
        TextView tv = view.findViewById(R.id.content);
        tv.setText(task.getContent());
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        return view;
    }

    private View drawUnchecked(View view, Task task){
        EditText etv = view.findViewById(R.id.content);
        etv.setText(task.getContent());
        return view;
    }
}
