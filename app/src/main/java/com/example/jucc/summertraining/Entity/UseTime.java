package com.example.jucc.summertraining.Entity;

/**
 * Created by xiaoqiang on 2016/7/19.
 */

public  class UseTime {
    private String appName;
    private int useTime;
    private String useDate;
    private int total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public UseTime(String appName,int useTime,int total){
        this.appName=appName;
        this.useTime=useTime;
        this.total=total;

    }

}
