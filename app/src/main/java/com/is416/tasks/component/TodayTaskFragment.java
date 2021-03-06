package com.is416.tasks.component;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.is416.tasks.R;
import com.is416.tasks.TasksActivity;
import com.is416.tasks.adapter.TaskListAdapter;
import com.is416.tasks.ctrl.TaskCtrl;
import com.is416.tasks.model.Task;
import com.is416.tasks.util.ActivityManager;
import com.is416.tasks.util.SharedPreferenceManager;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Gods on 2/5/2018.
 */

public class TodayTaskFragment extends Fragment {
    private static final String alarmName = "reminder";

    private Context mContext;
    private List<Task> tasks;
    private String master;
    private LayoutInflater inflater;

    private View mainView;
    private ListView content;
    private TaskListAdapter taskListAdapter;
    private View listHeader;
    private View listFooter;
    private TextView alarm;
    private LinearLayout reminder;
    private TextView header;
    private RelativeLayout nextPage;
    private LinearLayout shadow;
    private InputMethodManager imm;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.task_fragment, container, false);
        this.inflater = inflater;
        init();
        bindView();
        addListener();
        return mainView;
    }

    private void init(){
        this.master = ((TasksActivity) ActivityManager.getActivity("TASK_LIST_ACTIVITY")).getName();
        this.mContext = ((TasksActivity) ActivityManager.getActivity("TASK_LIST_ACTIVITY")).getContext();
        this.tasks = getTasks();
        this.taskListAdapter = new TaskListAdapter(this.tasks,this.mContext, master, true);
        this.imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void bindView(){
        this.content = mainView.findViewById(R.id.content);
        this.shadow = mainView.findViewById(R.id.shadow);
        this.listHeader = this.inflater.inflate(R.layout.list_header, null, false);
        this.listFooter = this.inflater.inflate(R.layout.list_footer, null, false);
        this.header = listHeader.findViewById(R.id.title);
        this.nextPage = listHeader.findViewById(R.id.nextPage);
        this.alarm = this.listFooter.findViewById(R.id.alarm);
        this.reminder = this.listFooter.findViewById(R.id.reminder);
        this.header.setText(getResources().getText(R.string.today));

        String reminder = SharedPreferenceManager.get(alarmName, mContext);
        this.alarm.setText(reminder.equals(SharedPreferenceManager.nullable) ? getResources().getText(R.string.reminder_not_set) : reminder);

        this.content.addHeaderView(this.listHeader, null, false);
        this.content.addFooterView(this.listFooter, null, false);
        this.content.setAdapter(this.taskListAdapter);
    }

    private void addListener(){
        this.nextPage.setOnClickListener(v -> {
            if (imm.isActive()){
                imm.hideSoftInputFromWindow(ActivityManager.getActivity(master).getCurrentFocus().getWindowToken(), 0);
            }
            ((TasksActivity)ActivityManager.getActivity(master)).setViewPager(1);
        });
        this.content.setOnItemClickListener((parent, view, position, id) -> {
            ((TasksActivity)ActivityManager.getActivity(master)).showBottomSheet(position-1);
        });

        this.reminder.setOnClickListener(v -> {
            //TODO
            ReminderDialog reminderDialog = new ReminderDialog(mContext, master);
            reminderDialog.show();
        });
    }

    private List<Task> getTasks(){
        List<Task> tasks = TaskCtrl.getTasks(mContext, true);
        return tasks;
    }

    public void updateReminder(){
        String reminder = SharedPreferenceManager.get(alarmName, mContext);
        this.alarm.setText(reminder.equals(SharedPreferenceManager.nullable) ? getResources().getText(R.string.reminder_not_set) : reminder);
    }

    public void undo(int i){
        this.taskListAdapter.markComplete(i, false);
    }

    public void deleteTask(int i){
        this.taskListAdapter.delete(i);
    }

}
