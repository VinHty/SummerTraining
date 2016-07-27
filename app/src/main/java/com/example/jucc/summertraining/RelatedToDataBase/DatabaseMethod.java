package com.example.jucc.summertraining.RelatedToDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.Entity.UseTime;
import com.example.jucc.summertraining.Entity.UseTimeList;
import com.example.jucc.summertraining.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

    private DatabaseMethod(Context mContext) {
        this.mContext = mContext;
        MyDatabaseHelper helper = new MyDatabaseHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        this.db = db;
    }

    //使用单例模式，防止过多对象导致数据库对象栈溢出
    public static DatabaseMethod getInstance(Context mContext) {
        if (databaseMethod == null) {
            databaseMethod = new DatabaseMethod(mContext);
        }
        return databaseMethod;
    }


    /*
    **插入今天的实时使用时间
     */
    public void insert_nowusetime(String appName, int nowUse, int iniUse) {
        String sqlinsert;
        sqlinsert = "insert into now_usetime( app_name ,now_use_time,ini_use_time) values('" + appName + "','" + nowUse + "','" + iniUse + "')";
        db.execSQL(sqlinsert);
    }

    /*
    **将今天的使用时间插入到总的时间记录表中
    */
    public void insert_usetime(String appName, int use, String date) {
        String sqlinsert;
        sqlinsert = "insert into usetime( app_name ,use_time,use_date) values('" + appName + "','" + use + "','" + date + "')";
        db.execSQL(sqlinsert);
    }

    /*
    **更新今天的使用时间
    */
    public void update_nowusetime(String appName, int nowUse, int iniUse) {
        String sqlupdate;
        sqlupdate = "update now_usetime set now_use_time='" + nowUse + "',ini_use_time='" + iniUse + "' where app_name='" + appName + "'";
        db.execSQL(sqlupdate);
    }
    /*
    **将今天的使用时间插入到总的时间记录表中
    public void  update_usetime(String appName,int use,String date){
        String sqlupdate;
        sqlupdate="update usetime set use_time='"+use+"' where app_name='"+appName+"' and use_date='"+date+"'";
        db.execSQL(sqlupdate);
    }
    */

    /*
    **开始自定义任务时插入
    */
    public void insert_quickjob(String timeStamp, String jobName, int lastTime, String startTime) {
        String sqlinsert;
        sqlinsert = "insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time,is_alert) values('" + timeStamp + "','" + jobName + "','" + lastTime + "','" + startTime + "','NULL','NULL','0')";
        db.execSQL(sqlinsert);
    }

    /*
    **添加没有提醒任务时插入
    */
    public void insert_jobWithoutAlert(String timeStamp, String jobName, String alertTime) {
        String sqlinsert;
        sqlinsert = "insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time,is_alert) values('" + timeStamp + "','" + jobName + "','NULL','NULL','NULL','" + alertTime + "','0')";
        db.execSQL(sqlinsert);
    }

    /*
    **添加有提醒任务时插入
    */
    public void insert_jobWithAlert(String timeStamp, String jobName, String alertTime) {
        String sqlinsert;
        sqlinsert = "insert into job( set_time,job_name,last_time,start_time,is_suc,alert_time,is_alert) values('" + timeStamp + "','" + jobName + "','NULL','NULL','NULL','" + alertTime + "','1')";
        db.execSQL(sqlinsert);
    }

    /*
    **编辑任务时更新
    */
    public void update_jobWhenEdit(String timeStamp, String jobName, String alertTime, boolean isAlert) {
        String sqlupdate;
        int i = 0;
        if (isAlert == true) i = 1;
        sqlupdate = "update job set job_name='" + jobName + "',alert_time='" + alertTime + "',is_alert='" + i + "' where set_time='" + timeStamp + "'";
        db.execSQL(sqlupdate);
    }

    /*
    **开始任务时更新
    */
    public void update_jobWhenStart(String jobName, int lastTime, String startTime, String timeStamp) {
        String sqlupdate;
        sqlupdate = "update job set job_name='" + jobName + "', last_time='" + lastTime + "',start_time='" + startTime + "' where set_time='" + timeStamp + "'";
        db.execSQL(sqlupdate);
    }

    /*
    **结束任务时更新
    */
    public void update_jobWhenFinish(String timeStamp, boolean isSuc) {
        String sqlupdate;
        int i = 0;
        if (isSuc == true) i = 1;
        sqlupdate = "update job set is_suc='" + i + "' where set_time='" + timeStamp + "'";
        db.execSQL(sqlupdate);
    }

    /*
    **删除任务
     */
    public void delete_job(String timeStamp) {
        String sqldelete;
        sqldelete = "delete from job  where set_time='" + timeStamp + "'";
        db.execSQL(sqldelete);
    }

    /*
    **获取昨天用量
    */

    public UseTimeList getYesterdayList() {
        //UseTimeList包含了一个List 和一个int类型数据记录总的时间
        int i = 0;
        List<UseTime> yesterdayList = new ArrayList<UseTime>();
        Date currentTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        //将日期设置为昨天的
        Cursor cu = db.rawQuery("select app_name,use_time,use_date from usetime where use_date='" + dateString + "'", null);
        //将查询的数据放进List中，并且计算出总时间
        while (cu.moveToNext()) {
            yesterdayList.add(new UseTime(cu.getString(cu.getColumnIndex("app_name")), cu.getInt(cu.getColumnIndex("use_time"))));
            i += cu.getInt(cu.getColumnIndex("use_time"));
        }
        cu.close();
        //关闭cursor
        UseTimeList timeList = new UseTimeList(yesterdayList);
        timeList.setCount(i);
        return timeList;
    }

    /*
    **获取上周
    */

    public UseTimeList getLastWeek() {
        //UseTimeList包含了一个List 和一个int类型数据记录总的时间
        int i = 0;
        List<UseTime> lastWeekList = new ArrayList<UseTime>();
        Date currentTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //将日期设置为昨天的
        String dateString = formatter.format(currentTime);
        Date beforeTime = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        //将日期设置为7天前
        String dateString2 = formatter2.format(beforeTime);
        Cursor cu = db.rawQuery("select app_name,SUM(use_time)  from usetime where use_date<='" + dateString + "'and use_date>='" + dateString2 + "' group by app_name", null);
        //将查询的数据放进List中，并且计算出总时间
        while (cu.moveToNext()) {
            lastWeekList.add(new UseTime(cu.getString(cu.getColumnIndex("app_name")), cu.getInt(cu.getColumnIndex("SUM(use_time)"))));
            i += cu.getInt(cu.getColumnIndex("SUM(use_time)"));
        }
        cu.close();
        //关闭cursor
        UseTimeList timeList = new UseTimeList(lastWeekList);
        timeList.setCount(i);
        return timeList;
    }

    /*
    **获取未完成任务列表
    */
    public List<Job> getUnfinishJob() {

        List<Job> jobs = new ArrayList<Job>();
        Cursor cu = db.rawQuery("select set_time,job_name,alert_time from job where is_suc='NULL' order by alert_time ", null);
        while (cu.moveToNext()) {
            jobs.add(new Job(cu.getString(cu.getColumnIndex("set_time")), cu.getString(cu.getColumnIndex("job_name")), cu.getString(cu.getColumnIndex("alert_time"))));
        }
        cu.close();
        return jobs;
    }

    /*
    **获取成功/失败任务列表
    */
    public List<Job> getFinishJob(boolean isSuc) {

        List<Job> jobs = new ArrayList<Job>();
        int i = 0;
        if (isSuc == true) i = 1;
        Cursor cu = db.rawQuery("select start_time,job_name,is_suc,last_time from job where is_suc='" + i + "' order by start_time DESC ", null);
        while (cu.moveToNext()) {
            jobs.add(new Job( cu.getString(cu.getColumnIndex("job_name")), cu.getString(cu.getColumnIndex("last_time")), cu.getString(cu.getColumnIndex("start_time")),cu.getInt(cu.getColumnIndex("is_suc"))));
        }
        cu.close();
        return jobs;
    }

    /*
    **获取需要编辑的任务列表
    */
    public List<Job> getJobWhenEdit(String timeStamp) {

        List<Job> jobs = new ArrayList<Job>();
        Cursor cu = db.rawQuery("select job_name,is_alert,alert_time from job where set_time='" + timeStamp + "' ", null);
        while (cu.moveToNext()) {
            jobs.add(new Job(cu.getString(cu.getColumnIndex("job_name")), cu.getString(cu.getColumnIndex("alert_time")), cu.getInt(cu.getColumnIndex("is_alert"))));
        }
        cu.close();
        return jobs;
    }

    /*
**将当前日期转化为string，精确到天，格式为2016-07-22
*/
    public static String getStringYesterday() {
        Date currentTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /*
    **将当前日期时间转化为string，精确到分钟，格式为2016-07-22 12:15
    */
    public static String getStringTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeString = formatter.format(currentTime);
        return timeString;
    }

    /*
**将当前日期时间转化为string，精确到秒，格式为2016-07-22 12:12:12
*/
    public static String getStringSecond() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
        String timeString = formatter.format(currentTime);
        return timeString;
    }

    /*
    Vin 增加于 2016-7-23 迭代2
   /获得用户金币数量
    */
    public String getCoins() {
        Cursor cursor = db.rawQuery("select * from account", null);
        int coins = 0;
        while (cursor.moveToNext()) {
            coins = cursor.getInt(cursor.getColumnIndex("coins"));
            Log.d(getClass().getSimpleName(),"coins are "+coins);
        }
        return String.valueOf(coins);
    }


    /*
    /增加用户金币数量
    /@number 增加的数量
     */
    public void increaseCoins(int number) {
        int coins =Integer.parseInt( getCoins());
        int newNumber = number + coins;
        db.execSQL("update account set coins =" + newNumber);
    }

    /*
    /购买鱼 减少金币数量
    @fish 鱼对象
     */
    public void buyFish(Fish fish) {
        int coins =Integer.parseInt( getCoins());
        int newNumber = coins - fish.getPrice();
        db.execSQL("update account set coins =" + newNumber);
        db.execSQL("update species set available =1 where species=" + fish.getSpecies());
    }

    /* species table method
    /用于在商店显示所有现有的fish的种类和价格和资源文件id
    return 基于fish对象的list列表
     */
    public List<Fish> getAllSpecies() {
        Cursor cursor = db.rawQuery("select s.species as species,name,price,id from species as s,achievement as a where a.species=s.species and state=2", null);
        List<Fish> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int species = cursor.getInt(cursor.getColumnIndex("species"));
            int price = cursor.getInt(cursor.getColumnIndex("price"));
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Fish fish = new Fish(species, price);
            fish.setId(id);
            fish.setName(name);
            list.add(fish);
        }
        return list;
    }

    /*/
    获取用户可用的鱼的列表 包含 species,name,id,state
    return 包含鱼的的list的list对象 每一个对象是一个包含3个state的鱼的list     */
    public List< List<Fish> > userAvailableFish() {
        Cursor cursor = db.rawQuery("select s.species as species,name,id,state from species as s,achievement as a where available=1 and s.species=a.species)", null);
        List<List<Fish>> list = null;
        while (cursor.moveToNext()) {
            List<Fish> local = null;
            int species = cursor.getInt(cursor.getColumnIndex("species"));
            String name =cursor.getString(cursor.getColumnIndex("name"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int state = cursor.getInt(cursor.getColumnIndex("state"));
            Fish fish = new Fish(species);
            fish.setName(name);
            fish.setId(id);
            fish.setState(state);
            local.add(fish);
            if(state==2){
                list.add(local);
                local.clear();
            }
        }
        return list;
    }

    /*
    /更新成就次数
    @param fish对象
     */
    public void updateAchievement(Fish fish) {
        int species = fish.getSpecies();
        int state = fish.getState();
        int times = getTimes(fish);
        int newTimes = times + 1;
        db.execSQL("update achievement set times=" + newTimes + "where species=" + species + "and state = " + state);


    }

    /*/
    基于fish种类获得完成次数
    @param fish 对象
     */
    public int getTimes(Fish fish) {
        int species = fish.getSpecies();
        int state = fish.getState();
        Cursor cu = db.rawQuery("select times from achievement where species=" + species + " and state = " + state, null);
        cu.moveToNext();
        int times = cu.getInt(cu.getColumnIndex("times"));
        return times;
    }

    public List<Fish> achievementSmall(){
        String sql = "select s.species as species,name,id,times from achievement as a,species as s where s.species=a.species and state=0";
        Cursor cursor=db.rawQuery(sql,null);
        List<Fish> list = null;
        while (cursor.moveToNext()){
            int species = cursor.getInt(cursor.getColumnIndex("species"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int times = cursor.getInt(cursor.getColumnIndex("times"));
            Fish fish = new Fish(species);
            fish.setName(name);
            fish.setId(id);
            fish.setTimes(times);
            list.add(fish);
        }
        return list;
    }
    public List<Fish> achievementMid(){
        String sql = "select s.species as species,name,id,times from achievement as a,species as s where s.species=a.species and state=1";
        Cursor cursor=db.rawQuery(sql,null);
        List<Fish> list = null;
        while (cursor.moveToNext()){
            int species = cursor.getInt(cursor.getColumnIndex("species"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int times = cursor.getInt(cursor.getColumnIndex("times"));
            Fish fish = new Fish(species);
            fish.setName(name);
            fish.setId(id);
            fish.setTimes(times);
            list.add(fish);
        }
        return list;
    }
    public List<Fish> achievementBig(){
        String sql = "select s.species as species,name,id,times from achievement as a,species as s where s.species=a.species and state=2";
        Cursor cursor=db.rawQuery(sql,null);
        List<Fish> list = null;
        while (cursor.moveToNext()){
            int species = cursor.getInt(cursor.getColumnIndex("species"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int times = cursor.getInt(cursor.getColumnIndex("times"));
            Fish fish = new Fish(species);
            fish.setName(name);
            fish.setId(id);
            fish.setTimes(times);
            list.add(fish);
        }
        return list;
    }


    public void insertRecords(){


    }


}
