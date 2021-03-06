package com.is416.tasks.component;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gods on 2/5/2018.
 */

public class TmrTaskFragment extends Fragment {
    private Context mContext;
    private List<Task> tasks;
    private String master;
    private LayoutInflater inflater;

    private View mainView;
    private ListView content;
    private TaskListAdapter taskListAdapter;
    private LinearLayout shadow;
    private View listHeader;
    private TextView header;
    private RelativeLayout nextPage;

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
        this.taskListAdapter = new TaskListAdapter(this.tasks,this.mContext, master,false);
    }

    private void bindView(){
        this.content = mainView.findViewById(R.id.content);
        this.shadow = mainView.findViewById(R.id.shadow);

        this.listHeader = this.inflater.inflate(R.layout.list_header, null, false);
        this.header = listHeader.findViewById(R.id.title);
        this.nextPage = listHeader.findViewById(R.id.nextPage);

        this.header.setText(getResources().getText(R.string.tomorrow));
        this.nextPage.setVisibility(View.INVISIBLE);

        this.content.addHeaderView(this.listHeader, null, false);
        this.content.setAdapter(this.taskListAdapter);
    }

    private void addListener(){

    }

    private List<Task> getTasks(){
        return TaskCtrl.getTasks(mContext,false);
    }
}
