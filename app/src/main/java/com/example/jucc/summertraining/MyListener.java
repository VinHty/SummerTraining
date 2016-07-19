package com.example.jucc.summertraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

/**
 * Created by Vin on 2016/7/18.
 */
public class MyListener {
    private Context mContext;
    private MyReceiver mReceiver;
    private afterReceive afterReceive;

    public MyListener(Context context) {
        mContext = context;
        mReceiver = new MyReceiver();
    }

    /**
     *
     */
    public class MyReceiver extends BroadcastReceiver {
        private final  String actionName = "android.intent.action.DATE_CHANGED";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action==actionName){
                afterReceive.insertIntoDB();
            }
        }
    }

    /**
     * 开始监听date 变化
     *
     * @param listener
     */
    public void begin(afterReceive listener) {
        afterReceive = listener;
        registerListener();


    }
    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.DATE_CHANGED");
        mContext.registerReceiver(mReceiver, filter);
    }

    /**
     * 停止screen状态监听
     */
    public void unregisterListener() {
        mContext.unregisterReceiver(mReceiver);
    }


    public interface afterReceive {// 返回给调用者屏幕状态信息
        void insertIntoDB();

    }
}