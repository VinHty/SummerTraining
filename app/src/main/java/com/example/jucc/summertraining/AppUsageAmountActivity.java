package com.example.jucc.summertraining;
/*
/author  VinHty on 16-7-16
 */

import android.app.Activity;
import android.app.usage.UsageStats;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppUsageAmountActivity extends FragmentActivity {

    /*
    /存放fragment的list
     */
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    /*
    /fragment adapter
     */
    private FragmentAdapter mFragmentAdapter;
    /*
    /textView的click listener
     */
    private MyOnClickListener onClickListener;

    // view pager
    private ViewPager mPageVp;
    /**
     * Tab显示内容TextView
     */
    private TextView mTabTodayTv, mTabYesterdayTv, mTabWeekTv;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;
    /**
     * Fragment
     */
    private MyFragment mTodayFg;
    private MyFragment mYesterdayFg;
    private MyFragment mWeekFg;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    //pager的listener
    private ViewPager.OnPageChangeListener listener;
    // 数据库对象
    private DatabaseMethod databaseMethod;

    /*
    /广播接收器，接收日期变化广播
     */
    public class MyReceiver extends BroadcastReceiver {
        private final String actionName = "android.intent.action.DATE_CHANGED";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == actionName) {
                insertIntoDB(mTodayFg);
            }
        }

        /*
        /将今日用量存入数据库 持久化数据
         */
        public void insertIntoDB(MyFragment f) {
            for (int a = 0; a < f.list.size(); a++) {
                String date = DatabaseMethod.getStringYesterday();
                HashMap map = f.list.get(a);
                String appName = (String) map.get("title");
                int useTime = (int) map.get("time");
                f.method.insert_usetime(appName, useTime, date);
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage_amount);
        //实例化数据库对象
        databaseMethod = DatabaseMethod.getInstance(this);
        databaseMethod.increaseCoins(1200);
        //获取各种控件
        findById();
        init();
        initTabLineWidth();
        //如果不能获取用量信息，则调用系统设置，给app授权
        if (TodayFragment.getUsageStats(this)==null) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    public void insertUsage() {
        if (checkFirstLanuch()) {
            //第一次启动 将用量存入数据库
            for (int a = 0; a < mTodayFg.list.size(); a++) {
                HashMap map = mTodayFg.list.get(a);
                String appName = (String) map.get("title");
                int useTime = (int) map.get("time");
                databaseMethod.insert_nowusetime(appName, 0, useTime);
            }
        }
    }

    //判断当前活动是否有权限获取数据
    public static boolean hasModule(Activity act) {
        PackageManager packageManager = act.getApplicationContext().getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    //获取各种控件
    private void findById() {
        mTabTodayTv = (TextView) this.findViewById(R.id.today);
        mTabYesterdayTv = (TextView) this.findViewById(R.id.yesterday);
        mTabWeekTv = (TextView) this.findViewById(R.id.week);

        mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
        mPageVp = (ViewPager) findViewById(R.id.id_page_vp);
    }

    private void init() {
        //初始化fragment 每个页面的fragment 存入list中
        mTodayFg = new TodayFragment();
        mYesterdayFg = new YesterdayFragment();
        mWeekFg = new WeekFragment();
        mFragmentList.add(mTodayFg);
        mFragmentList.add(mYesterdayFg);
        mFragmentList.add(mWeekFg);

        mTabLineIv.setImageResource(R.drawable.line);
        //使得textview也可以更改当前显示的页面
        onClickListener = new MyOnClickListener();
        mTabTodayTv.setOnClickListener(onClickListener);
        mTabYesterdayTv.setOnClickListener(onClickListener);
        mTabWeekTv.setOnClickListener(onClickListener);

        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);
        listener = new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                mTabLineIv.setLayoutParams(lp);
            }

            //选取不同页面时 更改顶部text的颜色
            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTabTodayTv.setTextColor(Color.BLUE);
                        //mTodayFg.update();
                        break;
                    case 1:
                        mTabYesterdayTv.setTextColor(Color.BLUE);
                        break;
                    case 2:
                        mTabWeekTv.setTextColor(Color.BLUE);
                        break;
                }
                currentIndex = position;
            }
        };
        mPageVp.addOnPageChangeListener(listener);

    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 3;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mTabTodayTv.setTextColor(Color.BLACK);
        mTabYesterdayTv.setTextColor(Color.BLACK);
        mTabWeekTv.setTextColor(Color.BLACK);
    }

    private class MyOnClickListener implements View.OnClickListener {
        //切换页面
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.today:
                    mPageVp.setCurrentItem(0);
                    break;
                case R.id.yesterday:
                    mPageVp.setCurrentItem(1);
                    break;
                case R.id.week:
                    mPageVp.setCurrentItem(2);
                    break;
                default:
                    break;

            }
        }
    }

    //判断APP是否为第一次启动
    // 是则返回true 否则返回 false
    private boolean checkFirstLanuch() {
        SharedPreferences setting = getSharedPreferences("versionFile", 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).commit();
            return true;

            // Toast.makeText(MainActivity.this, "第一次", Toast.LENGTH_LONG).show();
        } else {
            return false;
            //  Toast.makeText(MainActivity.this, "不是第一次", Toast.LENGTH_LONG).show();
        }
    }


}