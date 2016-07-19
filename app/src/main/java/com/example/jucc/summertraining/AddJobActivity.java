package com.example.jucc.summertraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.sql.Time;
import java.util.Calendar;

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

        dbMethod = new DatabaseMethod(this);
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
        //
        //




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
                Calendar c = Calendar.getInstance();
                int currentYear = c.get(Calendar.YEAR);
                int currentMonth = c.get(Calendar.MONTH);
                int currentDay = c.get(Calendar.DATE);
                int currentHour = c.get(Calendar.HOUR);
                int currentMinute = c.get(Calendar.MINUTE);
                String currentTime = "" + currentYear + "-" + currentMonth + "-" + currentDay + " " + currentHour + ":" + currentMinute;
                //调用数据库插入或更新函数，向里面插入或者更新任务的信息
                if(needNotificationOrNot == true) {
                    dbMethod.insert_jobWithAlert(currentTime, jobTitle,alertTime );
                }else{
                    dbMethod.insert_jobWithoutAlert(currentTime,jobTitle,alertTime);
                }

                //然后返回查看任务界面，同时任务界面需要更新UI，读取新的任务
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
