package com.example.jucc.summertraining.Entity;

/**
 * Created by Vin on 2016/7/14.
 */

public class Job {
    private String title;
    private String lastTime;
    private String beginTime;
    private boolean success;
    private String remindTime;

    public Job(String title, String lastTime, String beginTime, boolean success, String remindTime) {
        this.title = title;
        this.lastTime = lastTime;
        this.beginTime = beginTime;
        this.success = success;
        this.remindTime = remindTime;
    }

    public Job(String title) {
        this.title = title;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }


}
