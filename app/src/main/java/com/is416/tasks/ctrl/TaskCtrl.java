package com.is416.tasks.ctrl;

import android.content.Context;
import android.os.Build;

import com.is416.tasks.dao.TaskDao;
import com.is416.tasks.model.Task;
import com.is416.tasks.util.LockFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gods on 2/7/2018.
 */

public class TaskCtrl {

    public static boolean createTask(Context context, Task task){
        return TaskDao.createTask(context,task);
    }

    public static List<Task> getTasks(Context context, boolean isToday){
        List<Task> tasks = new ArrayList<>();
        List<Task> checked = new ArrayList<>();
        List<Task> unchecked = new ArrayList<>();
        List<Task> temp = TaskDao.getTask(context, isToday);

        for(Task t: temp){
            if (t.isComplete()){
                checked.add(t);
            }else {
                unchecked.add(t);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tasks.addAll(unchecked.stream()
                    .parallel()
                    .sorted((s1,s2) -> {
                        if(Long.parseLong(s1.getId()) < Long.parseLong(s2.getId())){
                            return -1;
                        }else if (Long.parseLong(s1.getId()) == Long.parseLong(s2.getId())){
                            return 0;
                        }else{
                            return 1;
                        }
                    })
                    .collect(Collectors.toList())
            );
            tasks.add(new Task("-1", null, "test 1"));
            tasks.addAll(checked.stream()
                    .parallel()
                    .sorted((s1,s2) -> {
                        if(s1.getCompleted().before(s2.getCompleted())){
                            return -1;
                        }else if (s1.getCompleted().equals(s2.getCompleted())){
                            return 0;
                        }else{
                            return 1;
                        }
                    })
                    .collect(Collectors.toList()));
        }else{
            tasks.addAll(unchecked);
            tasks.add(new Task("-1", null, "test 1"));
            tasks.addAll(checked);
        }
        return tasks;
    }

    public static Task markComplete(Context context, Task task, boolean isToComplete){
        HashMap<String, String> changes = new HashMap<>();
        task.setCompleted(isToComplete ? new Date() : null);
        changes.put("completed", isToComplete ? task.getCompleted().getTime() + "" : null);
        if (TaskDao.updateTask(context,changes,task.getId())){
            return task;
        }else {
            return null;
        }
    }

    public static boolean updateContent(Context context, Task task, String content){
        HashMap<String, String> changes = new HashMap<>();
        changes.put("content", content);
        LockFactory.getReadWriteLock("updateContent" + task.getId()).writeLock().lock();
        boolean isOk = TaskDao.updateTask(context,changes,task.getId());
        LockFactory.getReadWriteLock("updateContent" + task.getId()).writeLock().unlock();
        return isOk;
    }

    public static boolean deleteTask(Context context, String id){
        return TaskDao.deleteTask(context,id);
    }
}
