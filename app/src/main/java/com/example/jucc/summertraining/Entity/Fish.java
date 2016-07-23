package com.example.jucc.summertraining.Entity;

/**
 * Created by Vin on 2016/7/23.
 */

public class Fish {
    private int species;
    //鱼的种类 0-9分别代表不同的鱼
    //0 - xxx 1- xxx 2-xxx 3-xxx 4-xxx 5-xxx 6-xxx 7-xxx 8-xxx 9-xxx
    private int price;
    //每种鱼对应的价格
    private int state;
    //鱼所处的状态 0-2 分别代表三种状态 小 中 大
    private int times;
    //该种状态的鱼被完成的次数，UI端不需要传入该参数

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getSpecies() {
        return species;
    }

    public void setSpecies(int species) {
        this.species = species;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Fish(int species, int price) {
        this.species = species;
        this.price = price;
    }

    //此函数只在数据库层使用 注意
    public Fish(int species, int state, int times) {
        this.species = species;
        this.state = state;
        this.times = times;
    }

    //success只是用于区别于另一个构造函数，没有实际作用，值可以任意传0或1。
    //用于完成计时时创建对象
    public Fish(int species,int state,boolean success){
        this.species=species;
        this.state=state;
    }
}
