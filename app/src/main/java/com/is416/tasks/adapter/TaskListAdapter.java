package com.is416.tasks.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.is416.tasks.R;
import com.is416.tasks.TasksActivity;
import com.is416.tasks.ctrl.TaskCtrl;
import com.is416.tasks.model.Task;
import com.is416.tasks.util.ActivityManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gods on 2/3/2018.
 */

public class TaskListAdapter extends BaseAdapter {

    private List<Task> tasks;
    private boolean isToday;
    private Context mContext;
    private String master;
    private int focusedETV;
    private SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm");
    private int oldLength;

    public TaskListAdapter(List<Task> tasks, Context mContext, String master, boolean isToday) {
        this.tasks = tasks;
        this.mContext = mContext;
        this.master = master;
        this.isToday = isToday;
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
        return tasks.get(i).isComplete();
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Task t = tasks.get(i);
        //System.out.println(oldLength - tasks.size());
        if(t.getCreated() == null){
            view = drawSeparator(LayoutInflater.from(mContext).inflate(R.layout.list_seprater,viewGroup,false));
        }else if(t.isComplete()){
            view = drawChecked(LayoutInflater.from(mContext).inflate(R.layout.task_item_checked,viewGroup,false),t, i);
        }else{
            view = drawUnchecked(LayoutInflater.from(mContext).inflate(R.layout.task_item_uncheck,viewGroup,false),t, i);
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    private View drawChecked(View view, Task task, int i){
        TextView tvc = view.findViewById(R.id.content);
        TextView tvt = view.findViewById(R.id.cTime);
        tvc.setText(task.getContent());
        tvc.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvt.setText(mContext.getResources().getString(R.string.complete_time) + sdf.format(task.getCompleted()));
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private View drawUnchecked(View view, final Task task, final int i){
        EditText etv = view.findViewById(R.id.content);
        ImageView checkbox = view.findViewById(R.id.checkbox);
        if (this.isToday) {
            checkbox.setOnClickListener(v -> {
                markComplete(i, true);
            });
        }else{
            checkbox.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_delete_black_24dp));
            checkbox.setOnClickListener(v -> {
                delete(i);
            });
        }
        etv.setText(task.getContent());
        etv.addTextChangedListener(new TaskContentWatcher(i));
        etv.setOnTouchListener((v, event) -> {
            // TODO Auto-generated method stub
            if(event.getAction()==MotionEvent.ACTION_UP){
                focusedETV = i;
            }
            return false;
        });
        if (this.focusedETV != -1 && this.focusedETV == i){
            etv.setSelection(etv.getText().length());
        }
        return view;
    }

    private View drawSeparator(View view){
        TextView tv = view.findViewById(R.id.addTask);
        tv.setTypeface(null, Typeface.ITALIC);
        tv.setOnClickListener(v -> {
            create();
        });
        return view;
    }

    public void create(){
        Calendar c = Calendar.getInstance();
        if (!this.isToday){
            c.add(Calendar.DAY_OF_MONTH,1);
        }
        Task t = new Task((new Date()).getTime()+"",c.getTime(),"");
        if (TaskCtrl.createTask(mContext,t)){
            for (int i = 0; i < tasks.size(); i++){
                if(tasks.get(i).getCreated() == null){
                    tasks.add(i,t);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void markComplete(int position, boolean isToComplete){
        Task t = TaskCtrl.markComplete(mContext, tasks.get(position), isToComplete);
        if (t != null) {
            if (isToComplete) {
                tasks.remove(position);
                tasks.add(t);
                notifyDataSetChanged();
                Toast.makeText(mContext, "Task Completed", Toast.LENGTH_SHORT).show();
            } else {
                tasks.remove(position);
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getCreated() == null) {
                        tasks.add(i, t);
                        break;
                    }
                }
                notifyDataSetChanged();
                Toast.makeText(mContext, "Task Undo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void delete(final int i){
        if (TaskCtrl.deleteTask(mContext,tasks.get(i).getId())){
            tasks.remove(i);
            Toast.makeText(mContext, "Task Deleted", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
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
            if (TaskCtrl.updateContent(mContext,tasks.get(index),s.toString())){
                tasks.get(index).setContent(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //System.out.println(editable.toString());
        }
    }
}
