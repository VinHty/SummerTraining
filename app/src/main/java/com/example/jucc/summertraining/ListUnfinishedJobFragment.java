package com.example.jucc.summertraining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jucc.summertraining.Entity.Job;
import com.example.jucc.summertraining.RelatedToDataBase.DatabaseMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListUnfinishedJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListUnfinishedJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListUnfinishedJobFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Map<String, Object>> mData;
    private MyAdapter adapter;
    private DatabaseMethod dbMethod;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListUnfinishedJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListUnfinishedJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListUnfinishedJobFragment newInstance(String param1, String param2) {
        ListUnfinishedJobFragment fragment = new ListUnfinishedJobFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbMethod = DatabaseMethod.getInstance(getContext());
        mData = getData();
        adapter = new MyAdapter(getContext());
        setListAdapter(adapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_unfinished_job, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
         //   throw new RuntimeException(context.toString()
         //           + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //从数据库获得对应数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DatabaseMethod dbMethod = DatabaseMethod.getInstance(getContext());
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
        DatabaseMethod db =DatabaseMethod.getInstance(getContext());
        return db.getStringSecond();
    }

    //listview中每个item的布局class
    public final class ViewHolder{
        public TextView title;
        public ImageButton edit;
        public ImageButton start;
        public ImageButton delete;
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
                holder.edit = (ImageButton)convertView.findViewById(R.id.activity_job_details_edit);
                holder.start = (ImageButton)convertView.findViewById(R.id.activity_job_details_start);
                holder.delete = (ImageButton)convertView.findViewById(R.id.activity_job_details_delete);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            //设置listview中每行的文本信息
            holder.title.setText((String)mData.get(position).get("title"));
           // holder.edit.setText((String)mData.get(position).get("edit"));
           // holder.start.setText((String)mData.get(position).get("start"));
           // holder.delete.setText((String)mData.get(position).get("delete"));

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


            final EditText text = new EditText(getContext());
            //开始按钮的监听事件
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //弹出窗口获得用户输入的任务时长
                    new AlertDialog.Builder(getContext()).setTitle("请输入任务时长：")
                            .setView(text)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent startJob = new Intent(getContext(),ExecutionActivity.class);

                                    //对输入数据的格式进行判断
                                    if(text.getText().toString().matches("[0-9]||[1-9][0-9]||[1-6][0-9][0-9]||7[0-1][0-9]||720")&&(!text.getText().toString().isEmpty())) {
                                        long lastTime = Long.valueOf(text.getText().toString()).longValue();
                                        startJob.putExtra("title", mData.get(position).get("title").toString());
                                        startJob.putExtra("timeStamp", mData.get(position).get("timeStamp").toString());
                                        startJob.putExtra("lastTime", lastTime);
                                        startActivity(startJob);
                                    }
                                    else{
                                        new AlertDialog.Builder(getContext()).setTitle("FBI WARNING")
                                                .setMessage("任务时长应为0-720之间的任意一个数字，请重新输入")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent restartIntent = new Intent(getContext(),JobDetailsAcitiviy.class);
                                                        startActivity(restartIntent);
                                                    }
                                                }).show();
                                    }
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
                    Intent editIntent = new Intent(getContext(),AddJobActivity.class);
                    editIntent.putExtra("timeStamp",mData.get(position).get("timeStamp").toString());
                    startActivity(editIntent);
                }
            });
            return convertView;
        }
    }

}
