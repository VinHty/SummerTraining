package com.example.jucc.summertraining;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.example.jucc.summertraining.Entity.UseTime;

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
        List<UseTime> list = method.getYesterdayList();
        for(int a=0;a<list.size();a++){
            UseTime useTime= list.get(a);

        }
    }
}
