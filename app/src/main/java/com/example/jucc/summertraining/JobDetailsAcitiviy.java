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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobDetailsAcitiviy extends ListActivity {

    private List<Map<String, Object>> mData;
    private Button button_return;
    private Button button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details_acitiviy);

        button_return = (Button)findViewById(R.id.activity_job_details_return);
        button_add = (Button) findViewById(R.id.activity_job_details_add);

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(JobDetailsAcitiviy.this,MainActivity.class);
                startActivity(returnIntent);
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "Math");
        map.put("edit", "edit1");
        map.put("start", "start1");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "Chinese");
        map.put("edit", "edit2");
        map.put("start", "start2");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "English");
        map.put("edit", "edit2");
        map.put("start", "start2");
        list.add(map);

        return list;
    }

    public final class ViewHolder{
        public TextView title;
        public TextView edit;
        public TextView start;
    }

    public class MyAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.activity_job_details_listview_item, null);
                holder.title = (TextView)convertView.findViewById(R.id.activity_job_details_textview);
                holder.edit = (TextView)convertView.findViewById(R.id.activity_job_details_edit);
                holder.start = (TextView)convertView.findViewById(R.id.activity_job_details_start);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            holder.title.setText((String)mData.get(position).get("title"));
            holder.edit.setText((String)mData.get(position).get("edit"));
            holder.start.setText((String)mData.get(position).get("start"));

            ItemListener itemListener = new ItemListener(position); //监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件

            holder.edit.setOnClickListener(itemListener);
            holder.start.setOnClickListener(itemListener);

            return convertView;
        }
    }

    class ItemListener implements View.OnClickListener {
        private int m_position;

        ItemListener(int pos) {
            m_position = pos;
        }

        @Override
        public void onClick(View v) {
            Log.v("MyListView-click", "line:" + m_position + ":"+ v.getTag());
        }
    }
}




