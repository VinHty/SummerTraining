package com.example.jucc.summertraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddJobActivity extends AppCompatActivity {

    private Button yesSetJob;
    private Button noSetJob;
    private EditText editText;
    private RadioButton needNotification;
    private RadioButton noNeedNotification;
    private Button setDatePicker;
    private Button setTimePicker;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private DatabaseMethod dbMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        dbMethod = DatabaseMethod.getInstance(this);
        yesSetJob = (Button)findViewById(R.id.activity_activity_add_job_yes);
        noSetJob = (Button)findViewById(R.id.activity_activity_add_job_no);
        editText = (EditText)findViewById(R.id.activity_activity_add_job_edittext);
        needNotification = (RadioButton)findViewById(R.id.activity_activity_add_job_needNotification);
        noNeedNotification = (RadioButton)findViewById(R.id.activity_activity_add_job_notNeedNotification);
        setDatePicker = (Button)findViewById(R.id.activity_activity_add_job_showDatePicker);
        setTimePicker = (Button)findViewById(R.id.activity_activity_add_job_showTimePicker);
        datePicker = (DatePicker)findViewById(R.id.activity_activity_add_job_datePicker);
        timePicker = (TimePicker)findViewById(R.id.activity_activity_add_job_timePicker);
        timePicker.setIs24HourView(true);

        //若用户点击修改按钮，则有一个intent传来
        //然后所有数据都要初始化，从数据库里读取
        Intent intent = getIntent();
        final String timeStamp = intent.getStringExtra("timeStamp");
        if(timeStamp != null){
            List<Job> myJobs = dbMethod.getJobWhenEdit(timeStamp);
            String title = myJobs.get(0).getTitle();
            int needNotificationOrNot = myJobs.get(0).getisRemind();
            String alertTime = myJobs.get(0).getRemindTime();

            Log.e("remingtime is " , alertTime);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            editText.setText(title);
            if(needNotificationOrNot == 1){
                needNotification.setChecked(true);
            }else{
                noNeedNotification.setChecked(false);
            }

            try {
                Date date = formatter.parse(alertTime);
                Calendar cld = Calendar.getInstance();
                cld.setTime(date);
                datePicker.updateDate(cld.get(Calendar.YEAR),cld.get(Calendar.MONTH),cld.get((Calendar.DAY_OF_MONTH)));
                //Log.e("time" , "" + date.getYear() + ":" + date.getMonth() + ":" + date.getDay());
                timePicker.setCurrentHour(cld.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(cld.get(Calendar.MINUTE));
            }
            catch(Exception e){

            }
            //int year = Integer.valueOf("" + alertTime.charAt(0) + alertTime.charAt(1) + alertTime.charAt(2) + alertTime.charAt(3));
            //Log.e("title is ",year);
            //Toast.makeText(AddJobActivity.this,myJobs.get(0).getTitle(),Toast.LENGTH_LONG);



        }





        yesSetJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得任务的各个信息
                String jobTitle =(String) editText.getText().toString();  //任务标题
                Boolean needNotificationOrNot = needNotification.isChecked();//如果选择了需要提醒，则为true;如果选择了不需要提醒，则为false
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int date = datePicker.getDayOfMonth();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String alertTime = "" + year + "-" + month + "-" + date + " " + hour + ":" + minute;

                //获取当前时间

                if(needNotificationOrNot == true &&timeStamp ==null) {
                    dbMethod.insert_jobWithAlert(dbMethod.getStringSecond(),jobTitle,alertTime );
                }else if(needNotificationOrNot == false &&timeStamp ==null){
                    dbMethod.insert_jobWithoutAlert(dbMethod.getStringSecond(),jobTitle,alertTime);
                }else if(timeStamp != null){
                    dbMethod.update_jobWhenEdit(timeStamp,jobTitle,alertTime,needNotificationOrNot);
                }

                //然后返回查看任务界面，同时任务界面需要更新UI，读取新的任务
                Intent returnJobDetailsActivity = new Intent(AddJobActivity.this,JobDetailsAcitiviy.class);
                startActivity(returnJobDetailsActivity);
            }
        });

        noSetJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnJobDetailsActivity = new Intent(AddJobActivity.this,JobDetailsAcitiviy.class);
                startActivity(returnJobDetailsActivity);
            }
        });

        setDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
            }
        });

        setTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });




    }
}
