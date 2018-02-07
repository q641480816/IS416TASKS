package com.is416.tasks.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.is416.tasks.model.Task;
import com.is416.tasks.util.TaskDBOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gods on 2/6/2018.
 */

public class TaskDao {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String table = "task";

    public static boolean createTask(Context context, Task task) {
        try {
            TaskDBOpenHelper dbOpenHelper = new TaskDBOpenHelper(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id",task.getId());
            values.put("content", task.getContent());
            values.put("created", sdf.format(task.getCreated()));
            db.insert(table,null, values);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static List<Task> getTask(Context context, boolean isToday){
        List<Task> tasks = new ArrayList<>();
        try {
            TaskDBOpenHelper dbOpenHelper = new TaskDBOpenHelper(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            Calendar c = Calendar.getInstance();
            if (!isToday){
                c.add(Calendar.DAY_OF_MONTH,1);
            }
            Cursor cursor =  db.rawQuery("SELECT * FROM task WHERE created = ?",new String[]{sdf.format(c.getTime())});
            while(cursor.moveToNext()){
                Task t = new Task(cursor.getString(cursor.getColumnIndex("id")),sdf.parse(cursor.getString(cursor.getColumnIndex("created"))),cursor.getString(cursor.getColumnIndex("content")));
                String completed = cursor.getString(cursor.getColumnIndex("completed"));
                if(completed != null){
                    Calendar ct = Calendar.getInstance();
                    ct.setTimeInMillis(Long.parseLong(completed));
                    t.setCompleted(ct.getTime());
                }
                tasks.add(t);
            }
            cursor.close();
            return tasks;
        }catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }

    public static boolean updateTask(Context context, HashMap<String, String> changes, String id){
        try{
            TaskDBOpenHelper dbOpenHelper = new TaskDBOpenHelper(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            for(String k : changes.keySet()){
                values.put(k, changes.get(k));
            }
            db.update(table,values,"id = ?", new String[]{id});
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteTask(Context context,String id) {
        try {
            TaskDBOpenHelper dbOpenHelper = new TaskDBOpenHelper(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

            db.delete(table, "id = ?", new String[] {id});
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
}
