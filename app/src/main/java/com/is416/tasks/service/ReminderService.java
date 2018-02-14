package com.is416.tasks.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.is416.tasks.BroadcastReceiver.ReminderReceiver;
import com.is416.tasks.R;
import com.is416.tasks.TasksActivity;
import com.is416.tasks.ctrl.TaskCtrl;
import com.is416.tasks.model.Task;
import com.is416.tasks.util.AlarmSetter;
import com.is416.tasks.util.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderService extends Service {
    private Context mContext;
    private NotificationManager notificationManager;
    private Calendar expected;

    public ReminderService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        mContext = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(Intent intent, int flags, int startId) {
        String temp = SharedPreferenceManager.get("reminder", mContext);
        if (!temp.equals(SharedPreferenceManager.nullable)){
            String[] timeSet = temp.split(":");
            Calendar now = Calendar.getInstance();
            this.expected = Calendar.getInstance();
            now.set(Calendar.SECOND,0);
            now.set(Calendar.MILLISECOND, 0);
            expected.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeSet[0]));
            expected.set(Calendar.MINUTE, Integer.parseInt(timeSet[1]));
            expected.set(Calendar.SECOND, 0);
            expected.set(Calendar.MILLISECOND, 0);

            if (now.equals(expected)){
                notify_task();
                setReminder();
            }
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void notify_task(){
        List<Task> unchecked = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unchecked =  TaskCtrl.getTasks(mContext, true).stream()
                    .parallel()
                    .filter(t -> t.getCreated() != null && !t.isComplete())
                    .collect(Collectors.toList());
        }else{
            List<Task> temp = TaskCtrl.getTasks(mContext, true);
            for (Task t : temp){
                if(t.getCreated() != null && !t.isComplete()){
                    unchecked.add(t);
                }
            }
        }

        Intent it = new Intent(mContext, TasksActivity.class);
        PendingIntent pit = PendingIntent.getActivity(mContext, 0, it, 0);

        Notification.Builder mBuilder = new Notification.Builder(this);
        Bitmap largeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        mBuilder.setContentTitle("Tasks")
                .setContentText("You have " + unchecked.size() + " task(s) need to complete.")
                .setSubText(unchecked.size() == 0 ? "Good let's plan tomorrow's task!" : "Remember to finish them oh!")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(largeBitmap)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setContentIntent(pit);
        Notification notification = mBuilder.build();
        notificationManager.notify(TasksActivity.NOTIFICATION_ID, notification);
    }

    private void setReminder(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        this.expected.add(Calendar.DAY_OF_MONTH, 1);
        AlarmSetter.setAlarm(expected, manager, mContext);
    }
}
