package com.example.jucc.summertraining;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements CircleTimePiker.TimeAdapter, CircleTimePiker.OnSelectionChangeListener {
    private CircleTimePiker circleSelect;
    private TextView circleSelectTv;
    public final Bundle bundle=new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButton();
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

    @Override
    public String getNameByPosition(int position) {
        if (position % 5 == 0) {
            return position + "";
        }
        return "";
    }

    @Override
    public void onPositionChange(int newPositoin, int oldPosition) {
        circleSelectTv.setText("设定时长为" + newPositoin+"分钟");
        bundle.putInt("lastTime",newPositoin);
    }

    @Override
    public int getCount() {
        return 60;
    }

    public void initButton(){
        Button queryJob = (Button)findViewById(R.id.job_query);
        Button queryTiming = (Button)findViewById(R.id.timing_query);
        Button shop = (Button)findViewById(R.id.shop);
        Button setting = (Button)findViewById(R.id.setting);
        Button start =(Button)findViewById(R.id.start_timing);
        queryJob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, JobDetailsAcitiviy.class);
                MainActivity.this.startActivity(intent);
            }
        });
        queryTiming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AppUsageAmountActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShopActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, ExecutionActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    public void initSelector(){
        circleSelect = (CircleTimePiker) findViewById(R.id.circleSelect);
        circleSelectTv = (TextView) findViewById(R.id.circleSelectTv);
        circleSelect.setAdapter(this);
        circleSelect.setOnSelectionChangeListener(this);
        circleSelectTv.setText("请选择时间");

    }

}

