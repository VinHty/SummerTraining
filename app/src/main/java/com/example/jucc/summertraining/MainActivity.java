package com.example.jucc.summertraining;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends Activity implements CircleTimePiker.TimeAdapter, CircleTimePiker.OnSelectionChangeListener {
    private CircleTimePiker circleSelect;
    private TextView circleSelectTv;
    public Bundle bundle = new Bundle();
    private PopupWindow popMenu;
    private ImageButton mPopWindowBtn;
    private View mPopWindowView;
    private int[][] fish;
    private ImageButton selectFish;
    private int n, m, allFish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectFish = (ImageButton) findViewById(R.id.fish_image);
//        selectFish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeImage();
//            }
//        });
        m = 0;
        n = 0;
        //鱼的种类初始化为0
        allFish = 0;
        //初始化按键
        initButton();
//        数组存放鱼的不同种类和不同状态对应的图片资源id
        fish = new int[6][3];
        initUsableFish();
        //初始化时间选择器
        initSelector();
    }


    //设置监听事件
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        int[] i=new int[2];
//        circleSelect.getLocationOnScreen(i);
//        float x=event.getRawX()-i[0];
//        float y=event.getRawY()-i[1];
//        float r=circleSelect.getmCircleRadius()-50;
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//
//
//            case MotionEvent.ACTION_MOVE:
//                if(x*x+y*y<r*r){
//                    changeImage();
//                    Log.e("position","POSITION:   "+x+y);
//                }
//            if(x*x+y*y>=r*r) {
//                 circleSelect.getTouchPositionFromPoint(event.getX(), event.getY());
//            }
//
//                break;
//        }
//
//
//        return true;
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getLayoutInflater().inflate(R.layout.pop_menu_list, null);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    //获取位置
    @Override
    public String getNameByPosition(int position) {
        if (position % 5 == 0) {
            return position + "";
        }
        return "";
    }


    //获取位置，并将位置对应的值赋值给bundle
    @Override
    public void onPositionChange(int newPositoin, int oldPosition) {

        circleSelectTv.setText(newPositoin + ":00");

        changeState(fish, newPositoin);

        bundle.putInt("lastTime", newPositoin);
    }

    //设置圆形选择器的最大值
    @Override
    public int getCount() {
        return 120;
    }


    //初始化各个按钮
    public void initButton() {

        ImageButton imageButton = (ImageButton) findViewById(R.id.fish_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        //初始化开始按钮，点击后发送intent并且跳转到对应活动
        Button start = (Button) findViewById(R.id.start_timing);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
//开始时将鱼的不同状态的id发送给执行界面
                int small = fish[m][0];
                int middle = fish[m][1];
                int big = fish[m][2];
                bundle.putInt("fish", m);
                bundle.putInt("smallFish", small);
                bundle.putInt("middleFish", middle);
                bundle.putInt("bigFish", big);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, ExecutionActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        final ImageButton menu1 = (ImageButton) findViewById(R.id.pop_menu);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//展开popwindow
                showPopWindow();

            }
        });


    }

    //初始化选择器
    public void initSelector() {
        circleSelect = (CircleTimePiker) findViewById(R.id.circleSelect);
        circleSelectTv = (TextView) findViewById(R.id.circleSelectTv);
        circleSelect.setAdapter(this);
        circleSelect.setOnSelectionChangeListener(this);
        circleSelectTv.setText("0:00");

    }

    //获取sdk版本
    private int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }


    //展示popwindow
    private void showPopWindow() {
        mPopWindowBtn = (ImageButton) findViewById(R.id.pop_menu);
        mPopWindowView = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_menu_list, null);
        // 创建一个PopuWidow对象
        //初始化查看任务按钮，点击后跳转到对应活动
        ImageButton queryJob = (ImageButton) mPopWindowView.findViewById(R.id.job_query);
        queryJob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, JobDetailsAcitiviy.class);
                MainActivity.this.startActivity(intent);
            }
        });
        //初始化查看手机使用情况按钮，点击后跳转到对应活动
        ImageButton queryTiming = (ImageButton) mPopWindowView.findViewById(R.id.timing_query);
        queryTiming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //该功能只支持api21及以上功能，若版本低会弹出提醒
                if (getAndroidSDKVersion() < 21) {
                    new AlertDialog.Builder(MainActivity.this).setTitle("提醒")//设置对话框标题
                            .setMessage("您的安卓版本较低，暂无法使用该功能")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                                @Override

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                }

                            }).show();//在按键响应事件中显示此对话框
                } else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AppUsageAmountActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
//        //初始化查看设置按钮，点击后跳转到对应活动
//        ImageButton setting = (ImageButton) mPopWindowView.findViewById(R.id.setting);
//        setting.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, SettingsActivity.class);
//                MainActivity.this.startActivity(intent);
//            }
//        });
        //初始化商店按钮，点击后跳转到对应活动
        ImageButton shop = (ImageButton) mPopWindowView.findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShopActivity.class);
                MainActivity.this.startActivity(intent);
                finish();
            }
        });
        //初始化成就按钮，点击后跳转到对应活动
        ImageButton achi = (ImageButton) mPopWindowView.findViewById(R.id.achievement);
        achi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AchievementActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        popMenu = new PopupWindow(mPopWindowView, 700, 900, true);
        popMenu.setBackgroundDrawable(new BitmapDrawable());
        popMenu.showAsDropDown(mPopWindowBtn);
    }

    //获取所有可用种类的鱼，并将它们的所有状态对应的资源id存入二维数组fish中
    private void initUsableFish() {
        Context mContext = this;
        DatabaseMethod getUsableFish = DatabaseMethod.getInstance(mContext);
        List<Fish> listFish = getUsableFish.userAvailableFish();
        int all = listFish.size();
        int i = 0;
        int m = 0;
        int n = 0;

        while (i < all) {
            Log.e("fish:", "   n::" + n);
            fish[m][n] = listFish.get(i).getId();
            Log.e("fish:", "   fish" + fish[m][n]);
            n = n + 1;
            i++;

            if (n == 3) {
                n = 0;
                m = m + 1;
            }

        }
        allFish = all / 3;


    }


    //更改鱼的种类图片，点击后更换
    public void changeImage() {
        if (m == allFish - 1) {
            m = 0;
            selectFish.setImageResource(fish[m][n]);
        } else {
            m = m + 1;
            selectFish.setImageResource(fish[m][n]);
        }

    }


    //根据选择的时间更改鱼的状态
    public void changeState(int[][] i, int k) {
        if (k <= 20) {
            n = 0;
            selectFish.setImageResource(i[m][n]);
        } else if (k <= 50) {
            n = 1;
            selectFish.setImageResource(i[m][n]);
        } else
            n = 2;
        selectFish.setImageResource(i[m][n]);
    }

}

