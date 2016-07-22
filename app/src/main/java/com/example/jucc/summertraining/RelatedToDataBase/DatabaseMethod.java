package com.example.jucc.summertraining.RelatedToDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.Entity.UseTime;
import com.example.jucc.summertraining.Entity.UseTimeList;
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

    private static DatabaseMethod databaseMethod;
    private Context mContext;
    private SQLiteDatabase db;

    private DatabaseMethod(Context mContext){
        this.mContext=mContext;
        MyDatabaseHelper helper=new MyDatabaseHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        this.db= db;
    }

    //使用单例模式，防止过多对象导致数据库对象栈溢出
    public static DatabaseMethod getInstance(Context mContext){
        if(databaseMethod==null) {
            databaseMethod = new DatabaseMethod(mContext);
        }
            return databaseMethod;
        }



    /*
    **插入今天的实时使用时间
     */
    public void insert_nowusetime(String appName,int nowUse,int iniUse){
        String sqlinsert;
        sqlinsert="insert into now_usetime( app_name ,now_use_time,ini_use_time) values('"+appName+"','"+nowUse+"','"+iniUse+"')";
        db.execSQL(sqlinsert);
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
        sqlinsert="insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time,is_alert) values('"+timeStamp+"','"+jobName+"','"+lastTime+"','"+startTime+"','NULL','NULL','0')";
        db.execSQL(sqlinsert);
    }
    /*
    **添加没有提醒任务时插入
    */
    public void insert_jobWithoutAlert(String timeStamp,String jobName,String alertTime){
        String sqlinsert;
        sqlinsert="insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time,is_alert) values('"+timeStamp+"','"+jobName+"','NULL','NULL','NULL','"+alertTime+"','0')";
        db.execSQL(sqlinsert);
    }

    /*
    **添加有提醒任务时插入
    */
    public void insert_jobWithAlert(String timeStamp,String jobName,String alertTime){
        String sqlinsert;
        sqlinsert="insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time,is_alert) values('"+timeStamp+"','"+jobName+"','NULL','NULL','NULL','"+alertTime+"','1')";
        db.execSQL(sqlinsert);
    }

    /*
    **编辑任务时更新
    */
    public void update_jobWhenEdit(String timeStamp,String jobName,String alertTime,boolean isAlert){
        String sqlupdate;
        int i=0;
        if(isAlert==true) i=1;
        sqlupdate="update job set job_name='"+jobName+"',alert_time='"+alertTime+"',is_alert='"+i+"' where set_time='"+timeStamp+"'";
        db.execSQL(sqlupdate);
    }
    /*
    **开始任务时更新
    */
    public void update_jobWhenStart(String jobName,int lastTime,String startTime,String timeStamp){
        String sqlupdate;
        sqlupdate="update job set job_name='"+jobName+"', last_time='"+lastTime+"',start_time='"+startTime+"' where set_time='"+timeStamp+"'";
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

    public UseTimeList getYesterdayList(){
        //UestimeList包含了一个List 和一个int类型数据记录总的时间
        int i=0;
        List<UseTime> yesterdayList=new ArrayList<UseTime>();
        Date currentTime = new Date(System.currentTimeMillis()-24*60*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        //将日期设置为昨天的
        Cursor cu=db.rawQuery("select app_name,use_time,use_date from usetime where use_date='"+dateString+"'",null);
        //将查询的数据放进List中，并且计算出总时间
        while (cu.moveToNext()){
            yesterdayList.add(new UseTime(cu.getString(cu.getColumnIndex("app_name")),cu.getInt(cu.getColumnIndex("use_time"))));
            i+=cu.getInt(cu.getColumnIndex("use_time"));
        }
        cu.close();
        //关闭
        UseTimeList timeList = new UseTimeList(yesterdayList);
        timeList.setCount(i);
        return timeList;
    }

    /*
    **获取上周
    */

    public UseTimeList getLastWeek(){
        int i=0;
        List<UseTime> lastWeekList=new ArrayList<UseTime>();
        Date currentTime = new Date(System.currentTimeMillis()-24*60*60*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        Date beforeTime = new Date(System.currentTimeMillis()-7*24*60*60*1000);
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString2 = formatter2.format(beforeTime);
        Cursor cu=db.rawQuery("select app_name,SUM(use_time)  from usetime where use_date<='"+dateString+"'and use_date>='"+dateString2+"' group by app_name",null);
        while (cu.moveToNext()){
            lastWeekList.add(new UseTime(cu.getString(cu.getColumnIndex("app_name")),cu.getInt(cu.getColumnIndex("SUM(use_time)"))));
            i+=cu.getInt(cu.getColumnIndex("SUM(use_time)"));
        }
        cu.close();
        UseTimeList timeList = new UseTimeList(lastWeekList);
        timeList.setCount(i);
        return timeList;
    }

    /*
    **获取未完成任务列表
    */
    public List<Job> getUnfinishJob(){

        List<Job> jobs=new ArrayList<Job>();
        Cursor cu=db.rawQuery("select set_time,job_name,alert_time from job where is_suc='NULL' order by alert_time ",null);
        while (cu.moveToNext()){
            jobs.add(new Job(cu.getString(cu.getColumnIndex("set_time")),cu.getString(cu.getColumnIndex("job_name")),cu.getString(cu.getColumnIndex("alert_time"))));
        }
        cu.close();
        return jobs;
    }

    /*
    **获取成功/失败任务列表
    */
    public List<Job> getFinishJob(boolean isSuc){

        List<Job> jobs=new ArrayList<Job>();
        int i=0;
        if(isSuc==true) i=1;
        Cursor cu=db.rawQuery("select start_time,job_name,is_suc,last_time from job where is_suc='"+i+"' order by start_time DESC ",null);
        while (cu.moveToNext()){
            jobs.add(new Job(cu.getString(cu.getColumnIndex("start_time")),cu.getString(cu.getColumnIndex("job_name")),cu.getString(cu.getColumnIndex("last_time")),cu.getInt(cu.getColumnIndex("is_suc"))));
        }
        cu.close();
        return jobs;
    }

    /*
    **获取需要编辑的任务列表
    */
    public List<Job> getJobWhenEdit(String timeStamp){

        List<Job> jobs=new ArrayList<Job>();
        Cursor cu=db.rawQuery("select job_name,is_alert,alert_time from job where set_time='"+timeStamp+"' ",null);
        while (cu.moveToNext()){
            jobs.add(new Job(cu.getString(cu.getColumnIndex("job_name")),cu.getString(cu.getColumnIndex("alert_time")),cu.getInt(cu.getColumnIndex("is_alert"))));
        }
        cu.close();
        return jobs;
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
    /*
**将当前日期时间转化为string，精确到苗
*/
    public static String getStringSecond() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
        String timeString = formatter.format(currentTime);
        return timeString;
    }
}
