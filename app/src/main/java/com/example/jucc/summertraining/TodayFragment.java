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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Vin on 2016/7/15.
 */
public class TodayFragment extends MyFragment {

    /*/
    初始化view 读取app使用量 绑定适配器
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;

    }


    /**
     * 5.0以上系统获取应用使用情况 若支持则返回所有应用记录的数据，若无则返回null
     *
     * @return
     */
    public static List<UsageStats> getUsageStats(Activity act) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) act.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, ts - 200000, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }
        return queryUsageStats;
    }
    /*
    /在activity获取到了权限之后，再调用fragment onStart方法，填充数据
     */

    @Override
    public void onStart() {
        super.onStart();
        readFromList(getActivity());
        adapter = new SimpleAdapter(getContext(), list, R.layout.activity_app_usage_list_item, itemName, itemID);
        Log.d(getClass().getSimpleName(), list.toString());
        listView.setAdapter(adapter);
        ((AppUsageAmountActivity)getActivity()).insertUsage();
    }

    /*
        /读取数据 并且存入list中
         */
    public void readFromList(Activity act) {
        if (getUsageStats(act) == null) {
        } else {
            List<UsageStats> queryUsageStats = getUsageStats(act);
            PackageManager pm = getContext().getPackageManager();
            long totalTime = 0;
            for (UsageStats usageStats : queryUsageStats) {
                String appName = usageStats.getPackageName();
                long useTime = usageStats.getTotalTimeInForeground() / 1000 / 60;
                String name = null;
                try {
                    name = pm.getApplicationLabel(pm.getApplicationInfo(appName, PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                HashMap<String, Object> map = new HashMap<>();
                totalTime += useTime;
                map.put("title", name);
                map.put("time", useTime);
                this.list.add(map);
            }
            Log.e(getClass().getSimpleName(), "list size is " + list.size());
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);
                long time = (long) map.get("time");
                if (totalTime == 0) {
                    map.put("percent", 0);
                } else {
                    long percent = time * 100 / totalTime;
                    map.put("percent", percent);
                }

            }
        }


    }

    /*
    /更新数据 每次点击tab 删除现有的list里面的数据 然后重新获取消息装填
     */
    public void update() {
        list.clear();
        Log.d(getClass().getSimpleName(), list.toString());
        readFromList(getActivity());
        Log.d(getClass().getSimpleName(), list.get(9).toString());
        Log.e(getClass().getSimpleName(), "list size is " + list.size());
        adapter.notifyDataSetChanged();
    }
}
