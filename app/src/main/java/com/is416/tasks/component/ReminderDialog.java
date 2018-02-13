package com.is416.tasks.component;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.is416.tasks.R;
import com.is416.tasks.TasksActivity;
import com.is416.tasks.util.ActivityManager;
import com.is416.tasks.util.SharedPreferenceManager;

/**
 * Created by Gods on 2/13/2018.
 */

public class ReminderDialog extends Dialog{

    private Context mContext;
    private String master;
    private LinearLayout edit_reminder;
    private TextView reminder_content;
    private Button delete;
    private Button save;
    private Button cancel;

    public ReminderDialog(@NonNull Context context, String master) {
        super(context);

        this.mContext = context;
        this.master = master;
    }

    public ReminderDialog(@NonNull Context context, int themeResId, String master) {
        super(context, themeResId);

        this.mContext = context;
        this.master = master;
    }

    protected ReminderDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, String master) {
        super(context, cancelable, cancelListener);

        this.mContext = context;
        this.master = master;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_panel);
        setCanceledOnTouchOutside(false);


        init();
        bindView();
        addListener();
    }

    private void init(){

    }

    private void bindView(){
        this.edit_reminder = findViewById(R.id.edit_reminder);
        this.reminder_content = findViewById(R.id.reminder_content);
        this.cancel = findViewById(R.id.reminder_cancel);
        this.delete = findViewById(R.id.reminder_delete);
        this.save = findViewById(R.id.reminder_save);

        String reminder = SharedPreferenceManager.get("reminder", mContext);
        this.reminder_content.setText(reminder.equals(SharedPreferenceManager.nullable) ? mContext.getResources().getText(R.string.reminder_not_set) : reminder);
    }

    private void addListener(){
        this.cancel.setOnClickListener((v)->{
            dismiss();
        });

        this.delete.setOnClickListener((v)->{
            SharedPreferenceManager.remove("reminder",mContext);
            ((TasksActivity)ActivityManager.getActivity(master)).updateReminder(false, "");
            dismiss();
        });

        this.save.setOnClickListener((v)->{
            SharedPreferenceManager.save("reminder",this.reminder_content.getText().toString(),mContext);
            ((TasksActivity)ActivityManager.getActivity(master)).updateReminder(true, this.reminder_content.getText().toString());
            dismiss();
        });

        this.edit_reminder.setOnClickListener((v)->{
            TimePickerDialog timeDialog = new TimePickerDialog(mContext,(view, hourOfDay, minute) -> {
                String min = minute < 10 ? "0" + minute : minute + "";
                String h = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                this.reminder_content.setText(h + ":" + min);
            }, 0, 0, true);
            timeDialog.show();
        });
    }
}
