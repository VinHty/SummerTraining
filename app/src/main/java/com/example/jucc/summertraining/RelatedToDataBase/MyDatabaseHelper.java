package com.example.jucc.summertraining.RelatedToDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jucc.summertraining.R;

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
        db.execSQL("create table   account(coins INTEGER not null default 0 PRIMARY KEY)");
        //用于储存用户的金币数量的表
        db.execSQL("create table   species(species INTEGER not null PRIMARY KEY,name Text not null, price INTEGER not null,available INTEGER default 0 not null,FOREIGN KEY(species) REFERENCES achievement (species) )");
        //用于存放鱼的种类的表 ， 存放种类和价格，是否可用（使用之前需要在商店购买）， 客户端只需要读取，不需要对这个表进行修改和删除。
        db.execSQL("create table   achievement(species INTEGER not null , state INTEGER not null,times INTEGER default 0,id INTEGER not null,PRIMARY KEY(species,state))");
        //用于存放用户的成就， 分别对应鱼的 种类，状态，次数，资源文件ID
        //存入固定数据
        db.execSQL("insert into achievement values(0,0,0,"+ R.drawable.a_00+")");
        db.execSQL("insert into achievement values(0,1,0,"+ R.drawable.a_01+")");
        db.execSQL("insert into achievement values(0,2,0,"+ R.drawable.a_02+")");
        db.execSQL("insert into achievement values(1,0,0,"+ R.drawable.b_00+")");
        db.execSQL("insert into achievement values(1,1,0,"+ R.drawable.b_01+")");
        db.execSQL("insert into achievement values(1,2,0,"+ R.drawable.b_02+")");
        db.execSQL("insert into achievement values(2,0,0,"+ R.drawable.c_00+")");
        db.execSQL("insert into achievement values(2,1,0,"+ R.drawable.c_01+")");
        db.execSQL("insert into achievement values(2,2,0,"+ R.drawable.c_02+")");
        db.execSQL("insert into achievement values(3,0,0,"+ R.drawable.d_00+")");
        db.execSQL("insert into achievement values(3,1,0,"+ R.drawable.d_01+")");
        db.execSQL("insert into achievement values(3,2,0,"+ R.drawable.d_02+")");
        db.execSQL("insert into achievement values(4,0,0,"+ R.drawable.e_00+")");
        db.execSQL("insert into achievement values(4,1,0,"+ R.drawable.e_01+")");
        db.execSQL("insert into achievement values(4,2,0,"+ R.drawable.e_02+")");
        db.execSQL("insert into achievement values(5,0,0,"+ R.drawable.f_00+")");
        db.execSQL("insert into achievement values(5,1,0,"+ R.drawable.f_01+")");
        db.execSQL("insert into achievement values(5,2,0,"+ R.drawable.f_02+")");
        db.execSQL("insert into species values(0,"+"'蓝精灵'"+",200,1)");
        db.execSQL("insert into species values(1,"+"'小黄鱼'"+",300,0)");
        db.execSQL("insert into species values(2,"+"'鲤鱼'"+",500,0)");
        db.execSQL("insert into species values(3,"+"'武昌鱼'"+",700,0)");
        db.execSQL("insert into species values(4,"+"'胖头鱼'"+",1000,0)");
        db.execSQL("insert into species values(5,"+"'蝴蝶鱼'"+",1200,0)");
        db.execSQL("insert into account values(0)");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  super.onUpgrade(db,oldVersion,newVersion);
    }
}
