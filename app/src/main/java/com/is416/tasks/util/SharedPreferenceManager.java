package com.is416.tasks.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gods on 2/10/2018.
 */

public class SharedPreferenceManager {

    private static final String name = "TasksIS416";
    public static final String nullable = "null";

    public static void save(String key, String value, Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String get(String key, Context mContext){
        String out;
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        out = sp.getString(key, nullable);
        return out;
    }

    public static void remove(String key, Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }
}
