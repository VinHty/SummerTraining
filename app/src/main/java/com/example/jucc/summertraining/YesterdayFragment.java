package com.example.jucc.summertraining;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.jucc.summertraining.Entity.UseTime;
import com.example.jucc.summertraining.Entity.UseTimeList;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vin on 2016/7/18.
 */

public class YesterdayFragment extends MyFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= super.onCreateView(inflater, container, savedInstanceState);
        this.getUsageAmountFromDatabase();
        adapter=new SimpleAdapter(getContext(),list,R.layout.activity_app_usage_list_item,itemName,itemID);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void getUsageAmountFromDatabase() {

        UseTimeList useList = method.getYesterdayList();
        List list = useList.getList();
        long totalTime=useList.getCount();
        Toast.makeText(getContext(),"total time is "+totalTime,Toast.LENGTH_SHORT).show();
        for(int a=0;a<list.size();a++){
            UseTime useTime= (UseTime) list.get(a);
            HashMap<String,Object> map = new HashMap<>();
            String name =useTime.getAppName();
            long time = useTime.getUseTime();
            Toast.makeText(getContext(),"each app time is "+time,Toast.LENGTH_SHORT).show();
            long percent = time*100/totalTime;
            Toast.makeText(getContext(),"each app percent is "+percent,Toast.LENGTH_SHORT).show();
            map.put("title",name);
            map.put("time",time);
            map.put("percent",percent);
            this.list.add(map);
        }
    }
}
