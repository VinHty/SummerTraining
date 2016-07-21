package com.example.jucc.summertraining;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//当用户在主界面点击任务按钮的时候，跳转至该界面，即任务界面
public class JobDetailsAcitiviy extends ListActivity {

    private List<Map<String, Object>> mData;
    private Button button_return;
    private Button button_add;
    private Button button_listFinishedJob;
    private DatabaseMethod dbMethod;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details_acitiviy);

        dbMethod = DatabaseMethod.getInstance(this);
        button_return = (Button)findViewById(R.id.activity_job_details_return);
        button_add = (Button) findViewById(R.id.activity_job_details_add);
        button_listFinishedJob = (Button)findViewById(R.id.activity_job_details_listFinishedJob);

        //返回按钮的监听事件
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(JobDetailsAcitiviy.this,MainActivity.class);
                startActivity(returnIntent);
            }
        });

        //添加按钮的监听事件
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addJobActivity = new Intent(JobDetailsAcitiviy.this,AddJobActivity.class);
                startActivity(addJobActivity);
            }
        });

        //列出所有已完成任务的按钮的监听事件
        button_listFinishedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listFinishedJob = new Intent(JobDetailsAcitiviy.this,ListFinishedJob.class);
                startActivity(listFinishedJob);
                
            }
        });

        //构建listView
        mData = getData();
        adapter = new MyAdapter(this);
        setListAdapter(adapter);

    }

    //从数据库获得对应数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseMethod dbMethod = DatabaseMethod.getInstance(this);
        List<Job> myJobs = dbMethod.getUnfinishJob();  //从数据库读取未完成的任务,以list的形式
        for(int i = 0; i < myJobs.size(); i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("title",myJobs.get(i).getTitle());
            map.put("timeStamp",myJobs.get(i).getTimeStamp());
            map.put("alertTime",myJobs.get(i).getRemindTime());
            map.put("edit","edit" + i);
            map.put("start","start" + i);
            map.put("delete","delete" + i);
            list.add(map);
        }
        return list;
    }

    //获取当前日期和时间
    public String getCurrentTime(){
        DatabaseMethod db =DatabaseMethod.getInstance(this);
        return db.getStringSecond();
     }

    //listview中每个item的布局class
    public final class ViewHolder{
        public TextView title;
        public Button edit;
        public Button start;
        public Button delete;
    }

    //存取数据的adapter
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        //adapter的构造函数
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

        //listview中每行的view
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.activity_job_details_listview_item, null);
                holder.title = (TextView)convertView.findViewById(R.id.activity_job_details_textview);
                holder.edit = (Button)convertView.findViewById(R.id.activity_job_details_edit);
                holder.start = (Button)convertView.findViewById(R.id.activity_job_details_start);
                holder.delete = (Button)convertView.findViewById(R.id.activity_job_details_delete);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            //设置listview中每行的文本信息
            holder.title.setText((String)mData.get(position).get("title"));
            holder.edit.setText((String)mData.get(position).get("edit"));
            holder.start.setText((String)mData.get(position).get("start"));
            holder.delete.setText((String)mData.get(position).get("delete"));

            //监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            //delete按钮的监听事件
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbMethod.delete_job(mData.get(position).get("timeStamp").toString());
                    mData.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });


            final EditText text = new EditText(JobDetailsAcitiviy.this);
            //开始按钮的监听事件
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     //弹出窗口获得用户输入的任务时长
                     new AlertDialog.Builder(JobDetailsAcitiviy.this).setTitle("请输入任务时长：")
                            .setView(text)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent startJob = new Intent(JobDetailsAcitiviy.this,ExecutionActivity.class);
                                    long lastTime = Long.valueOf(text.getText().toString()).longValue();
                                    startJob.putExtra("title",mData.get(position).get("title").toString());
                                    startJob.putExtra("timeStamp",mData.get(position).get("timeStamp").toString());
                                    startJob.putExtra("lastTime",lastTime);
                                    startActivity(startJob);
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }
            });

            //edit按钮的响应事件
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editIntent = new Intent(JobDetailsAcitiviy.this,AddJobActivity.class);
                    editIntent.putExtra("timeStamp",mData.get(position).get("timeStamp").toString());
                    startActivity(editIntent);
                }
            });
            return convertView;
        }
    }
}




