package com.example.jucc.summertraining.Entity;

/**
 * Created by xiaoqiang on 2016/7/19.
 */

public  class UseTime {
    //一个类表示每个应用和对应的使用时间
    private String appName;
    private int useTime;
    private String useDate;
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }



    public UseTime(String appName,int useTime){
        this.appName=appName;
        this.useTime=useTime;
    }

}
