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
        //创建一个表存储过去的各个应用的用量，分别为应用名称，使用时间以及对应的日期
        db.execSQL("create table   now_usetime( app_name TEXT not null PRIMARY KEY,now_use_time INTEGER,ini_use_time INTEGER not null)");
        //创建一个表存储今天的使用用量，分别为应用名称，当前的使用总量，今天刚开始的使用总量
        db.execSQL("create table   job( set_time TEXT NOT NULL PRIMARY KEY,job_name TEXT not null ,last_time INTEGER,start_time TEXT ,is_suc INTEGER ,alert_time TEXT,is_alert INTEGER)");
        //创建一个表存储任务，分别为添加任务的时间，任务的名称，任务持续时间，开始时间，是否成功，提醒时间，是否提醒1
        db.execSQL("create table   account(coins INTEGER default 0 PRIMARY KEY)");
        //用于储存用户的金币数量的表
        db.execSQL("create table   species(species INTEGER not null PRIMARY KEY, price INTEGER not null,FOREIGN KEY(species) REFERENCES achievement (species) )");
        //用于存放鱼的种类的表 ， 存放种类和价格， 客户端只需要读取，不需要对这个表进行修改和删除。
        db.execSQL("create table   achievement(species INTEGER not null , state INTEGER not null,times INTEGER default 0,available INTEGER default 0),PRIMARY KEY(species,state)");
        //用于存放用户的成就， 分别对应鱼的 种类，状态，次数，是否可用（使用之前需要在商店购买）

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
