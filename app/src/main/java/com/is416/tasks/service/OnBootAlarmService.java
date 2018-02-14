package com.is416.tasks.service;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.is416.tasks.util.AlarmSetter;
import com.is416.tasks.util.SharedPreferenceManager;

import java.util.Calendar;

public class OnBootAlarmService extends Service {
    private Context mContext;

    public OnBootAlarmService() {
    }

    public void onCreate() {
        mContext = this;
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String time = SharedPreferenceManager.get("reminder", this);
        if (!time.equals(SharedPreferenceManager.nullable)){
            String[] timeSet = time.split(":");
            Calendar now = Calendar.getInstance();
            Calendar expected = Calendar.getInstance();
            expected.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeSet[0]));
            expected.set(Calendar.MINUTE, Integer.parseInt(timeSet[1]));
            expected.set(Calendar.SECOND, 0);
            if (!expected.after(now)){
                expected.add(Calendar.DAY_OF_MONTH, 1);
            }
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            AlarmSetter.setAlarm(expected, manager, mContext);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
