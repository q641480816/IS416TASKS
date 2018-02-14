package com.is416.tasks.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.is416.tasks.BroadcastReceiver.ReminderReceiver;

import java.util.Calendar;

/**
 * Created by Gods on 2/14/2018.
 */

public class AlarmSetter {

    public static void setAlarm(Calendar expected, AlarmManager manager, Context context){
        Intent i = new Intent(context, ReminderReceiver.class);
        i.setData(Uri.parse("content://tasks/tasks_alerts/1"));
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        if (pi != null){
            manager.cancel(sender);
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(expected.getTimeInMillis(), sender);
                manager.setAlarmClock(alarmClockInfo, sender);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.RTC_WAKEUP, expected.getTimeInMillis(), sender);
            } else {
                manager.set(AlarmManager.RTC_WAKEUP, expected.getTimeInMillis(), sender);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
