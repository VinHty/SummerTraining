package com.example.jucc.summertraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

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

        yesSetJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}
