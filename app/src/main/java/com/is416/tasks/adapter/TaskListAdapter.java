package com.is416.tasks.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.is416.tasks.R;
import com.is416.tasks.TasksActivity;
import com.is416.tasks.model.Task;
import com.is416.tasks.util.ActivityManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Gods on 2/3/2018.
 */

public class TaskListAdapter extends BaseAdapter {

    private List<Task> tasks;
    private Context mContext;
    private String master;
    private int focusedETV;
    private SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm");
    private int oldLength;

    public TaskListAdapter(List<Task> tasks, Context mContext, String master) {
        this.tasks = tasks;
        this.mContext = mContext;
        this.master = master;
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
        System.out.println(oldLength - tasks.size());
        if(t.getCreated() == null){
            view = drawSeparator(LayoutInflater.from(mContext).inflate(R.layout.list_seprater,viewGroup,false));
        }else if(t.isComplete()){
            view = drawChecked(LayoutInflater.from(mContext).inflate(R.layout.task_item_checked,viewGroup,false),t, i);
        }else{
            view = drawUnchecked(LayoutInflater.from(mContext).inflate(R.layout.task_item_uncheck,viewGroup,false),t, i);
        }
        return view;
    }

    private View drawChecked(View view, Task task, int i){
        TextView tvc = view.findViewById(R.id.content);
        TextView tvt = view.findViewById(R.id.cTime);
        tvc.setText(task.getContent());
        tvc.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvt.setText(mContext.getResources().getString(R.string.complete_time) + sdf.format(task.getCompleted()));
        return view;
    }

    private View drawUnchecked(View view, final Task task, final int i){
        EditText etv = view.findViewById(R.id.content);
        ImageView checkbox = view.findViewById(R.id.checkbox);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((TasksActivity)ActivityManager.getActivity(master)).completeTask()){
                    Task t = tasks.remove(i);
                    t.setCompleted(new Date());
                    t.setComplete(true);
                    int index = 0;
                    for(int i = 0; i < tasks.size(); i++){
                        if(tasks.get(i).getCreated() == null){
                            index = i;
                            break;
                        }
                    }

                    //TODO:
                    tasks.add(index+1, t);
                    notifyDataSetChanged();
                }
            }
        });
        etv.setText(task.getContent());
        etv.addTextChangedListener(new TaskContentWatcher(i));
        etv.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(event.getAction()==MotionEvent.ACTION_UP){
                    focusedETV = i;
                }
                return false;
            }
        });
        if (this.focusedETV != -1 && this.focusedETV == i){
            etv.setSelection(etv.getText().length());
        }
        return view;
    }

    private View drawSeparator(View view){
        TextView tv = view.findViewById(R.id.addTask);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((TasksActivity)ActivityManager.getActivity(master)).addTask()){
                    int index = 0;
                    for(int i = 0; i < tasks.size(); i++){
                        if(tasks.get(i).getCreated() == null){
                            index = i;
                            break;
                        }
                    }

                    //TODO:
                    tasks.add(index,new Task(new Date()," new" + index, false));
                    notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    public void clearFocus(){
        focusedETV = -1;
    }

    private class TaskContentWatcher implements TextWatcher {
        private int index;
        public TaskContentWatcher(int i) {
            index = i;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //TODO
            tasks.get(index).setContent(s.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
