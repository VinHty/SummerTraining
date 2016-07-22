package com.example.jucc.summertraining;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements CircleTimePiker.TimeAdapter, CircleTimePiker.OnSelectionChangeListener {
    private CircleTimePiker circleSelect;
    private TextView circleSelectTv;
    public  Bundle bundle=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化按键
        initButton();
        //初始化时间选择器
        initSelector();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);  
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
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

        circleSelectTv.setText("设定时长为" + newPositoin+"分钟");
        bundle.putInt("lastTime",newPositoin);
    }

    @Override
    public int getCount() {
        return 60;
    }


    //初始化各个按钮
    public void initButton(){
        //初始化查看任务按钮，点击后跳转到对应活动
        Button queryJob = (Button)findViewById(R.id.job_query);
        queryJob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, JobDetailsAcitiviy.class);
                MainActivity.this.startActivity(intent);
            }
        });
        //初始化查看手机使用情况按钮，点击后跳转到对应活动
        Button queryTiming = (Button)findViewById(R.id.timing_query);
        queryTiming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //该功能只支持api22及以上功能，若版本低会弹出提醒
                if(getAndroidSDKVersion()<=22){
                    new AlertDialog.Builder(MainActivity.this).setTitle("提醒")//设置对话框标题
                            .setMessage("您的安卓版本较低，暂无法使用该功能")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮



                                @Override

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                }

                            }).show();//在按键响应事件中显示此对话框
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AppUsageAmountActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
        //初始化查看设置按钮，点击后跳转到对应活动
        Button setting = (Button)findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        //初始化商店按钮，点击后跳转到对应活动
        Button shop = (Button)findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShopActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        //初始化开始按钮，点击后发送intent并且跳转到对应活动
        Button start =(Button)findViewById(R.id.start_timing);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, ExecutionActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


    }
    //初始化选择器
    public void initSelector(){
        circleSelect = (CircleTimePiker) findViewById(R.id.circleSelect);
        circleSelectTv = (TextView) findViewById(R.id.circleSelectTv);
        circleSelect.setAdapter(this);
        circleSelect.setOnSelectionChangeListener(this);
        circleSelectTv.setText("设定时长为0分钟");

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

}

