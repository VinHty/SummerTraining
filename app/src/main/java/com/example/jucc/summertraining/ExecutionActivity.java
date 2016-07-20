package com.example.jucc.summertraining;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;
import android.widget.Button;

import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;
import com.example.jucc.summertraining.RelatedToDataBase.MyDatabaseHelper;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod.getStringTime;

public class ExecutionActivity extends Activity {


    private float mTotalProgress;
    private float mCurrentProgress;
    private CircleProgressBar rpBar01;
    private float lastTime;
    private Context mContext;
    private CountDownTimer timer;
    private String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execution);
        initVariable();
        viewInit();
        initJob();
        startCounting();
        timer.start();
        final Button giveUp =(Button)findViewById(R.id.give_up);
        giveUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(ExecutionActivity.this).setTitle("放弃任务？")//设置对话框标题
                        .setMessage("放弃任务将会记录为失败")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮



                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    giveUpJob();
                            }

                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮



                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }

                }).show();//在按键响应事件中显示此对话框
            }
        });
    }

    private void initVariable() {
        mTotalProgress = 100;
        mCurrentProgress = 0;
    }

    private void viewInit() {

        rpBar01 = (CircleProgressBar) findViewById(R.id.circleProgerss);

    }
    private void initJob(){
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String timeStamp=bundle.getString("timeStamp");
        long lastTime2=bundle.getLong("lastTime")*60000;
        String title=bundle.getString("title");
        int lastTime1 = bundle.getInt("lastTime")*60000;
        mContext=getBaseContext();
        if(timeStamp==null){
        DatabaseMethod quickJob=new DatabaseMethod(mContext);
        timeStamp=getStringTime();
        int i=new Float(lastTime1/60000).intValue();
        quickJob.insert_quickjob(timeStamp,"自定义任务",i,timeStamp);
        lastTime=lastTime1;}
        else{
            DatabaseMethod doJob=new DatabaseMethod(mContext);
            int i=new Long(lastTime2/60000).intValue();
            doJob.update_jobWhenStart(timeStamp,i,getStringTime());
            lastTime=lastTime2;
        }
    }
    private void startCounting(){
        int i=new Float(lastTime).intValue();
        timer=new CountDownTimer(i,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int finish=new Long(millisUntilFinished).intValue();
                mCurrentProgress =(lastTime-finish)/(lastTime/100);
                rpBar01.setProgress(mCurrentProgress);
            }

            @Override
            public void onFinish() {
                mContext=getBaseContext();
                DatabaseMethod quickJobFinish=new DatabaseMethod(mContext);
               quickJobFinish.update_jobWhenFinish(timeStamp,true);
                new AlertDialog.Builder(ExecutionActivity.this).setTitle("成功")//设置对话框标题
                        .setMessage("任务成功")//设置显示的内容
                        .setPositiveButton("分享",new DialogInterface.OnClickListener() {//添加确定按钮



                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                            }

                        }).setNegativeButton("回主界面",new DialogInterface.OnClickListener() {//添加返回按钮



                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        Intent intent = new Intent();
                        intent.setClass(ExecutionActivity.this,MainActivity.class);
                        ExecutionActivity.this.startActivity(intent);
                        finish();
                    }

                }).show();//在按键响应事件中显示此对话框
            }
        };
    }
    private void giveUpJob(){
        timer.cancel();
        mContext=getBaseContext();
        DatabaseMethod quickJobGiveUp=new DatabaseMethod(mContext);
        quickJobGiveUp.update_jobWhenFinish(timeStamp,false);
        Intent intent = new Intent();
        intent.setClass(ExecutionActivity.this,MainActivity.class);
        ExecutionActivity.this.startActivity(intent);
        finish();
    }

}
