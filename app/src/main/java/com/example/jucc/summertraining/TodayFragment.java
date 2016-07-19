package com.example.jucc.summertraining;


import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vin on 2016/7/15.
 */
public  class TodayFragment extends MyFragment  {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =super.onCreateView(inflater, container, savedInstanceState);
        try {
           getUsageAmountFromSDK();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        adapter=new SimpleAdapter(getContext(),list,R.layout.activity_app_usage_list_item,itemName,itemID);
        listView.setAdapter(adapter);
        return view;

    }
    public void getUsageAmountFromSDK() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        //相当于：IBinder oRemoteService = ServiceManager.getService("usagestats");
        Class<?> cServiceManager = Class.forName("android.os.ServiceManager");
        Method mGetService = cServiceManager.getMethod("getService", java.lang.String.class);
        Object oRemoteService = mGetService.invoke(null, "usagestats");

        // 相当于:IUsageStats mUsageStatsService = IUsageStats.Stub.asInterface(oRemoteService)
        Class<?> cStub = Class.forName("com.android.internal.app.IUsageStats$Stub");
        Method mUsageStatsService = cStub.getMethod("asInterface", android.os.IBinder.class);
        Object oIUsageStats = mUsageStatsService.invoke(null, oRemoteService);

        // 相当于:PkgUsageStats[] oPkgUsageStatsArray =mUsageStatsService.getAllPkgUsageStats();
        Class<?> cIUsageStatus = Class.forName("com.android.internal.app.IUsageStats");
        Method mGetAllPkgUsageStats = cIUsageStatus.getMethod("getAllPkgUsageStats", (Class[]) null);
        Object[] oPkgUsageStatsArray = (Object[]) mGetAllPkgUsageStats.invoke(oIUsageStats, (Object[]) null);

        //相当于
        //for (PkgUsageStats pkgUsageStats: oPkgUsageStatsArray)
        //{
        //  当前APP的包名：
        //  packageName = pkgUsageStats.packageName
        //  当前APP的启动次数
        //  launchCount = pkgUsageStats.launchCount
        //  当前APP的累计使用时间：
        //  usageTime = pkgUsageStats.usageTime
        //  当前APP的每个Activity的最后启动时间
        //  componentResumeTimes = pkgUsageStats.componentResumeTimes
        //}
        Class<?> cPkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");
        long totalUseTime=0;
        for (Object pkgUsageStats : oPkgUsageStatsArray) {
            String packageName = (String) cPkgUsageStats.getDeclaredField("packageName")  .get(pkgUsageStats);
          //  int launchCount = cPkgUsageStats.getDeclaredField("launchCount").getInt(pkgUsageStats);
            long usageTime = cPkgUsageStats.getDeclaredField("usageTime").getLong(pkgUsageStats);
             HashMap<String,Object> map = new HashMap<>();
            //Map<String, Long> componentResumeMap = (Map<String, Long>) cPkgUsageStats.getDeclaredField("componentResumeTimes").get(pkgUsageStats);
            map.put("title",packageName);
            map.put("time",usageTime);
            totalUseTime+=usageTime;
            list.add(map);
        }
        for(int i=0;i<list.size();i++){
            HashMap map = list.get(i);
            long useTime= (long) map.get("time");
            long percent = useTime/totalUseTime;
            map.put("percent",percent);
        }


    }
    public void update (){
        list.clear();
        try {
            getUsageAmountFromSDK();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }
}
