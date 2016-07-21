package com.example.jucc.summertraining;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFinishedJob extends ListActivity {

    private List<Map<String, Object>> mData;
    private Button button_return;
    private Button button_listSuccessfulJob;
    private Button button_listFailJob;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_finished_job);

        button_return = (Button)findViewById(R.id.activity_list_finished_job_return);
        button_listFailJob = (Button)findViewById(R.id.activity_list_finished_job_failed);
        button_listSuccessfulJob = (Button)findViewById(R.id.activity_list_finished_job_successful);

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnJobDetailsActivity = new Intent(ListFinishedJob.this,JobDetailsAcitiviy.class);
                startActivity(returnJobDetailsActivity);
            }
        });

        button_listSuccessfulJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = getData(true);
                adapter = new MyAdapter(ListFinishedJob.this);
                setListAdapter(adapter);
            }
        });

        button_listFailJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = getData(false);
                adapter = new MyAdapter(ListFinishedJob.this);
                setListAdapter(adapter);

            }
        });

        mData = getData(true);
        adapter = new MyAdapter(this);
        setListAdapter(adapter);

    }

    private List<Map<String, Object>> getData(Boolean isSuc) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DatabaseMethod dbMethod = DatabaseMethod.getInstance(this);
        List<Job> myJobs = dbMethod.getFinishJob(isSuc);  //从数据库读取未完成的任务,以list的形式
        Log.e("myJobs size is","" + myJobs.size());
        for(int i = 0; i < myJobs.size(); i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("job_name",myJobs.get(i).getTitle());

            Log.e("job_name",myJobs.get(i).getTitle());

            map.put("alertTime",myJobs.get(i).getRemindTime());
            map.put("startTime",myJobs.get(i).getBeginTime());
            list.add(map);
        }

        return list;
    }

    public final class ViewHolder{
        public TextView job_name;
        public TextView alertTime;
        public TextView startTime;
    }

    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.activity_list_finished_job_listview,null);
                holder.alertTime = (TextView)convertView.findViewById(R.id.activity_list_finished_job_dialog_alerttime);
                holder.job_name = (TextView)convertView.findViewById(R.id.activity_list_finished_job_listview_jobname);
                holder.startTime = (TextView)convertView.findViewById(R.id.activity_list_finished_job_dialog_starttime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.job_name.setText((String)mData.get(position).get("job_name"));
            holder.startTime.setText("Start Time:" + (String)mData.get(position).get("startTime"));
            holder.alertTime.setText("Alert Time:" + (String)mData.get(position).get("alertTime"));

            return convertView;

        }

    }

}






