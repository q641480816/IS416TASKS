package com.is416.tasks.BroadcastReceiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.is416.tasks.service.OnBootAlarmService;
import com.is416.tasks.util.AlarmSetter;
import com.is416.tasks.util.SharedPreferenceManager;

import java.util.Calendar;

public class OnBootReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent){
        try {
            if (intent.getAction().equals(ACTION_BOOT)) {
                Intent i = new Intent(context, OnBootAlarmService.class);
                context.startService(i);
            }
        }catch (NullPointerException ne){
            ne.printStackTrace();
        }
    }
}
