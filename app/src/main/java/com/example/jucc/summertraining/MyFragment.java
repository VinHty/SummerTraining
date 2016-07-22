package com.example.jucc.summertraining;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vin on 2016/7/18.
 */

public class MyFragment extends Fragment {
    //每个子类都共有的部分 数据库访问对象，存放数据的list,listView的适配器，key的集合，layout里的id的集合
    protected DatabaseMethod method;
    ArrayList<HashMap<String, Object>> list;
    protected ListView listView;
    protected SimpleAdapter adapter;
    protected static String[] itemName = {"title","time","percent"};
    protected static int[] itemID = {R.id.activity_app_usage_titel,R.id.activity_app_usage_time,R.id.activity_app_usage_percent};
    @Nullable
    @Override
    /*
    初始化View 获得基本控件 实例化对象
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_app_usage_amount,container,false);
        list= new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listView);
        Log.e(getClass().getSimpleName(), "onCreateView");
        method=DatabaseMethod.getInstance(getContext());
        return view;
    }

    /*
    /从数据库读取消息存入全局list
     */
    public void getUsageAmountFromDatabase(){};
/*
/更新数据操作 针对今日用量
 */
    public void update() {}
}
