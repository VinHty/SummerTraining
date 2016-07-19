package com.example.jucc.summertraining.Entity;

/**
 * Created by xiaoqiang on 2016/7/19.
 */

public  class UseTime {
    private String appName;
    private int useTime;
    private String useDate;

    public UseTime(String appName,int useTime,String useDate){
        this.appName=appName;
        this.useTime=useTime;
        this.useDate=useDate;
    }
}
