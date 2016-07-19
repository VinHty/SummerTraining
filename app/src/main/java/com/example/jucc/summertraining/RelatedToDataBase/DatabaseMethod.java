package com.example.jucc.summertraining.RelatedToDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.example.jucc.summertraining.Entity.UseTime;
import com.example.jucc.summertraining.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Vin on 2016/7/14.
 */

public class DatabaseMethod {


    private Context mContext;
    private MyDatabaseHelper helper ;
    private SQLiteDatabase db;

    public DatabaseMethod(Context mContext){
        this.mContext=mContext;
        MyDatabaseHelper helper=new MyDatabaseHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        this.db= db;
    }



    /*
    **插入今天的实时使用时间1
     */
    public void insert_nowusetime(String appName,int nowUse,int iniUse){
        String sqlinsert;
        sqlinsert="insert into now_usetime( app_name ,now_use_time,ini_use_time) values('"+appName+"','"+nowUse+"','"+iniUse+"')";
//        db.execSQL(sqlinsert);
    }
    /*
    **将今天的使用时间插入到总的时间记录表中
    */
    public void insert_usetime( String appName,int use,String date){
        String sqlinsert;
        sqlinsert="insert into usetime( app_name ,use_time,use_date) values('"+appName+"','"+use+"','"+date+"')";
        db.execSQL(sqlinsert);
    }
    /*
    **更新今天的使用时间
    */
    public void  update_nowusetime(String appName, int nowUse, int iniUse){
        String sqlupdate;
        sqlupdate="update now_usetime set now_use_time='"+nowUse+"',ini_use_time='"+iniUse+"' where app_name='"+appName+"'";
        db.execSQL(sqlupdate);
    }
    /*
    **将今天的使用时间插入到总的时间记录表中
        突然发现这个函数没用……尴尬
    public void  update_usetime(String appName,int use,String date){
        String sqlupdate;
        sqlupdate="update usetime set use_time='"+use+"' where app_name='"+appName+"' and use_date='"+date+"'";
        db.execSQL(sqlupdate);
    }
    */

            /*
    **开始自定义任务时插入
    */
    public void insert_quickjob(String timeStamp,String jobName,int lastTime,String startTime){
        String sqlinsert;
        sqlinsert="insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time) values('"+timeStamp+"','"+jobName+"','"+lastTime+"','"+startTime+"','NULL','NULL')";
        db.execSQL(sqlinsert);
    }
    /*
    **添加没有提醒任务时插入
    */
    public void insert_jobWithoutAlert(String timeStamp,String jobName){
        String sqlinsert;
        sqlinsert="insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time) values('"+timeStamp+"','"+jobName+"','NULL','NULL','NULL','NULL')";
        db.execSQL(sqlinsert);
    }

    /*
    **添加有提醒任务时插入
    */
    public void insert_jobWithAlert(String timeStamp,String jobName,String alertTime){
        String sqlinsert;
        sqlinsert="insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time) values('"+timeStamp+"','"+jobName+"','NULL','NULL','NULL','"+alertTime+"')";
        db.execSQL(sqlinsert);
    }

    /*
    **编辑任务时更新
    */
    public void update_jobWhenEdit(String timeStamp,String jobName,String alertTime){
        String sqlupdate;
        sqlupdate="update job set job_name='"+jobName+"',alert_time='"+alertTime+"' where set_time='"+timeStamp+"'";
        db.execSQL(sqlupdate);
    }
    /*
    **开始任务时更新
    */
    public void update_jobWhenStart(String jobName,int lastTime,String startTime){
        String sqlupdate;
        sqlupdate="update job set last_time='"+lastTime+"',start_time='"+startTime+"' where job_name='"+jobName+"'";
        db.execSQL(sqlupdate);
    }
    /*
    **结束任务时更新
    */
    public void update_jobWhenFinish(String timeStamp,boolean isSuc){
        String sqlupdate;
        int i=0;
        if(isSuc==true) i=1;
        sqlupdate="update job set is_suc='"+i+"' where set_time='"+timeStamp+"'";
        db.execSQL(sqlupdate);
    }
    /*
    **删除任务
     */
    public void delete_job(String timeStamp){
        String sqldelete;
        sqldelete="delete from job  where set_time='"+timeStamp+"'";
        db.execSQL(sqldelete);
    }

    /*
    **获取昨天用量
    */

    public List<UseTime> getYesterdayList(){

        List<UseTime> yesterdayList=new ArrayList<UseTime>();
        db=helper.getReadableDatabase();
        Date currentTime = new Date(System.currentTimeMillis()-24*60*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        Cursor cu=db.rawQuery("select app_name,use_time,use_date,SUM(use_time) as total from usetime where use_date='"+dateString+"' group by app_name,use_date",null);
        while (cu.moveToNext()){
        yesterdayList.add(new UseTime(cu.getString(cu.getColumnIndex("app_name")),cu.getInt(cu.getColumnIndex("use_time")),cu.getInt(cu.getColumnIndex("total"))));
        }

        return yesterdayList;
    }

    /*
    **获取上周
    */

    public Cursor getLastWeek(){

        db=helper.getReadableDatabase();
        Date currentTime = new Date(System.currentTimeMillis()-24*60*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        Date beforeTime = new Date(System.currentTimeMillis()-7*24*60*60*1000);
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString2 = formatter.format(beforeTime);
        Cursor cu=db.rawQuery("select _id,app_name,use_time from usetime where use_date<='"+dateString+"'and use_time>='"+dateString2+"'",null);
        return cu;
    }
        /*
    **将当前日期转化为string，精确到天
    */
    public static String getStringYesterday() {
        Date currentTime = new Date(System.currentTimeMillis()-24*60*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /*
    **将当前日期时间转化为string，精确到分钟
    */
    public static String getStringTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeString = formatter.format(currentTime);
        return timeString;
    }
}
