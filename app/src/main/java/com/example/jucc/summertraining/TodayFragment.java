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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vin on 2016/7/15.
 */
public  class TodayFragment extends MyFragment  {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =super.onCreateView(inflater, container, savedInstanceState);
        readUsageStats(getActivity());
        adapter=new SimpleAdapter(getContext(),list,R.layout.activity_app_usage_list_item,itemName,itemID);
        listView.setAdapter(adapter);
        return view;

    }

    public void update (){
        list.clear();
        readUsageStats(getActivity());
        adapter.notifyDataSetChanged();

    }
    public static boolean hasModule(Activity act) {
        PackageManager packageManager = act.getApplicationContext().getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    public static List<UsageStats> getUsageStats(Activity act){
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager)act.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,ts-200000, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }
        return queryUsageStats;
    }

    public void readUsageStats(Activity act) {
        if (hasModule(act)) {
            List<UsageStats> queryUsageStats = getUsageStats(act);
            if(queryUsageStats==null){
                Toast.makeText(getContext(),"list 为空",Toast.LENGTH_LONG).show();
            }
            else {
                long totalUseTime = 0;

                for (UsageStats usageStats : queryUsageStats) {
                    String name = usageStats.getPackageName();
                    long useTime = usageStats.getTotalTimeInForeground() / 1000 / 60;//单位为Minutes
                    HashMap<String, Object> map = new HashMap<>();
                    //Map<String, Long> componentResumeMap = (Map<String, Long>) cPkgUsageStats.getDeclaredField("componentResumeTimes").get(pkgUsageStats);
                    map.put("title", name);
                    map.put("time", useTime);
                    totalUseTime += useTime;
                    list.add(map);
                }
                for (int i = 0; i < list.size(); i++) {
                    HashMap map = list.get(i);
                    long useTime = (long) map.get("time");
                    long percent = useTime / totalUseTime;
                    map.put("percent", percent);
                }
            }
        } else {
            Toast.makeText(getContext(), "no module found", Toast.LENGTH_LONG).show();
        }
    }
}
