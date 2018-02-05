package com.is416.tasks.util;

import android.app.Activity;

import java.util.HashMap;

/**
 * Created by Gods on 2/4/2018.
 */

public class ActivityManager {

    private static HashMap<String, Activity> activities;

    public static void addActivity(String name,Activity activity){
        if(activities == null){
            activities = new HashMap<>();
        }
        activities.put(name, activity);
    }

    public static Activity getActivity(String name){
        return activities.get(name);
    }

    public static void finishActivity(String name){
        activities.remove(name);
    }

    private static void finishAll(){
        for(Activity activity: activities.values()){
            activity.finish();
        }
    }
}
