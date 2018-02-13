package com.is416.tasks.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gods on 2/6/2018.
 */

public class TaskDBManager extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String db = "task.db";

    public TaskDBManager(Context context) {
        super(context, db, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE task(\n" +
                "\tid VARCHAR PRIMARY KEY,\n" +
                "\tcontent text(10000),\n" +
                "    created date not null,\n" +
                "    completed VARCHAR\n" +
                ")";

        db.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO:
    }
}
