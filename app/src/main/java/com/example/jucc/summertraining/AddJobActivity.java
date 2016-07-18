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

import java.sql.Time;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

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
                Toast.makeText(AddJobActivity.this, "currentHour is " + hour + " currentMinute is " + minute, Toast.LENGTH_LONG).show();
                //调用数据库插入或更新函数，向里面插入或者更新任务的信息
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
