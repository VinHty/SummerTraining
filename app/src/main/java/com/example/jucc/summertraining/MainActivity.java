package com.example.jucc.summertraining;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import static com.example.jucc.summertraining.R.styleable.Toolbar;

public class MainActivity extends AppCompatActivity {
private Button queryJob;
    private Button queryTiming;
    private  Button shop;
    private Button setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button queryJob = (Button)findViewById(R.id.job_query);
        Button queryTiming = (Button)findViewById(R.id.timing_query);
        Button shop = (Button)findViewById(R.id.shop);
        Button setting = (Button)findViewById(R.id.setting);

        queryJob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, JobDetailsAcitiviy.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
        queryTiming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AppUsageAmountActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShopActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);  
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
