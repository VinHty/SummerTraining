package com.example.jucc.summertraining;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jucc.summertraining.Entity.Fish;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;
import static com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod.getInstance;
import static com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod.getStringSecond;
import static com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod.getStringTime;

public class ExecutionActivity extends Activity {


    private float mTotalProgress;
    private float mCurrentProgress;
    private CircleProgressBar rpBar01;
    private float lastTime;
    private Context mContext;
    private CountDownTimer timer;
    private String timeStampJob;
    private int state=0;
    private ImageButton fishState;
    private int small;
    private int middle;
    private int big;
    private TextView time;
    private int fish;
    private int now=0;
    private Bundle bundle;
    private Intent intent;
    private Vibrator vibrator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execution);
        time=(TextView)findViewById(R.id.time_counting);
        intent=this.getIntent();
        bundle=intent.getExtras();
        small=bundle.getInt("smallFish");
//        Log.e("ss","small     " +small    );
        middle=bundle.getInt("middleFish");
        big=bundle.getInt("bigFish");
        fish=bundle.getInt("fish");
        fishState=(ImageButton) findViewById(R.id.fish_state);
        fishState.setImageResource(R.drawable.xiaoxiaoyu);
        //初始化进度条的变量值
        initVariable();
        //初始化时间选择器
        viewInit();
        //根据收到的Intent判断开始的任务是快速任务还是已经添加的任务
        initJob();
        //倒计时进行时对进度条进行更新
        startCounting();
        //开始倒计时
        timer.start();
        //初始化放弃按钮，并且响应时间为弹出对话框
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


        String timeStamp1=bundle.getString("timeStamp");
        long lastTime2=bundle.getInt("lastTime")*60000;
        String title=bundle.getString("title");
        int lastTime1 = bundle.getInt("lastTime")*60000;

        mContext=getBaseContext();
        //timeStamp为null说明为快速任务
        if(timeStamp1==null){
        DatabaseMethod quickJob=getInstance(mContext);
        int i=new Float(lastTime1/60000).intValue();
        String timeStamp=getStringSecond();
            //调用方法，将任务的相关数据存入数据库
        quickJob.insert_quickjob(timeStamp,"自定义任务",i,getStringTime());
        lastTime=lastTime1;
        timeStampJob=timeStamp;
        }
        else{
            DatabaseMethod doJob=getInstance(mContext);
            int i=new Long(lastTime2/60000).intValue();
            //调用方法，将任务的相关数据存入数据库
            doJob.update_jobWhenStart(title,i,getStringTime(),timeStamp1);
            lastTime=lastTime2;
            timeStampJob=timeStamp1;
        }
    }
    private void startCounting(){
        int i=new Float(lastTime).intValue();

        //时间间隔设为一秒
        timer=new CountDownTimer(i,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每秒对当前进度进行更新
                long myminute = (millisUntilFinished / 1000) / 60;
                long mysecond = millisUntilFinished / 1000 - myminute * 60;
                time.setText( myminute + ":" + mysecond);
                int finish=new Long(millisUntilFinished).intValue();
                    now=now+1;
                Log.e("ss","small     " +small    );
                    switch (getState(now)){
                        case 0:fishState.setImageResource(R.drawable.xiaoxiaoyu);
                            break;
                        case 1:fishState.setImageResource(small);
                            break;
                        case 2:fishState.setImageResource(middle);
                            break;
                        case 3:fishState.setImageResource(big);
                            break;

                }
                mCurrentProgress =(lastTime-finish)/(lastTime/100);
                rpBar01.setProgress(mCurrentProgress);

            }

            @Override
            //倒计时结束时执行此函数
            public void onFinish() {
                mCurrentProgress=100;
                rpBar01.setProgress(mCurrentProgress);
                mContext=getBaseContext();
                //将任务完成情况存入数据库
                DatabaseMethod quickJobFinish= DatabaseMethod.getInstance(mContext);
               quickJobFinish.update_jobWhenFinish(timeStampJob,true);
                if (state!=0){
                    Fish fishFinish=new Fish(fish,state-1,true);
                    quickJobFinish.updateAchievement(fishFinish);
                }
                int i=(int)lastTime/60000;
                int m=(int)(i*0.6+Math.pow(2,state));
                Log.e("Exe","m:   "+m);
                quickJobFinish.increaseCoins(m);
                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
                vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
          //      onStop();
                new AlertDialog.Builder(ExecutionActivity.this).setTitle("成功")//设置对话框标题1
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

    //放弃任务时执行
    private void giveUpJob(){
        //取消计时器
        timer.cancel();
        mContext=getBaseContext();
        //将任务完成情况存入数据库
        DatabaseMethod quickJobGiveUp=DatabaseMethod.getInstance(mContext);
        quickJobGiveUp.update_jobWhenFinish(timeStampJob,false);
        if (state!=0){
            Fish fishGiveUp=new Fish(fish,state-1,false);
            quickJobGiveUp.updateAchievement(fishGiveUp);

        }


        //关闭Activity并且跳转到MainActivity
//        Intent intent = new Intent();
//        intent.setClass(ExecutionActivity.this,MainActivity.class);
//        ExecutionActivity.this.startActivity(intent);
        finish();
    }

    private int getState(int i) {
        if (i <= 600) {
            state = 0;
        } else if (i <= 1800) {
            state = 1;
        } else if (i <= 4200) {
            state = 2;
        } else state = 3;

        return state;
    }


}
