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
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.is416.tasks.R;
import com.is416.tasks.ctrl.TaskCtrl;
import com.is416.tasks.model.Task;
import com.is416.tasks.util.ActivityManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gods on 2/3/2018.
 */

public class TaskListAdapter extends BaseAdapter {

    private static final int TYPE_UNCHECKED = 0;
    private static final int TYPE_CHECKED = 1;
    private static final int TYPE_SEPARATOR = 2;

    private List<Task> tasks;
    private boolean isToday;
    private Context mContext;
    private String master;
    private int focusedETV;
    private SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm");
    private int oldLength;
    private InputMethodManager imm;

    public TaskListAdapter(List<Task> tasks, Context mContext, String master, boolean isToday) {
        this.tasks = tasks;
        this.mContext = mContext;
        this.master = master;
        this.isToday = isToday;
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
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
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(tasks.get(position).getCreated() == null){
            return TYPE_SEPARATOR;
        }else if(tasks.get(position).isComplete()){
            return TYPE_CHECKED;
        }else if(!tasks.get(position).isComplete()){
            return TYPE_UNCHECKED;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        Task t = tasks.get(i);
        ViewHolderSeparator separator = null;
        ViewHolderChecked checked = null;
        ViewHolderUnchecked unchecked = null;

        if(view == null){
            switch (type){
                case TYPE_SEPARATOR:
                    separator = new ViewHolderSeparator();
                    view = LayoutInflater.from(mContext).inflate(R.layout.list_seprater, viewGroup, false);
                    separator.tv = view.findViewById(R.id.addTask);
                    view.setTag(R.id.Tag_Separator,separator);
                    break;
                case TYPE_CHECKED:
                    checked = new ViewHolderChecked();
                    view = LayoutInflater.from(mContext).inflate(R.layout.task_item_checked, viewGroup, false);
                    checked.tvc = view.findViewById(R.id.content);
                    checked.tvt = view.findViewById(R.id.cTime);
                    view.setTag(R.id.Tag_Checked,checked);
                    break;
                case TYPE_UNCHECKED:
                    unchecked = new ViewHolderUnchecked();
                    view = LayoutInflater.from(mContext).inflate(R.layout.task_item_uncheck, viewGroup, false);
                    unchecked.etv = view.findViewById(R.id.content);
                    unchecked.checkbox = view.findViewById(R.id.checkbox);
                    view.setTag(R.id.Tag_Unchecked,unchecked);
                    break;
            }
        }else{
            switch (type){
                case TYPE_SEPARATOR:
                    separator = (ViewHolderSeparator) view.getTag(R.id.Tag_Separator);
                    break;
                case TYPE_CHECKED:
                    checked = (ViewHolderChecked) view.getTag(R.id.Tag_Checked);
                    break;
                case TYPE_UNCHECKED:
                    unchecked = (ViewHolderUnchecked) view.getTag(R.id.Tag_Unchecked);
                    break;
            }
        }

        switch (type){
            case TYPE_SEPARATOR:
                separator = drawSeparator((ViewHolderSeparator) view.getTag(R.id.Tag_Separator));
                break;
            case TYPE_CHECKED:
                checked = drawChecked((ViewHolderChecked) view.getTag(R.id.Tag_Checked), t, i);
                break;
            case TYPE_UNCHECKED:
                unchecked = drawUnchecked((ViewHolderUnchecked) view.getTag(R.id.Tag_Unchecked), t,i);
                break;
        }

        //
        return view;
    }

    @SuppressLint("SetTextI18n")
    private ViewHolderChecked drawChecked(ViewHolderChecked holder, Task task, int i){
        System.out.println("test");
        holder.tvc.setText(task.getContent());
        holder.tvc.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvt.setText(mContext.getResources().getString(R.string.complete_time) + sdf.format(task.getCompleted()));
        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    private ViewHolderUnchecked drawUnchecked(ViewHolderUnchecked holder, final Task task, final int i){
        if (this.isToday) {
            holder.checkbox.setOnClickListener(v -> {
                markComplete(i, true);
            });
        }else{
            holder.checkbox.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_delete_black_24dp));
            holder.checkbox.setOnClickListener(v -> {
                delete(i);
            });
        }
        if (!holder.etv.getText().toString().equals(tasks.get(i).getContent())){
            holder.etv.setText(task.getContent());
        }
        holder.etv.addTextChangedListener(new TaskContentWatcher(i));
        holder.etv.setOnTouchListener((v, event) -> {
            // TODO Auto-generated method stub
            if(event.getAction()==MotionEvent.ACTION_UP){
                focusedETV = i;
            }
            return false;
        });
        if (this.focusedETV != -1 && this.focusedETV == i){
            //holder.etv.setSelection(holder.etv.getText().length());
        }
        return holder;
    }

    private ViewHolderSeparator drawSeparator(ViewHolderSeparator holder){
        holder.tv.setTypeface(null, Typeface.ITALIC);
        holder.tv.setOnClickListener(v -> {
            create();
        });
        return holder;
    }

    public void create(){
        if (imm.isActive()){
            imm.hideSoftInputFromWindow(ActivityManager.getActivity(master).getCurrentFocus().getWindowToken(), 0);
        }
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
        if (imm.isActive()){
            imm.hideSoftInputFromWindow(ActivityManager.getActivity(master).getCurrentFocus().getWindowToken(), 0);
        }
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
            if (imm.isActive()){
                imm.hideSoftInputFromWindow(ActivityManager.getActivity(master).getCurrentFocus().getWindowToken(), 0);
            }
            Toast.makeText(mContext, "Task Deleted", Toast.LENGTH_SHORT).show();
            //TODO: add animation
            tasks.remove(i);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolderSeparator{
        TextView tv;
    }

    private static class ViewHolderChecked{
        TextView tvc;
        TextView tvt;
    }

    private static class ViewHolderUnchecked{
        EditText etv;
        ImageView checkbox;
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
