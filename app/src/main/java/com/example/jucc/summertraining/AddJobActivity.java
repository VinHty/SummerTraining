package com.example.jucc.summertraining;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//用户在任务界面，点击添加按钮后，跳转到此界面，即添加任务界面
public class AddJobActivity extends Activity {

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_job);


        dbMethod = DatabaseMethod.getInstance(this);
        yesSetJob = (Button) findViewById(R.id.activity_activity_add_job_yes);
        noSetJob = (Button) findViewById(R.id.activity_activity_add_job_no);
        editText = (EditText) findViewById(R.id.activity_activity_add_job_edittext);
        needNotification = (RadioButton) findViewById(R.id.activity_activity_add_job_needNotification);
        noNeedNotification = (RadioButton) findViewById(R.id.activity_activity_add_job_notNeedNotification);
        //setDatePicker = (Button)findViewById(R.id.activity_activity_add_job_showDatePicker);
        //setTimePicker = (Button)findViewById(R.id.activity_activity_add_job_showTimePicker);
        datePicker = (DatePicker) findViewById(R.id.activity_activity_add_job_datePicker);
        timePicker = (TimePicker) findViewById(R.id.activity_activity_add_job_timePicker);
        resizePikcer(datePicker);//调整datepicker大小
        resizePikcer(timePicker);//调整timepicker大小
        timePicker.setIs24HourView(true);

        //若启动该activity的intent中含有非空的时间戳数据，则表示用户点击的是修改按钮，所以下面的代码是用于初始化该界面的所有数据
        Intent intent = getIntent();
        final String timeStamp = intent.getStringExtra("timeStamp");
        if (timeStamp != null) {

            //从数据库里获得相应数据
            List<Job> myJobs = dbMethod.getJobWhenEdit(timeStamp);
            String title = myJobs.get(0).getTitle();
            int needNotificationOrNot = myJobs.get(0).getisRemind();
            String alertTime = myJobs.get(0).getRemindTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            editText.setText(title);
            if (needNotificationOrNot == 1) {
                needNotification.setChecked(true);
            } else {
                noNeedNotification.setChecked(false);
            }
            try {
                Date date = formatter.parse(alertTime);
                Calendar cld = Calendar.getInstance();
                cld.setTime(date);

                //初始化数据
                datePicker.updateDate(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH), cld.get((Calendar.DAY_OF_MONTH)));
                timePicker.setCurrentHour(cld.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(cld.get(Calendar.MINUTE));
            } catch (Exception e) {
            }
        }

        //点击确定按钮后的响应时间
        yesSetJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获得任务的各个信息
                String jobTitle = (String) editText.getText().toString();  //任务标题
                //对数据输入格式进行限制
                if (jobTitle.replace(" ", "").isEmpty()) {
                    new AlertDialog.Builder(AddJobActivity.this).setTitle("FBI WARNING")
                            .setMessage("任务名字不可为空，请重新输入")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

                Boolean needNotificationOrNot = needNotification.isChecked();//如果选择了需要提醒，则为true;如果选择了不需要提醒，则为false
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int date = datePicker.getDayOfMonth();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String alertTime = "" + year + "-" + month + "-" + date + " " + hour + ":" + minute;

                if (!jobTitle.replace(" ", "").isEmpty()) {
                    //根据timeStamp的有无来判断是用户是在建立新任务还是在修改已经存在的任务，进而调用不同的数据库语句
                    if (needNotificationOrNot == true && timeStamp == null) {
                        dbMethod.insert_jobWithAlert(dbMethod.getStringSecond(), jobTitle, alertTime);
                    } else if (needNotificationOrNot == false && timeStamp == null) {
                        dbMethod.insert_jobWithoutAlert(dbMethod.getStringSecond(), jobTitle, alertTime);
                    } else if (timeStamp != null) {
                        dbMethod.update_jobWhenEdit(timeStamp, jobTitle, alertTime, needNotificationOrNot);
                    }

                    //然后返回查看任务界面，同时任务界面需要更新UI，读取新的任务
                    Intent returnJobDetailsActivity = new Intent(AddJobActivity.this, JobDetailsAcitiviy.class);
                    startActivity(returnJobDetailsActivity);
                }
            }
        });

        //取消按钮的响应事件，用户点击取消，则返回JobDetailsAcitivity界面
        noSetJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnJobDetailsActivity = new Intent(AddJobActivity.this, JobDetailsAcitiviy.class);
                startActivity(returnJobDetailsActivity);
            }
        });
    }

        //设置日期按钮的响应事件
      /*  setDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
            }
        });

        //设置时间按钮的响应事件
        setTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });
        */

        /**
         * 调整FrameLayout大小
         * @param tp
         */
        private void resizePikcer(FrameLayout tp){
            List<NumberPicker> npList = findNumberPicker(tp);
            for(NumberPicker np:npList){
                resizeNumberPicker(np);
            }
        }

        /**
         * 得到viewGroup里面的numberpicker组件
         * @param viewGroup
         * @return
         */
        private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
            List<NumberPicker> npList = new ArrayList<NumberPicker>();
            View child = null;
            if(null != viewGroup){
                for(int i = 0; i < viewGroup.getChildCount(); i++){
                    child = viewGroup.getChildAt(i);
                    if(child instanceof NumberPicker){
                        npList.add((NumberPicker)child);
                    }
                    else if(child instanceof LinearLayout){
                        List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                        if(result.size()>0){
                            return result;
                        }
                    }
                }
            }
            return npList;
        }

	/*
	 * 调整numberpicker大小
	 */
        private void resizeNumberPicker(NumberPicker np){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);
            np.setLayoutParams(params);
        }

    }

