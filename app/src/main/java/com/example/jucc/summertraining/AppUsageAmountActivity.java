package com.example.jucc.summertraining;
/*
/author  VinHty on 16-7-16
 */

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppUsageAmountActivity extends FragmentActivity {

    private Map<String,Integer> map = new HashMap<>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private MyOnClickListener onClickListener;
    private DatabaseMethod method;

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

    private ViewPager.OnPageChangeListener listener;
    private DatabaseMethod databaseMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage_amount);
        databaseMethod= new DatabaseMethod(this);
        findById();
        init();
        initTabLineWidth();
        try {
            getInfo();
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
        if(checkFirstLanuch()){
            //第一次启动
            Iterator it = map.keySet().iterator();
            while (it.hasNext())
            {
                String appName = (String) it.next();
                databaseMethod.insert_nowusetime(appName,0,map.get(appName));
            }
        }
        MyListener listener=new MyListener(this);
        listener.begin(new MyListener.afterReceive() {
            @Override
            public void insertIntoDB() {
                for (int a = 0; a < mTodayFg.list.size(); a++) {
                    String date = DatabaseMethod.getStringYesterday();
                    HashMap map = mTodayFg.list.get(a);
                    String appName = (String) map.get("title");
                    int useTime = (int) map.get("time");
                    method.insert_usetime(appName, useTime, date);
                }
            }
        });


    }

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

        method=new DatabaseMethod(this);

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

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTabTodayTv.setTextColor(Color.BLUE);
                        mTodayFg.update();
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
        //网上的关于获取应用程序使用情况的代码 需要研究一下
    private void getInfo() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
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
        for (Object pkgUsageStats : oPkgUsageStatsArray) {
            String packageName = (String) cPkgUsageStats.getDeclaredField("packageName").get(pkgUsageStats);
            int launchCount = cPkgUsageStats.getDeclaredField("launchCount").getInt(pkgUsageStats);
            long usageTime = cPkgUsageStats.getDeclaredField("usageTime").getLong(pkgUsageStats);
            Map<String, Long> componentResumeMap = (Map<String, Long>) cPkgUsageStats.getDeclaredField("componentResumeTimes").get(pkgUsageStats);
            map.put(packageName,Integer.parseInt(Long.toString(usageTime)));
        }


    }
    private boolean checkFirstLanuch(){
        SharedPreferences setting = getSharedPreferences("versionFile", 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次
            setting.edit().putBoolean("FIRST", false).commit();
            return true;

           // Toast.makeText(MainActivity.this, "第一次", Toast.LENGTH_LONG).show();
        }else{
            return false;
          //  Toast.makeText(MainActivity.this, "不是第一次", Toast.LENGTH_LONG).show();
        }
    }


}