package com.example.jucc.summertraining.RelatedToDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vin on 2016/7/14.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {


    public  MyDatabaseHelper(Context context) {
        super(context, "TimerManagerDataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table   usetime( app_name TEXT not null,use_time INTEGER,use_date TEXT not null,PRIMARY KEY(app_name,use_date))");
        db.execSQL("create table   now_usetime( app_name TEXT not null PRIMARY KEY,now_use_time INTEGER,ini_use_time INTEGER not null)");
        db.execSQL("create table   job( set_time TEXT NOT NULL PRIMARY KEY,job_name TEXT not null ,last_time INTEGER,start_time TEXT ,is_suc INTEGER ,alert_time TEXT,is_alert INTEGER)");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
