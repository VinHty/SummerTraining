package com.example.jucc.summertraining.Entity;

import java.util.List;

/**
 * Created by Vin on 2016/7/19.
 */

public class UseTimeList {
    public UseTimeList(List<UseTime> list) {
        this.list = list;
    }

    public List<UseTime> getList() {
        return list;
    }

    public void setList(List<UseTime> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private List<UseTime> list;
    private int count;
}
